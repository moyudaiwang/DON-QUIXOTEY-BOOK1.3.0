package com.element.hydrogen.service.common.impl;

import com.element.hydrogen.constant.common.ResponseJsonConstant;
import com.element.hydrogen.dto.common.TreeNode;
import com.element.hydrogen.entity.common.ResponseJson;
import com.element.hydrogen.entity.common.TreeRelEntity;
import com.element.hydrogen.mapper.common.TreeRelMapper;
import com.element.hydrogen.service.common.TreeRelService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TreeRelServiceImpl
 * @Description TODO
 * @Author yanyu
 * @Date 2024-12-01 19:02:38
 * @Version 1.0
 */
@Service
public class TreeRelServiceImpl implements TreeRelService {

    @Autowired
    public TreeRelMapper donLogInfoMapper;

    public static ResponseJsonConstant res;

    @Override
    public ResponseJson query(TreeRelEntity treeRelEntity) {
        ResponseJson resJson = new ResponseJson();
        PageHelper.startPage(treeRelEntity.getPageNum(), treeRelEntity.getPageSize());
        List<TreeRelEntity> treeRelEntityList = donLogInfoMapper.query(treeRelEntity);
        PageInfo<TreeRelEntity> pageInfo = new PageInfo<TreeRelEntity>(treeRelEntityList);
        resJson.setCode(res.SUCCESS);
        resJson.setObject(pageInfo);
        return resJson;
    }

    @Override
    public ResponseJson insert(TreeRelEntity treeRelEntity) {
        ResponseJson resJson = new ResponseJson();
        try {
            donLogInfoMapper.insert(treeRelEntity);
            resJson.setCode(res.SUCCESS);
            resJson.setMsg(res.SUC_ADD);
        } catch (Exception e) {
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_ADD);
        }
        return resJson;
    }

    @Override
    public ResponseJson update(TreeRelEntity treeRelEntity) {
        ResponseJson resJson = new ResponseJson();
        try {
            donLogInfoMapper.updateByPrimaryKey(treeRelEntity);
            resJson.setCode(res.SUCCESS);
            resJson.setMsg(res.SUC_UPD);
        } catch (Exception e) {
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_UPD);
        }
        return resJson;
    }

    @Override
    public ResponseJson delete(List<TreeRelEntity> treeRelEntityList) {
        ResponseJson resJson = new ResponseJson();
        try {
            for (TreeRelEntity treeRelEntity : treeRelEntityList) {
                donLogInfoMapper.deleteByPrimaryKey(treeRelEntity.getDonTreeRelId());
            }
            resJson.setCode(res.SUCCESS);
            resJson.setMsg(res.SUC_DEL);
        } catch (Exception e) {
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_DEL);
        }
        return resJson;
    }

    /**
     * 根据模块查询树结构（用于下拉框）
     *
     * @param treeRelEntity
     * @return
     */
    @Override
    public ResponseJson queryByModule(TreeRelEntity treeRelEntity) {
        ResponseJson resJson = new ResponseJson();
        List<TreeRelEntity> treeRelEntityList = donLogInfoMapper.queryByModule(treeRelEntity);
        List<TreeNode> nodeList = new ArrayList<>();
        treeRelEntityList.forEach(entity ->{
            TreeNode treeNode = new TreeNode();
            treeNode.setValue(entity.getId());
            treeNode.setLabel(entity.getName());
            treeNode.setParentId(entity.getPId());
            nodeList.add(treeNode);
        });
        List<TreeNode> treeRel = buildTree(nodeList);
        resJson.setCode(res.SUCCESS);
        resJson.setObject(treeRel);
        return resJson;
    }

    /**
     * list转树结构
     * @param nodes
     * @return
     */
    public static List<TreeNode> buildTree(List<TreeNode> nodes) {
        Map<String, TreeNode> nodeMap = new HashMap<>();
        List<TreeNode> rootNodes = new ArrayList<>();
        // 将所有节点存入 Map 中
        for (TreeNode node : nodes) {
            nodeMap.put(node.getValue(), node);
        }
        // 构建树结构
        for (TreeNode node : nodes) {
            if ("0".equals(node.getParentId())) {
                rootNodes.add(node); // 根节点
            } else {
                TreeNode parent = nodeMap.get(node.getParentId());
                if (parent != null) {
                    parent.addChild(node); // 添加为父节点的子节点
                }
            }
        }
        return rootNodes;
    }
}
