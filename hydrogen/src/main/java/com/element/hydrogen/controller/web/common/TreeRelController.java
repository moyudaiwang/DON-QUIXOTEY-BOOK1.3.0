package com.element.hydrogen.controller.web.common;

import cn.hutool.http.server.HttpServerRequest;
import com.element.hydrogen.constant.common.ResponseJsonConstant;
import com.element.hydrogen.entity.common.ResponseJson;
import com.element.hydrogen.entity.common.TreeRelEntity;
import com.element.hydrogen.service.common.TreeRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName TreeRelController
 * @Description 树结构Controller
 * @Author yanyu
 * @Date 2024-12-01 19:02:38
 * @Version 1.0
 */
@RestController
@RequestMapping("/web/treeRel")
public class TreeRelController {

    @Autowired
    private TreeRelService treeRelService;

    public static ResponseJsonConstant res;

    @CrossOrigin
    @RequestMapping(value = "/query",method = RequestMethod.POST)
    public ResponseJson query(@RequestBody TreeRelEntity treeRelEntity){
        return treeRelService.query(treeRelEntity);
    }

    @CrossOrigin
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public ResponseJson insert(HttpServerRequest request, @RequestBody TreeRelEntity treeRelEntity){
        ResponseJson resJson = new ResponseJson();
        treeRelEntity.setOperator("molecule");
        try{
            resJson =treeRelService.insert(treeRelEntity);
        }catch (Exception e){
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_ADD);
        }
        return resJson;
    }

    @CrossOrigin
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseJson update(HttpServerRequest request,@RequestBody TreeRelEntity treeRelEntity){
        ResponseJson resJson = new ResponseJson();
        treeRelEntity.setOperator("molecule");
        try{
            resJson =treeRelService.update(treeRelEntity);
        }catch (Exception e){
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_UPD);
        }
        return resJson;
    }

    @CrossOrigin
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseJson delete(@RequestBody List<TreeRelEntity> donUserInfoEntityList){
        ResponseJson resJson = new ResponseJson();
        try{
            resJson =treeRelService.delete(donUserInfoEntityList);
        }catch (Exception e){
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_DEL);
        }
        return resJson;
    }

    /**
     * 根据模块查询树结构（用于下拉框）
     * @param treeRelEntity
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/queryByModule",method = RequestMethod.POST)
    public ResponseJson queryByModule(@RequestBody TreeRelEntity treeRelEntity){
        return treeRelService.queryByModule(treeRelEntity);
    }
}
