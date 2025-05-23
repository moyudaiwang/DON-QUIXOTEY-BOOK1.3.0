package com.element.hydrogen.service.user.impl;

import com.element.hydrogen.constant.common.ResponseJsonConstant;
import com.element.hydrogen.entity.common.ResponseJson;
import com.element.hydrogen.entity.user.DonUserInfoEntity;
import com.element.hydrogen.mapper.user.DonUserInfoMapper;
import com.element.hydrogen.service.user.UserInfoService;
import com.element.hydrogen.utils.common.math.LogicUtil;
import com.element.hydrogen.vo.user.UserInfoVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserInfoServiceImpl
 * @Description TODO
 * @Author yanyu
 * @Date 2022-02-03 03:31:50
 * @Version 1.0
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    public DonUserInfoMapper donUserInfoMapper;

    public static ResponseJsonConstant res;

    @Override
    public ResponseJson query(DonUserInfoEntity donUserInfoEntity) {
        ResponseJson resJson = new ResponseJson();
        PageHelper.startPage(donUserInfoEntity.getPageNum(), donUserInfoEntity.getPageSize());
        List<DonUserInfoEntity> donUserInfoEntityList = donUserInfoMapper.query(donUserInfoEntity);
        PageInfo<DonUserInfoEntity> pageInfo = new PageInfo<DonUserInfoEntity>(donUserInfoEntityList);
        resJson.setCode(res.SUCCESS);
        resJson.setObject(pageInfo);
        return resJson;
    }

    @Override
    public ResponseJson insert(DonUserInfoEntity donUserInfoEntity) {
        ResponseJson resJson = new ResponseJson();
        try {
            donUserInfoMapper.insert(donUserInfoEntity);
            resJson.setCode(res.SUCCESS);
            resJson.setMsg(res.SUC_ADD);
        } catch (Exception e) {
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_ADD);
        }
        return resJson;
    }

    @Override
    public ResponseJson update(DonUserInfoEntity donUserInfoEntity) {
        ResponseJson resJson = new ResponseJson();
        try {
            donUserInfoMapper.updateByPrimaryKey(donUserInfoEntity);
            resJson.setCode(res.SUCCESS);
            resJson.setMsg(res.SUC_UPD);
        } catch (Exception e) {
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_UPD);
        }
        return resJson;
    }

    @Override
    public ResponseJson delete(List<DonUserInfoEntity> donUserInfoEntityList) {
        ResponseJson resJson = new ResponseJson();
        try {
            for (DonUserInfoEntity donUserInfoEntity : donUserInfoEntityList) {
                donUserInfoMapper.deleteByPrimaryKey(donUserInfoEntity.getDonUserInfoId());
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
     * 根据用户id查询用户信息
     *
     * @param donUserInfoEntity
     * @return
     */
    @Override
    public ResponseJson queryByUserId(DonUserInfoEntity donUserInfoEntity) {
        ResponseJson resJson = new ResponseJson();
        resJson.setOkay(false);
        resJson.setCode(res.FAIL);
        if (donUserInfoEntity != null && LogicUtil.noEmpty(donUserInfoEntity.getUserId())){
            UserInfoVO entity = donUserInfoMapper.queryByUserId(donUserInfoEntity);
            resJson.setOkay(true);
            resJson.setCode(res.SUCCESS);
            resJson.setObject(entity);
        }else {
            resJson.setMsg("账号为空");
        }
        return resJson;
    }

    /**
     * 修改用户信息
     *
     * @param donUserInfoEntity
     * @return
     */
    @Override
    public ResponseJson updateInfo(DonUserInfoEntity donUserInfoEntity) {
        ResponseJson resJson = new ResponseJson();
        try {
            donUserInfoMapper.updateInfo(donUserInfoEntity);
            resJson.setCode(res.SUCCESS);
            resJson.setMsg(res.SUC_UPD);
        } catch (Exception e) {
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_UPD);
        }
        return resJson;
    }

    /**
     * 修改头像
     *
     * @param donUserInfoEntity
     * @return
     */
    @Override
    public ResponseJson updateAvatar(DonUserInfoEntity donUserInfoEntity) {
        ResponseJson resJson = new ResponseJson();
        try {
            donUserInfoMapper.updateAvatar(donUserInfoEntity);
            resJson.setCode(res.SUCCESS);
            resJson.setMsg(res.SUC_UPD);
        } catch (Exception e) {
            resJson.setCode(res.FAIL);
            resJson.setMsg(res.FAIL_UPD);
        }
        return resJson;
    }
}
