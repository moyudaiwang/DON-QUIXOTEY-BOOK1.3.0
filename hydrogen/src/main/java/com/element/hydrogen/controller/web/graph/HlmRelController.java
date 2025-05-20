package com.element.hydrogen.controller.web.graph;

import cn.hutool.http.server.HttpServerRequest;
import com.element.hydrogen.constant.common.ResponseJsonConstant;
import com.element.hydrogen.entity.common.ResponseJson;
import com.element.hydrogen.entity.graph.DonHlmRelEntity;
import com.element.hydrogen.service.graph.HlmRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName HlmRelController
 * @Description HlmRelController
 * @Author yanyu
 * @Date 2022-02-26 22:07:28
 * @Version 1.0
 */
@RestController
@RequestMapping("/web/graph/hlmRel")
public class HlmRelController {

    @Autowired
    private HlmRelService hlmRelService;

    public static ResponseJsonConstant res;

    @CrossOrigin
    @RequestMapping(value = "/queryHlmRel",method = RequestMethod.POST)
    public ResponseJson queryHlmRel(@RequestBody DonHlmRelEntity donHlmRelEntity){
        return hlmRelService.queryHlmRel(donHlmRelEntity);
    }

    @CrossOrigin
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public ResponseJson insert(HttpServerRequest request, @RequestBody DonHlmRelEntity donHlmRelEntity){
        ResponseJson resJson = new ResponseJson();
        donHlmRelEntity.setOperator("molecule");
        try{
            resJson =hlmRelService.insert(donHlmRelEntity);
        }catch (Exception e){
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_ADD);
        }
        return resJson;
    }

    @CrossOrigin
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseJson update(HttpServerRequest request,@RequestBody DonHlmRelEntity donHlmRelEntity){
        ResponseJson resJson = new ResponseJson();
        donHlmRelEntity.setOperator("molecule");
        try{
            resJson =hlmRelService.update(donHlmRelEntity);
        }catch (Exception e){
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_UPD);
        }
        return resJson;
    }

    @CrossOrigin
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseJson delete(@RequestBody List<DonHlmRelEntity> donUserInfoEntityList){
        ResponseJson resJson = new ResponseJson();
        try{
            resJson =hlmRelService.delete(donUserInfoEntityList);
        }catch (Exception e){
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_DEL);
        }
        return resJson;
    }

}
