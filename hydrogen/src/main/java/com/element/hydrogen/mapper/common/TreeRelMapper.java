package com.element.hydrogen.mapper.common;

import com.element.hydrogen.entity.common.TreeRelEntity;

import java.util.List;

public interface TreeRelMapper {
    int deleteByPrimaryKey(String donTreeRelId);

    int insert(TreeRelEntity record);

    int insertSelective(TreeRelEntity record);

    TreeRelEntity selectByPrimaryKey(String donTreeRelId);

    int updateByPrimaryKeySelective(TreeRelEntity record);

    int updateByPrimaryKey(TreeRelEntity record);

    List<TreeRelEntity> query(TreeRelEntity treeRelEntity);

    /**
     * 根据模块查询
     * @param treeRelEntity
     * @return
     */
    List<TreeRelEntity> queryByModule(TreeRelEntity treeRelEntity);
}