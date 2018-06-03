package com.mmall.controller.portal;

import com.github.pagehelper.StringUtil;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/5/17.
 */
@Controller
@RequestMapping("/user/springsession/")
public class UserSpringSessionController {
    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {
        //service->mybatis->dao
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            //第一期
            session.setAttribute(Const.CURRENT_USER, response.getData());

            //第二期，将登录信息存储在redis中
            //sessionId:74DD7417258581DF27873A72725B5BA4
//            CookieUtil.writeLoginToken(httpServletResponse, session.getId());

            RedisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), Const.RedisCacheExTime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        CookieUtil.delLoginToken(httpServletRequest, httpServletResponse);
        RedisPoolUtil.del(loginToken);//从redisPool中删除
        return ServerResponse.createBySuccess();
    }


    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session, HttpServletRequest httpServletRequest) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if (StringUtil.isEmpty(loginToken))
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
//        String userJsonStr = RedisPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null)
            return ServerResponse.createBySuccess(user);
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
    }


}
