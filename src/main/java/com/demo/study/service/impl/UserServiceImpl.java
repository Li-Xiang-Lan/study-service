package com.demo.study.service.impl;

import com.demo.study.cache.JedisUtil;
import com.demo.study.dao.UserDao;
import com.demo.study.entity.User;
import com.demo.study.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private JedisUtil.Keys keys;
    @Autowired
    private JedisUtil.Strings strings;

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
    public User registerUser(User user) {
        int num = userDao.insertUser(user);
        if (num>0){
            user.setSeesion(getSeesion(user.getId(),user.getPhone()));
            return user;
        }else {
            return null;
        }
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


    private String getSeesion(Integer id, String phone){
        String seesion = DigestUtils.md5DigestAsHex((phone + System.currentTimeMillis()).getBytes());
        //redis存储seesion
        strings.set(id.toString(),seesion);
        return seesion;
    }
}
