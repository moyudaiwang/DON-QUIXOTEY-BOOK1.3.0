package com.element.hydrogen.mapper.auth;

import com.element.hydrogen.entity.auth.DonAccountInfoEntity;
import com.element.hydrogen.vo.user.UserInfoVO;

import java.util.List;

/**
 * @author yanyu
 */
public interface DonAccountInfoMapper {
    int deleteByPrimaryKey(String donAccountInfoId);

    int insert(DonAccountInfoEntity record);

    int insertSelective(DonAccountInfoEntity record);

    DonAccountInfoEntity selectByPrimaryKey(String donAccountInfoId);

    int updateByPrimaryKeySelective(DonAccountInfoEntity record);

    int updateByPrimaryKey(DonAccountInfoEntity record);

    List<DonAccountInfoEntity> query(DonAccountInfoEntity donAccountInfoEntity);

    /***
     * 校验用户ID、用户名，手机号，邮箱是否存在
     * @param param
     * @return
     */
    int check(UserInfoVO param);

    /**
     * 根据用户id查询用户信息
     * @param donAccountInfoEntity
     * @return
     */
    DonAccountInfoEntity queryByUserId(DonAccountInfoEntity donAccountInfoEntity);
    /**
     * 更新密码
     * @param donAccountInfoEntity
     */
    void updatePwd(DonAccountInfoEntity donAccountInfoEntity);

    /**
     * 登录查询
     * @param queryEntity
     * @return
     */
    UserInfoVO login(DonAccountInfoEntity queryEntity);

    /**
     * 注册：写入表数据
     * @param param
     */
    void add(UserInfoVO param);
}