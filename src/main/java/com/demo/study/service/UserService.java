package com.demo.study.service;

import com.demo.study.entity.MoneyRecordBean;
import com.demo.study.entity.RechargeMoneyListBean;
import com.demo.study.entity.User;

import java.util.List;

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
     * @param phone
     * @return
     */
    User registerUser(String phone);

    /**
     * 插入用户钱包数据
     * @param userId
     * @return
     */
    int insertUserWallet(Integer userId);

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

    /**
     * 获取用户钱包数量
     * @param userId
     * @return
     */
    int getUserMoney(Integer userId);

    /**
     * 获取充值列表
     * @return
     */
    List<RechargeMoneyListBean> getRechargeList();

    /**
     * 根据id获取充值金额
     * @param id
     * @return
     */
    int getMoneyById(Integer id);

    /**
     * 用户充值
     * @param userId
     * @param money
     * @return
     */
    int updateUserMoney(Integer userId,Integer money);

    //获取消费记录
    List<MoneyRecordBean> getMoneyRecord(Integer userId,Integer pageNum,Integer pageSize,Integer type);
}
