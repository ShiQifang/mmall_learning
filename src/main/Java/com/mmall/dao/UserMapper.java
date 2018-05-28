package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    //传递多个参数，需要param注解
    User selectLogin(@Param("username") String username, @Param("password") String password);

    //查找问题
    String selectQuestionByUsername(String username);

    //获取
    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String Answer);

    //更新新的密码
    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    //查询密码，加userId的目的是为了防止横向越权
    int checkPassword(@Param("password") String password, @Param("userId") Integer userId);


    int checkEmailByUserId(@Param("email") String email, @Param("userId") int userId);
}