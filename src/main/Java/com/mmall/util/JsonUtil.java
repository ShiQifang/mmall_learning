package com.mmall.util;

import com.github.pagehelper.StringUtil;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2018/5/30.
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
        //取消默认转换timestamps形式----Json序列化时会将DATE默认转化为TIMESTAMPS
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        //所有的日期格式都统一为以下的样式
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
        //忽略空bean转Json的一个错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        //忽略在json序列化中存在，但是在java对象中不存在对应属性的情况，防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String obj2String(T obj) {
        if (obj == null)
            return null;
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("parse object to String error", e);
            return null;
        }
    }

    public static <T> String obj2StringPretty(T obj) {
        if (obj == null)
            return null;
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("parse object to String error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtil.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : (T) objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.warn("parse string to object error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtil.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (IOException e) {
            log.warn("parse string to object error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.warn("parse String to Object error", e);
            return null;
        }
    }


    public static void main(String[] args) {
        User user = new User();
        user.setId(1);
        user.setEmail("gely@happymmall.com");
        String userJsonPretty = JsonUtil.obj2StringPretty(user);
        log.info("userJsonPretty:{}", userJsonPretty);

//        User u2 = new User();
//        u2.setId(2);
//        u2.setEmail("gely@happymmall2.com");
//
//        String user1Json = JsonUtil.obj2String(u1);
//        String user1JsonPretty = JsonUtil.obj2StringPretty(u1);
//        log.info("user1Json:{}", user1Json);
//        log.info("user1JsonPretty:{}", user1JsonPretty);
//
//        User user = JsonUtil.string2Obj(user1Json, User.class);
//        log.info(user.toString());
//
//        List<User> userList = Lists.newArrayList();
//        userList.add(u1);
//        userList.add(u2);
//
//        String userListStr = JsonUtil.obj2StringPretty(userList);
//        log.info("====================================");
//        log.info(userListStr);
//        List<User> userListObj1 = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() {
//        });
//        List<User> userListObj2 = JsonUtil.string2Obj(userListStr, List.class, User.class);
//        System.out.println(userListObj1.toString());
    }


}
