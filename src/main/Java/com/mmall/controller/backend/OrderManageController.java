package com.mmall.controller.backend;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Order;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IOrderService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/5/28.
 */
@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    @Autowired
    private IOrderService iorderService;

    @RequestMapping("list.do")
    public ServerResponse<PageInfo> orderList(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtil.isEmpty(loginToken))
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
//        String userJsonStr = RedisPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//        if (user == null)
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
//        if (!iUserService.checkAdminRole(user).isSuccess())
//            return ServerResponse.createByErrorMessage("无权限操作");

        //通过拦截器进行处理
        return iorderService.manageList(pageNum, pageSize);
    }

    @RequestMapping("detail.do")
    public ServerResponse orderDetail(HttpServletRequest httpServletRequest, Long orderNo) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtil.isEmpty(loginToken))
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
//        String userJsonStr = RedisPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//        if (user == null)
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
//        if (!iUserService.checkAdminRole(user).isSuccess())
//            return ServerResponse.createByErrorMessage("无权限操作");
//        //业务逻辑

        //通过拦截器验证
        return iorderService.getOrderDetail(null, orderNo);
    }


    @RequestMapping("search.do")
    public ServerResponse manageSearch(HttpServletRequest httpServletRequest, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtil.isEmpty(loginToken))
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
//        String userJsonStr = RedisPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//        if (user == null)
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
//        if (!iUserService.checkAdminRole(user).isSuccess())
//            return ServerResponse.createByErrorMessage("无权限操作");


        return iorderService.manageSearch(orderNo, pageNum, pageSize);
    }

    @RequestMapping("send_goods.do")
    public ServerResponse<String> sendGoods(HttpServletRequest httpServletRequest, Long orderNo) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtil.isEmpty(loginToken))
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
//        String userJsonStr = RedisPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);
//        if (user == null)
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
//        if (!iUserService.checkAdminRole(user).isSuccess())
//            return ServerResponse.createByErrorMessage("无权限操作");
        
        //业务逻辑
        return iorderService.manageSendGood(orderNo);
    }

}
