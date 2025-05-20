package com.element.hydrogen.service.user;

import com.element.hydrogen.entity.common.ResponseJson;
import com.element.hydrogen.entity.user.DonUserInfoEntity;

import java.util.List;

/**
 * @InterfaceName UserInfoService
 * @Description TODO
 * @Author yanyu
 * @Date 2022-02-03 03:31:50
 * @Version 1.0
 */
public interface UserInfoService {
    ResponseJson query(DonUserInfoEntity donUserInfoEntity);

    ResponseJson insert(DonUserInfoEntity donUserInfoEntity);

    ResponseJson update(DonUserInfoEntity donUserInfoEntity);

    ResponseJson delete(List<DonUserInfoEntity> donUserInfoEntityList);

    /**
     * 根据用户id查询用户信息
     * @param donUserInfoEntity
     * @return
     */
    ResponseJson queryByUserId(DonUserInfoEntity donUserInfoEntity);

    /**
     * 修改用户信息
     * @param donUserInfoEntity
     * @return
     */
    ResponseJson updateInfo(DonUserInfoEntity donUserInfoEntity);

    /**
     * 修改头像
     * @param donUserInfoEntity
     * @return
     */
    ResponseJson updateAvatar(DonUserInfoEntity donUserInfoEntity);
}
