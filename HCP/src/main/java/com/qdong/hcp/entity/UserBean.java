package com.qdong.hcp.entity;

import java.io.Serializable;

/**
 * UserBean
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/11  11:36
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class UserBean  implements Serializable{

    private static final long serialVersionUID = 19840902L;
    private boolean mIsLogin;//这个属性是本地的,用户是否已登录


    public boolean ismIsLogin() {
        return mIsLogin;
    }

    public void setmIsLogin(boolean mIsLogin) {
        this.mIsLogin = mIsLogin;
    }

    private int userId;
    private Object name;
    private String nickname;
    private Object birth;
    private Object email;
    private Object telephone;
    private int stature;
    private int weight;
    private int gender;
    private int age;
    private String headPhoto;
    private Object provinceCode;
    private Object cityCode;
    private Object updateTime;
    private String token;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Object getBirth() {
        return birth;
    }

    public void setBirth(Object birth) {
        this.birth = birth;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getTelephone() {
        return telephone;
    }

    public void setTelephone(Object telephone) {
        this.telephone = telephone;
    }

    public int getStature() {
        return stature;
    }

    public void setStature(int stature) {
        this.stature = stature;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public Object getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Object provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Object getCityCode() {
        return cityCode;
    }

    public void setCityCode(Object cityCode) {
        this.cityCode = cityCode;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userId=" + userId +
                ", name=" + name +
                ", nickname='" + nickname + '\'' +
                ", birth=" + birth +
                ", email=" + email +
                ", telephone=" + telephone +
                ", stature=" + stature +
                ", weight=" + weight +
                ", gender=" + gender +
                ", age=" + age +
                ", headPhoto='" + headPhoto + '\'' +
                ", provinceCode=" + provinceCode +
                ", cityCode=" + cityCode +
                ", updateTime=" + updateTime +
                ", token='" + token + '\'' +
                '}';
    }
}
