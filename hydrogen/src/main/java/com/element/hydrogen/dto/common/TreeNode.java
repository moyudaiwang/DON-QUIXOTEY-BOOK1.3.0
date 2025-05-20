package com.element.hydrogen.dto.common;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TreeNode
 * @Description TODO
 * @Author yanyu
 * @Date 2024/12/1 22:16
 * @Version 1.0
 */
@Data
public class TreeNode implements Serializable {

    private String value;
    private String label;
    private String parentId;
    private List<TreeNode> children;

    public void addChild(TreeNode child) {
        if (children ==null){
            children = new ArrayList<>();
        }
        children.add(child);
    }

}
