package com.element.hydrogen.service.auth.impl;

import com.element.hydrogen.entity.auth.DonMenuInfoEntity;
import com.element.hydrogen.mapper.auth.DonMenuInfoMapper;
import com.element.hydrogen.service.auth.DonMenuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DonMenuInfoServiceImpl implements DonMenuInfoService {

    @Autowired
    private DonMenuInfoMapper menuMapper;

    @Override
    public List<DonMenuInfoEntity> queryMenuTree() {
        // 获取所有正常状态的菜单
        List<DonMenuInfoEntity> allMenus = menuMapper.selectNormalMenus();
        // 构建树形结构
        return buildMenuTree(allMenus, "0"); // 假设根节点的parentId为"0"
    }

    private List<DonMenuInfoEntity> buildMenuTree(List<DonMenuInfoEntity> allMenus, String parentId) {
        List<DonMenuInfoEntity> resultMenus = new ArrayList<>();
        for (DonMenuInfoEntity menu : allMenus) {
            if (Objects.equals(menu.getMenuPid(), parentId)) {
                menu.setChildren(buildMenuTree(allMenus, menu.getMenuId()));
                resultMenus.add(menu);
            }
        }
        return resultMenus;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(DonMenuInfoEntity menu) {
        // 参数校验
        validateMenu(menu);
        // 保存菜单信息
        menuMapper.insertMenu(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(DonMenuInfoEntity menu) {
        // 参数校验
        validateMenu(menu);
        // 不能将菜单的父节点设置为自己或自己的子节点
        if (isCircularReference(menu)) {
            throw new RuntimeException("不能将菜单的父节点设置为自己或自己的子节点");
        }
        // 更新菜单信息
        menuMapper.updateMenu(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(String menuId) {
        // 查询是否有子菜单
        List<DonMenuInfoEntity> children = menuMapper.selectMenusByPid(menuId);
        if (!children.isEmpty()) {
            throw new RuntimeException("存在子菜单，不能删除");
        }
        // 删除菜单
        menuMapper.deleteMenuById(menuId);
    }

    @Override
    public DonMenuInfoEntity getMenuById(String menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * 校验菜单参数
     */
    private void validateMenu(DonMenuInfoEntity menu) {
        if (!StringUtils.hasText(menu.getMenuName())) {
            throw new RuntimeException("菜单名称不能为空");
        }
        if (!StringUtils.hasText(menu.getMenuType())) {
            throw new RuntimeException("菜单类型不能为空");
        }
        // 如果是按钮，则不需要URL
        if (!"2".equals(menu.getMenuType()) && !StringUtils.hasText(menu.getMenuUrl())) {
            throw new RuntimeException("菜单URL不能为空");
        }
        // 校验父菜单是否存在
        if (StringUtils.hasText(menu.getMenuPid()) && !"0".equals(menu.getMenuPid())) {
            DonMenuInfoEntity parentMenu = menuMapper.selectMenuById(menu.getMenuPid());
            if (parentMenu == null) {
                throw new RuntimeException("父菜单不存在");
            }
            // 如果父菜单是按钮，则不允许添加子菜单
            if ("2".equals(parentMenu.getMenuType())) {
                throw new RuntimeException("按钮类型的菜单不能添加子菜单");
            }
        }
    }

    /**
     * 检查是否存在循环引用
     */
    private boolean isCircularReference(DonMenuInfoEntity menu) {
        if (!StringUtils.hasText(menu.getMenuPid()) || "0".equals(menu.getMenuPid())) {
            return false;
        }
        String parentId = menu.getMenuPid();
        while (StringUtils.hasText(parentId) && !"0".equals(parentId)) {
            if (Objects.equals(parentId, menu.getMenuId())) {
                return true;
            }
            DonMenuInfoEntity parentMenu = menuMapper.selectMenuById(parentId);
            if (parentMenu == null) {
                break;
            }
            parentId = parentMenu.getMenuPid();
        }
        return false;
    }
}