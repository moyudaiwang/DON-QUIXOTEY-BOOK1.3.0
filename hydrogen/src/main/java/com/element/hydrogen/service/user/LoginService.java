package com.element.hydrogen.service.user;

import com.element.hydrogen.entity.auth.DonAccountInfoEntity;
import com.element.hydrogen.entity.common.ResponseJson;
import com.element.hydrogen.vo.user.UserInfoVO;

/**
 * @InterfaceName LoginService
 * @Description TODO
 * @Author yanyu
 * @Date 2021/1/30 17:29
 * @Version 1.0
 */
public interface LoginService {
    ResponseJson login(DonAccountInfoEntity donAccountInfoEntity);

    ResponseJson receiveCode(DonAccountInfoEntity donAccountInfoEntity);

    ResponseJson register(UserInfoVO param);
}
