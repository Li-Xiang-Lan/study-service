package com.demo.study.service;

import com.demo.study.entity.User;

public interface UserService {
    /**
     * 获取课程是否被购买
     * @param courseId
     * @param userId
     * @return
     */
    int getCourseIsBuy(Integer userId,Integer courseId);

    /**
     * 获取验证码
     * @param phone
     * @return
     */
    String getSmsCode(String phone);

    /**
     * 校验验证码
     * @param phone
     * @param code
     * @return
     */
    boolean checkSmsCode(String phone,String code);

    /**
     * 判断用户是否已经注册
     * @param phone
     * @return
     */
    int getUserExists(String phone);

    /**
     * 注册用户
     * @param user
     * @return
     */
    User registerUser(User user);

    /**
     * 通过手机号或者用户id获取用户信息
     * @param phone
     * @return
     */
    User getUserInfo(String phone,String userId);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    int updateUserInfo(User user);
}
