package com.demo.study.service.impl;

import com.demo.study.cache.JedisUtil;
import com.demo.study.dao.CourseDao;
import com.demo.study.dao.UserDao;
import com.demo.study.entity.MoneyRecordBean;
import com.demo.study.entity.RechargeMoneyListBean;
import com.demo.study.entity.User;
import com.demo.study.service.UserService;
import com.demo.study.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private CourseDao courseDao;

    @Autowired
    private JedisUtil.Keys keys;
    @Autowired
    private JedisUtil.Strings strings;

    private final static String RECHARGE_KEY="recharge";

    @Override
    public int getCourseIsBuy(Integer userId, Integer courseId) {
        return userDao.queryCourseIsBuy(userId, courseId);
    }

    @Override
    public String getSmsCode(String phone) {
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        strings.set(phone,code);
        return code;
    }

    @Override
    public boolean checkSmsCode(String phone, String code) {
        if (keys.exists(phone)&&strings.get(phone).equals(code)){
            //清空redis验证码
            strings.set(phone,"");
            return true;
        }
        return false;
    }

    @Override
    public int getUserExists(String phone) {
        return userDao.queryUserExists(phone);
    }

    @Override
    @Transactional
    public User registerUser(String phone) {
        try {
            User user=new User();
            user.setUserName("新用户"+phone.substring(7,11));
            user.setPhone(phone);
            user.setHeadUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566989694337&di=afcb2255fdbf308d08f3fc5006ee3cb3&imgtype=0&src=http%3A%2F%2F07imgmini.eastday.com%2Fmobile%2F20180919%2F20180919101827_4b72047aec8a8657fb647b621c824288_2.jpeg");
            user.setUserSex(1);
            int num = userDao.insertUser(user);
            if (num>0){
                user.setSeesion(getSeesion(user.getId(),user.getPhone()));
                return user;
            }else {
                throw new RuntimeException("注册用户失败");
            }
        }catch (Exception e){
            throw new RuntimeException("注册用户异常");
        }
    }

    @Override
    @Transactional
    public int insertUserWallet(Integer userId) {
        int num = userDao.insertUserWallet(userId);
        if (num<=0){
            throw new RuntimeException("添加钱包数据出错");
        }
        return num;
    }

    @Override
    public User getUserInfo(String phone,String userId) {
        User user = userDao.queryUserInfo(phone,userId);
        if (null!=user){
            //登录时查询用户信息 添加seesion信息
            if (null!=phone){
                user.setSeesion(getSeesion(user.getId(), user.getPhone()));
            }
            return user;
        }
        return null;
    }

    @Override
    public int updateUserInfo(User user) {
        return userDao.updateUserInfo(user);
    }

    @Override
    public int getUserMoney(Integer userId) {
        return userDao.queryUserMoney(userId);
    }

    @Override
    public List<RechargeMoneyListBean> getRechargeList() {
        List<RechargeMoneyListBean> list=new ArrayList<>();
        if (keys.exists(RECHARGE_KEY)&&!Util.isEmpty(strings.get(RECHARGE_KEY))){
            List<RechargeMoneyListBean> beanList=new Gson().fromJson(strings.get(RECHARGE_KEY),new TypeToken<List<RechargeMoneyListBean>>(){}.getType());
            list.addAll(beanList);
        }else {
            list.addAll(userDao.queryRechargeList());
            strings.set(RECHARGE_KEY,new Gson().toJson(list));
        }
        return list;
    }

    @Override
    public int getMoneyById(Integer id) {
        return userDao.queryRechargeMoenyById(id);
    }

    @Override
    @Transactional
    public int updateUserMoney(Integer userId, Integer money) {
        //充值
        int updateUserMoney = userDao.updateUserMoney(userId, money);
        if (updateUserMoney<=0){
            throw new RuntimeException("充值金额失败");
        }else {
            //添加充值记录
            MoneyRecordBean recordBean = new MoneyRecordBean();
            recordBean.setUserId(userId);
            recordBean.setCreateTime(new Date());
            recordBean.setTitle("充值账户金额");
            recordBean.setPrice(money);
            recordBean.setType(2);
            int courseBuy = courseDao.insertMoneyRecord(recordBean);
            if (courseBuy<=0){
                throw new RuntimeException("添加消费记录失败");
            }else {
                return courseBuy;
            }
        }
    }

    @Override
    public List<MoneyRecordBean> getMoneyRecord(Integer userId, Integer pageNum, Integer pageSize, Integer type) {
        return userDao.queryMoneyRecord(userId, Util.calculateRowIndex(pageNum, pageSize), pageSize,type);
    }


    private String getSeesion(Integer id, String phone){
        String seesion = DigestUtils.md5DigestAsHex((phone + System.currentTimeMillis()).getBytes());
        //redis存储seesion
        strings.set(id.toString(),seesion);
        return seesion;
    }
}
