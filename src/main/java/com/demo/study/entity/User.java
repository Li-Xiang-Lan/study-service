package com.demo.study.entity;

public class User {
    private Integer id;
    private String userName;
    private Integer userSex;
    private String phone;
    private String headUrl;

    private String seesion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserSex() {
        return userSex;
    }

    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getSeesion() {
        return seesion;
    }

    public void setSeesion(String seesion) {
        this.seesion = seesion;
    }
}
