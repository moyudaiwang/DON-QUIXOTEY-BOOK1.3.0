package com.element.hydrogen.service.common;

import com.element.hydrogen.entity.common.ResponseJson;
import com.element.hydrogen.entity.common.TreeRelEntity;

import java.util.List;

/**
 * @InterfaceName TreeRelService
 * @Description TODO
 * @Author yanyu
 * @Date 2024-12-01 19:02:38
 * @Version 1.0
 */
public interface TreeRelService {
    ResponseJson query(TreeRelEntity treeRelEntity);

    ResponseJson insert(TreeRelEntity treeRelEntity);

    ResponseJson update(TreeRelEntity treeRelEntity);

    ResponseJson delete(List<TreeRelEntity> treeRelEntityList);

    /**
     * 根据模块查询树结构（用于下拉框）
     * @param treeRelEntity
     * @return
     */
    ResponseJson queryByModule(TreeRelEntity treeRelEntity);
}
