package com.demo.study.web.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.study.entity.BaseBackBean;
import com.demo.study.entity.MoneyRecordBean;
import com.demo.study.entity.RechargeMoneyListBean;
import com.demo.study.entity.User;
import com.demo.study.service.UserService;
import com.demo.study.util.Status;
import com.demo.study.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
                            User registerUser = userService.registerUser(phone);
                            //添加用户钱包数据
                            userService.insertUserWallet(registerUser.getId());
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

    @RequestMapping(value = "getmoney.do",method = RequestMethod.GET)
    @ResponseBody
    private BaseBackBean getRechargeList(@RequestHeader("userId")Integer userId){
        BaseBackBean backBean = new BaseBackBean();
        try {
            int userMoney = userService.getUserMoney(userId);
            backBean.setStatus(Status.STATUS_SUCCESS);
            Map<String,Integer> map=new HashMap<>();
            map.put("money",userMoney);
            backBean.setData(map);
        } catch (Exception e) {
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("获取钱包数量异常");
        }
        return backBean;
    }

    @RequestMapping(value = "getrechargelist.do",method = RequestMethod.GET)
    @ResponseBody
    private BaseBackBean getRechargeList(){
        BaseBackBean backBean = new BaseBackBean();
        try {
            List<RechargeMoneyListBean> rechargeList = userService.getRechargeList();
            if (null==rechargeList){
                backBean.setStatus(Status.STATUS_FAIL);
                backBean.setErrorMsg("获取充值列表出错");
            }else {
                backBean.setStatus(Status.STATUS_SUCCESS);
                backBean.setData(rechargeList);
            }
        } catch (Exception e) {
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("获取充值列表异常");
        }
        return backBean;
    }


    @RequestMapping(value = "updatemoney.do",method = RequestMethod.POST)
    @ResponseBody
    private BaseBackBean updateUserMoney(@RequestBody String str,@RequestHeader("userId")Integer userId){
        BaseBackBean backBean = new BaseBackBean();
        try {
            JSONObject jsonObject = JSON.parseObject(str);
            Integer id = jsonObject.getInteger("id");
            //查询指定id的金额
            int moneyById = 0;
            try {
                moneyById = userService.getMoneyById(id);
            } catch (Exception e) {
                backBean.setStatus(Status.STATUS_FAIL);
                backBean.setErrorMsg("获取充值金额异常");
            }
            //前端传来的money
            int money = jsonObject.getInteger("money");
            if (money!=moneyById){
                backBean.setStatus(Status.STATUS_FAIL);
                backBean.setErrorMsg("充值列表已变更，请刷新重试");
            }else {
                int num = userService.updateUserMoney(userId, moneyById);
                if (num<=0){
                    backBean.setStatus(Status.STATUS_FAIL);
                    backBean.setErrorMsg("充值失败");
                }else {
                    backBean.setStatus(Status.STATUS_SUCCESS);
                }
            }
        } catch (Exception e) {
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg(e.getMessage());
        }
        return backBean;
    }

    @RequestMapping(value = "getmoneyrecord.do",method = RequestMethod.POST)
    @ResponseBody
    private BaseBackBean getMoneyRecord(@RequestBody String str,@RequestHeader("userId")Integer userId){
        BaseBackBean backBean = new BaseBackBean();
        try {
            JSONObject jsonObject = JSON.parseObject(str);
            List<MoneyRecordBean> list = userService.getMoneyRecord(userId,
                    jsonObject.getInteger("pageNum"),
                    jsonObject.getInteger("pageSize"),
                    jsonObject.getInteger("type"));
            backBean.setStatus(Status.STATUS_SUCCESS);
            backBean.setData(list);
        } catch (Exception e) {
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("获取消费记录异常");
        }
        return backBean;
    }
}
