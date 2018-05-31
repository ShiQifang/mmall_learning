package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Administrator on 2018/5/17.
 */
public class Const {
    public final static String TOKEN_PREFIX = "token_";
    public final static String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface RedisCacheExTime {
        int REDIS_SESSION_EXTIME = 60 * 30;//30分钟
    }

    public interface Role {
        int ROLE_CUSTOMER = 0;//普通用户
        int ROLE_ADMIN = 1;//管理员
    }

    public enum ProductStatusEnum {
        ON_SALE(1, "在线");
        private String value;
        private int code;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

    }

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Cart {
        int CHECKED = 1;//购物车选中
        int UNCHECKED = 0;//购物车未选中

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public enum OrderStatusEnum {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAYID(20, "已支付"),
        SHIPPIED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_FINISH(60, "订单关闭");

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code)
                    return orderStatusEnum;
            }
            throw new RuntimeException("没有找到对应的枚举");
        }

    }

    public interface AlipayCallback {
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public enum PayPlatformEnum {
        ALIPAY(0, "ALIPAY");

        PayPlatformEnum(int code, String platform) {
            this.code = code;
            this.platform = platform;
        }

        int code;
        String platform;

        public int getCode() {
            return code;
        }
    }

    public enum paymentTypeEnum {
        ONLINE_PAY(0, "在线支付");

        paymentTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        int code;
        String desc;

        public String getDesc() {
            return desc;
        }

        public int getCode() {
            return code;
        }

        public static paymentTypeEnum codeOf(int code) {
            for (paymentTypeEnum paymentTypeEnum : values()) {
                if (paymentTypeEnum.getCode() == code)
                    return paymentTypeEnum;
            }
            throw new RuntimeException("没有找找到对应的枚举");
        }
    }

}
