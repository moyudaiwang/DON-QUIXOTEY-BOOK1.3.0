package com.element.hydrogen.mapper.user;

import com.element.hydrogen.entity.user.DonUserInfoEntity;
import com.element.hydrogen.vo.user.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DonUserInfoMapper {
    int deleteByPrimaryKey(String donUserInfoId);

    int insert(DonUserInfoEntity record);

    int insertSelective(DonUserInfoEntity record);

    DonUserInfoEntity selectByPrimaryKey(String donUserInfoId);

    int updateByPrimaryKeySelective(DonUserInfoEntity record);

    int updateByPrimaryKey(DonUserInfoEntity record);

    List<DonUserInfoEntity> query(DonUserInfoEntity donUserInfoEntity);

    UserInfoVO queryByUserId(DonUserInfoEntity donUserInfoEntity);

    void updateInfo(DonUserInfoEntity donUserInfoEntity);

    void updateAvatar(DonUserInfoEntity donUserInfoEntity);

    /**
     * 注册：写入表数据
     * @param param
     */
    void add(UserInfoVO param);
}