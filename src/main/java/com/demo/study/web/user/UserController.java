package com.demo.study.web.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.study.entity.BaseBackBean;
import com.demo.study.entity.User;
import com.demo.study.service.UserService;
import com.demo.study.util.Status;
import com.demo.study.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "getcode",method = RequestMethod.POST)
    @ResponseBody
    private BaseBackBean getCode(@RequestBody String str){
        BaseBackBean backBean = new BaseBackBean();
        try {
            JSONObject jsonObject = JSON.parseObject(str);
            String phone = jsonObject.getString("phone");
            if (Util.isEmpty(phone)||phone.length()!=11||!phone.startsWith("1")){
                backBean.setStatus(Status.STATUS_FAIL);
                backBean.setErrorMsg("手机号有误");
            }else {
                Map<String,String> map=new HashMap<>();
                String smsCode = userService.getSmsCode(phone);
                map.put("code",smsCode);
                backBean.setData(map);
                backBean.setStatus(Status.STATUS_SUCCESS);
            }
        } catch (Exception e) {
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("获取验证码异常");
        }
        return backBean;
    }

    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    private BaseBackBean login(@RequestBody String str){
        BaseBackBean backBean = new BaseBackBean();
        try {
            JSONObject jsonObject = JSON.parseObject(str);
            String phone = jsonObject.getString("phone");
            if (Util.isEmpty(phone)||phone.length()!=11||!phone.startsWith("1")){
                backBean.setStatus(Status.STATUS_FAIL);
                backBean.setErrorMsg("手机号有误");
            }else {
                String code = jsonObject.getString("code");
                if (Util.isEmpty(code)||code.length()!=6){
                    backBean.setStatus(Status.STATUS_FAIL);
                    backBean.setErrorMsg("验证码有误");
                }else {
                    boolean checkSmsCode = userService.checkSmsCode(phone, code);
                    //验证码正确
                    if (checkSmsCode){
                        //查询该用户是否已注册
                        int exists = userService.getUserExists(phone);
                        //未注册 添加用户信息
                        if (exists<=0){
                            User user=new User();
                            user.setUserName("新用户"+phone.substring(7,11));
                            user.setPhone(phone);
                            user.setHeadUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566989694337&di=afcb2255fdbf308d08f3fc5006ee3cb3&imgtype=0&src=http%3A%2F%2F07imgmini.eastday.com%2Fmobile%2F20180919%2F20180919101827_4b72047aec8a8657fb647b621c824288_2.jpeg");
                            user.setUserSex(1);
                            User registerUser = userService.registerUser(user);
                            //注册失败
                            if (null==registerUser){
                                backBean.setStatus(Status.STATUS_FAIL);
                                backBean.setErrorMsg("登录失败");
                            }else {  //登录成功
                                backBean.setStatus(Status.STATUS_SUCCESS);
                                backBean.setData(registerUser);
                            }
                        }else {  //已注册
                            //查询用户信息
                            User userInfo = userService.getUserInfo(phone,null);
                            if (null==userInfo){
                                backBean.setStatus(Status.STATUS_FAIL);
                                backBean.setErrorMsg("登录失败");
                            }else {
                                backBean.setStatus(Status.STATUS_SUCCESS);
                                backBean.setData(userInfo);
                            }
                        }
                    }else {
                        backBean.setStatus(Status.STATUS_FAIL);
                        backBean.setErrorMsg("验证码有误");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("============"+e.getMessage());
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("登录异常");
        }
        return backBean;
    }

    @RequestMapping(value = "updateuserinfo.do",method = RequestMethod.POST)
    @ResponseBody
    private BaseBackBean updateUserInfo(@RequestBody User user, @RequestHeader(value = "userId")Integer userId){
        BaseBackBean backBean = new BaseBackBean();
        try {
            if (null==user){
                backBean.setStatus(Status.STATUS_FAIL);
                backBean.setErrorMsg("入参错误");
            }else {
                user.setId(userId);
                int num = userService.updateUserInfo(user);
                if (num<=0){
                    backBean.setStatus(Status.STATUS_FAIL);
                    backBean.setErrorMsg("修改用户信息失败");
                }else {
                    backBean.setStatus(Status.STATUS_SUCCESS);
                }
            }
        } catch (Exception e) {
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("修改用户信息异常");
        }
        return backBean;
    }
}
