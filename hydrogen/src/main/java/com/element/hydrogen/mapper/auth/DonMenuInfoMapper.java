package com.element.hydrogen.mapper.auth;

import com.element.hydrogen.entity.auth.DonMenuInfoEntity;

import java.util.List;

public interface DonMenuInfoMapper {
    int deleteByPrimaryKey(String DonMenuInfoEntityId);

    int insert(DonMenuInfoEntity record);

    int insertSelective(DonMenuInfoEntity record);

    DonMenuInfoEntity selectByPrimaryKey(String DonMenuInfoEntityId);

    int updateByPrimaryKeySelective(DonMenuInfoEntity record);

    int updateByPrimaryKey(DonMenuInfoEntity record);

    List<DonMenuInfoEntity> query(DonMenuInfoEntity donMenuInfoEntity);

    /**
     * 查询所有菜单列表
     */
    /**
     * 查询所有菜单列表
     */
    List<DonMenuInfoEntity> selectAllMenus();

    /**
     * 查询子菜单
     */
    List<DonMenuInfoEntity> selectMenusByPid(String menuPid);

    /**
     * 查询正常状态的菜单
     */
    List<DonMenuInfoEntity> selectNormalMenus();


    /**
     * 根据ID查询菜单
     */
    DonMenuInfoEntity selectMenuById(String menuId);

    /**
     * 新增菜单
     */
    int insertMenu(DonMenuInfoEntity menu);

    /**
     * 更新菜单
     */
    int updateMenu(DonMenuInfoEntity menu);

    /**
     * 删除菜单
     */
    int deleteMenuById(String menuId);
}