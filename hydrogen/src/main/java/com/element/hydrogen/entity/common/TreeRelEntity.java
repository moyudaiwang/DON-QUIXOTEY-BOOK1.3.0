package com.element.hydrogen.entity.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 树结构实体
 */

@Data
public class TreeRelEntity extends PageResultEntity implements Serializable {
    private String donTreeRelId;

    private String module;

    private String id;

    private String name;

    private String pId;

    private String pName;

    private String level;

    private String seq;

    private String remark;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;



}