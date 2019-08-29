package com.demo.study.dao;

import com.demo.study.entity.MoneyRecordBean;
import com.demo.study.entity.RechargeMoneyListBean;
import com.demo.study.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    //查询课程是否被购买
    int queryCourseIsBuy(@Param("userId") Integer userId,@Param("menuId")Integer menuId);
    //判断用户是否已注册
    int queryUserExists(String phone);
    //注册用户
    int insertUser(User user);
    //添加用户钱包数据
    int insertUserWallet(@Param("userId") Integer userId);
    //查询用户信息
    User queryUserInfo(@Param("phone") String phone,@Param("userId") String userId);
    //更新用户信息
    int updateUserInfo(User user);
    //获取用户钱包数量
    int queryUserMoney(Integer userId);
    //获取充值列表
    List<RechargeMoneyListBean> queryRechargeList();
    //查询指定充值id的金额
    int queryRechargeMoenyById(Integer id);
    //更新用户钱包金额
    int updateUserMoney(@Param("userId") Integer userId,@Param("money")Integer money);
    //获取消费记录
    List<MoneyRecordBean> queryMoneyRecord(@Param("userId")Integer userId,
                                           @Param("pageNum")Integer pageNum,
                                           @Param("pageSize")Integer pageSize,
                                           @Param("type")Integer type);
}
