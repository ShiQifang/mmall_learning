package com.mmall.controller.backend;

import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/5/26.
 */
@Controller
@RequestMapping("/test/")
public class TestController {

    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("get_cache.do")
    @ResponseBody
    public String getCache(String key) {
        return TokenCache.getKey(key);
    }

    @RequestMapping("test.do")
    @ResponseBody
    public String test(String str) {
        logger.info("testInfo");
        logger.warn("testWarn");
        logger.error("testError");
        return "testValue" + str;

    }

}
