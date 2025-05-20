package com.element.hydrogen.controller.web.auth;

import com.element.hydrogen.entity.auth.DonMenuInfoEntity;
import com.element.hydrogen.service.auth.DonMenuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 */
@RestController
@RequestMapping("/web/menu")
public class DonMenuInfoController {

    @Autowired
    private DonMenuInfoService menuService;
    /**
     * 获取菜单树
     */
    @PostMapping("/queryMenuTree")
    public List<DonMenuInfoEntity> queryMenuTree() {
        return menuService.queryMenuTree();
    }

    /**
     * 获取菜单详情
     */
    @PostMapping("/getMenu")
    public DonMenuInfoEntity getMenu(String menuId) {
        return menuService.getMenuById(menuId);
    }

    /**
     * 新增菜单
     */
    @PostMapping("/save")
    public void saveMenu(@RequestBody DonMenuInfoEntity menu) {
        menuService.saveMenu(menu);
    }

    /**
     * 修改菜单
     */
    @PostMapping("/update")
    public void updateMenu(@RequestBody DonMenuInfoEntity menu) {
        menuService.updateMenu(menu);
    }

    /**
     * 删除菜单
     */
    @PostMapping("/delete")
    public void deleteMenu(String menuId) {
        menuService.deleteMenu(menuId);
    }
}