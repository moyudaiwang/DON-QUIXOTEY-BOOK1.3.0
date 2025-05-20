package com.element.hydrogen.entity.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单信息表
 */
@Data
public class DonMenuInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String donMenuInfoId;

    private String menuId;

    private String menuPid;

    private String menuName;

    private String menuUrl;

    private String menuType;

    private String menuIcon;

    private String menuOrder;

    private String menuStatus;

    private String remark;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private List<DonMenuInfoEntity> children;  // 子菜单列表

}