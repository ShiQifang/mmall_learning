package com.mmall.task;

/**
 * Created by Administrator on 2018/6/3.
 */

import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时关闭未付款的订单
 */
@Slf4j
@Component
public class CloseOrderTask {
    @Autowired
    private IOrderService iOrderService;

    @Scheduled(cron = "0 */1 * * * ?")//每一分钟执行一次
    public void closeOrderTaskV1() {
        log.info("关闭订单定时任务启动");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", String.valueOf(2)));
        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }


}
