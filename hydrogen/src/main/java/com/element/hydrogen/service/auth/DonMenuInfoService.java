package com.element.hydrogen.service.auth;

import com.element.hydrogen.entity.auth.DonMenuInfoEntity;

import java.util.List;

public interface DonMenuInfoService {

    /**
     * 获取菜单树结构
     */
    List<DonMenuInfoEntity> queryMenuTree();

    /**
     * 新增菜单
     */
    void saveMenu(DonMenuInfoEntity menu);

    /**
     * 修改菜单
     */
    void updateMenu(DonMenuInfoEntity menu);

    /**
     * 删除菜单
     */
    void deleteMenu(String menuId);

    /**
     * 获取菜单详情
     */
    DonMenuInfoEntity getMenuById(String menuId);
}