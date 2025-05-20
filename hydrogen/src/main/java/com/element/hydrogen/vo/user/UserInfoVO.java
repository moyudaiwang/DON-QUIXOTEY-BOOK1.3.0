package com.element.hydrogen.vo.user;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName UserInfoVO
 * @Description 用户信息vo
 * @Author yanyu
 * @Date 2024/12/1 15:22
 * @Version 1.0
 */
@Data
public class UserInfoVO {
    private String userId;

    private String userName;

    private String nickname;

    private String userForeignName;

    private String sex;

    private String birthday;

    private String phoneNo;

    private String email;

    private String alg;

    private String address;

    private String avatar;

    private String userStatus;

    private String remark;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private String donUserInfoId;


    /**
     * 密码
     */
    private String pwd;

    /**
     * 账号状态
     */
    private String accountStatus;
}
