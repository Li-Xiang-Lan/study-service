package com.demo.study.dao;

import com.demo.study.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    //查询课程是否被购买
    int queryCourseIsBuy(@Param("userId") Integer userId,@Param("courseId")Integer courseId);
    //判断用户是否已注册
    int queryUserExists(String phone);
    //注册用户
    int insertUser(User user);
    //查询用户信息
    User queryUserInfo(@Param("phone") String phone,@Param("userId") String userId);
    //更新用户信息
    int updateUserInfo(User user);
}
