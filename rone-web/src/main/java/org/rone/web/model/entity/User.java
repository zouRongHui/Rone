package org.rone.web.model.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 用户(User)实体类
 * @author rone
 */
public class User implements Serializable {
    private static final long serialVersionUID = 372490527722647173L;
    /**
     * 用户编号
     */
    @ApiModelProperty("用户编号")
    private String userNo;
    /**
     * 用户名
     */
    @ApiModelProperty("用户姓名")
    private String userName;
    /**
     * 说明
     */
    @ApiModelProperty("备注")
    private String userExplain;

    public User() {
    }

    public User(String userNo, String userName, String userExplain) {
        this.userNo = userNo;
        this.userName = userName;
        this.userExplain = userExplain;
    }

    @Override
    public String toString() {
        return "User{" +
                "userNo='" + userNo + '\'' +
                ", userName='" + userName + '\'' +
                ", userExplain='" + userExplain + '\'' +
                '}';
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserExplain() {
        return userExplain;
    }

    public void setUserExplain(String userExplain) {
        this.userExplain = userExplain;
    }

}