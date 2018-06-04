package com.mmall.task;

/**
 * Created by Administrator on 2018/6/3.
 */

import com.mmall.common.Const;
import com.mmall.common.RedisSharedPool;
import com.mmall.common.RedissonManager;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisSharedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * 定时关闭未付款的订单
 */
@Slf4j
@Component
public class CloseOrderTask {
    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedissonManager redissonManager;

    //@Scheduled(cron = "0 */1 * * * ?")//每一分钟执行一次
    public void closeOrderTaskV1() {
        log.info("关闭订单定时任务启动");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", String.valueOf(2)));
        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }

    /**
     * 当没有使用kill进程时关闭Tomcat,而使用shutdown关闭tomcat.
     * tomcat容器会调用preDestory
     * 缺点:
     * 1.若锁很多，则需要花费大量时间关闭死锁
     */
    @PreDestroy
    public void delLock() {
        //释放锁
        RedisSharedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

    @Scheduled(cron = "0 */1 * * * ?")//每一分钟执行一次
    public void closeOrderTaskV2() {
        log.info("关闭订单定时任务启动");
        //分布式锁的超时时间
        long lockTimeOut = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", String.valueOf(5000)));
        //配合时间戳结合使用
        Long setnxResult = RedisSharedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
        if (setnxResult != null && setnxResult.intValue() == 1) {
            //如果值为1，代表设置成功,获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            log.error("没有获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }

        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", String.valueOf(2)));
        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }

    //@Scheduled(cron = "0 */1 * * * ?")//每一分钟执行一次
    public void closeOrderTaskV3() {
        log.info("关闭订单定时任务启动");
        //利用起来时间戳
        //分布式锁的超时时间
        long lockTimeOut = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", String.valueOf(5000)));
        //配合时间戳结合使用
        Long setnxResult = RedisSharedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
                String.valueOf(System.currentTimeMillis() + lockTimeOut));
        if (setnxResult != null && setnxResult.intValue() == 1) {
            //如果值为1，代表设置成功,获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            //未获取到锁，继续判断，判断时间戳，看是否可以重置并获取到锁
            String lockValueStr = RedisSharedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            //若锁已经失效，则有权限获取这个锁
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)) {
                String getSetResult = RedisSharedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,
                        String.valueOf(System.currentTimeMillis() + lockTimeOut));
                //再次用当前时间戳
                //返回给定的key的旧址，使用旧值判定，是否可以获取锁
                //当key没有旧值是，即key不存在时，返回nil-->获取锁
                //这里我们set了一个新的value值，获取旧的值
                if (getSetResult == null || (getSetResult != null && (StringUtils.equals(lockValueStr, getSetResult)))) {
                    //关闭订单
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                } else {
                    log.info("没有获取到分布时间锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            } else {
                log.info("没有获取到分布时间锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }

        log.info("关闭订单定时任务结束");
    }


    /**
     * 使用单个Redisson构建任务
     */
    @Scheduled(cron = "0 */1 * * * ?")//每一分钟执行一次
    public void closeOrderTaskV4() {
        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false;
        try {
            if (getLock = lock.tryLock(2, 5, TimeUnit.SECONDS)) {
                log.info("获取{},ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", String.valueOf(2)));
                iOrderService.closeOrder(hour);
            } else {
                log.info("没有获取{},ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.info("Redisson分布式锁获取异常", e);
        } finally {
            if (!getLock)
                return;
            lock.unlock();
            log.info("Redisson分布式锁释放锁");
        }

    }


    private void closeOrder(String lockName) {
        RedisSharedPoolUtil.expire(lockName, 5);//有效期:50秒，防止死锁
        log.info("获取{},ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", String.valueOf(2)));
        //关闭订单，释放库存
        iOrderService.closeOrder(hour);

        log.info("释放锁{},ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
    }


}
