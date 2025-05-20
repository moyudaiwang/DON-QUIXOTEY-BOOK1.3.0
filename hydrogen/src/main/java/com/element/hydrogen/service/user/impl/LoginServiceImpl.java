package com.element.hydrogen.service.user.impl;

import com.element.hydrogen.constant.common.ResponseJsonConstant;
import com.element.hydrogen.entity.auth.DonAccountInfoEntity;
import com.element.hydrogen.entity.common.ResponseJson;
import com.element.hydrogen.mapper.auth.DonAccountInfoMapper;
import com.element.hydrogen.mapper.user.DonUserInfoMapper;
import com.element.hydrogen.service.user.LoginService;
import com.element.hydrogen.utils.common.math.LogicUtil;
import com.element.hydrogen.vo.user.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName LoginServiceImpl
 * @Description TODO
 * @Author yanyu
 * @Date 2021/1/30 17:29
 * @Version 1.0
 */
@Service
public class LoginServiceImpl  implements LoginService {

    @Autowired
    private DonAccountInfoMapper donAccountInfoMapper;

    @Autowired
    public DonUserInfoMapper donUserInfoMapper;

    /**
     * 登录
     * @param queryEntity
     * @return
     */
    @Override
    public ResponseJson login(DonAccountInfoEntity queryEntity) {
        ResponseJson responseJson= new ResponseJson();
        responseJson.setOkay(false);
        responseJson.setCode(ResponseJsonConstant.FAIL);
        UserInfoVO userInfoVO = new UserInfoVO();
        //判断账号是用户Id/用户名
        if (queryEntity != null && LogicUtil.noEmpty(queryEntity.getAlien()) && LogicUtil.noEmpty(queryEntity.getPwd())){
            userInfoVO = donAccountInfoMapper.login(queryEntity);
        }else {
            responseJson.setMsg("账号/密码不能为空");
            return responseJson;
        }

        if(userInfoVO != null){
            if ("Y".equals(userInfoVO.getAccountStatus())){
                if (queryEntity.getPwd().equals(userInfoVO.getPwd())) {
                    userInfoVO.setPwd("");//密码置空
                    responseJson.setOkay(true);
                    responseJson.setCode(ResponseJsonConstant.SUCCESS);
                    responseJson.setObject(userInfoVO);
                    responseJson.setMsg("登录成功");
                    return responseJson;
                }else {
                    responseJson.setMsg("密码错误");
                }
            }else {
                responseJson.setMsg("该账号已锁定");
            }
        }else {
            responseJson.setMsg("账号未注册，请先注册");
        }
        return responseJson;
    }

    /**
     * 注册
     * @param param
     * @return
     */
    @Override
    public ResponseJson register(UserInfoVO param) {
        ResponseJson responseJson= new ResponseJson();
        responseJson.setOkay(false);
        responseJson.setCode(ResponseJsonConstant.FAIL);
        try {
            int num = donAccountInfoMapper.check(param);
            if (num>0){
                responseJson.setMsg("用户ID或用户名已存在，注册失败");
                return responseJson;
            }
            donAccountInfoMapper.add(param);
            donUserInfoMapper.add(param);
            responseJson.setOkay(true);
            responseJson.setCode(ResponseJsonConstant.SUCCESS);
            responseJson.setMsg("注册成功");
        } catch (Exception e){
            e.printStackTrace();
            responseJson.setMsg("注册失败");
        }
        return responseJson;
    }

    /**
     * 获取验证码
     * @param donAccountInfoEntity
     * @return
     */
    @Override
    public ResponseJson receiveCode(DonAccountInfoEntity donAccountInfoEntity) {
        //账号是否存在
        //账密是否正确
        //账号是否已登录
        //账密是否已达到失败上限
        ResponseJson responseJson = login(donAccountInfoEntity);
        if (responseJson.isOkay()){

        }
        //上一验证码是否失效
        //发送验证码
        return null;
    }


    //验证验证码
    private boolean checkCaptcha(DonAccountInfoEntity donAccountInfoEntityByResult, ResponseJson responseJson) {

        return true;
    }


    //验证账号状态
    private boolean checkAccountStatus(DonAccountInfoEntity donAccountInfoEntityByResult, ResponseJson responseJson) {
/*        //验证账号状态
        if (checkAccountStatus(entity,responseJson)){
            //验证验证码
            if (checkCaptcha(entity,responseJson)){
                //验证验证码CAPTCHA
                //todo yyf
                responseJson.setOkay(true);
                responseJson.setCode(ResponseJsonConstant.SUCCESS);
                responseJson.setMsg("登录成功");
                return responseJson;
            }else {
                //todo yyf
                //return responseJson;
            }
        }else {
            return responseJson;
        }
        if(entity != null){
            if (LogicUtil.noEmpty(entity.getPwd())) {
                if (donAccountInfoEntity.getPwd().equals(entity.getPwd())) {
                    if ("Y".equals(entity.getAccountStatus())){
                        //TODO YYF
                        if (1==1){
                            responseJson.setOkay(true);
                            responseJson.setCode(ResponseJsonConstant.SUCCESS);
                            responseJson.setMsg("登录成功");
                            return responseJson;
                        }else {
                            responseJson.setCode(ResponseJsonConstant.FAIL);
                            responseJson.setMsg("验证码未失效，稍后重试");
                        }
                    }else {
                        responseJson.setCode(ResponseJsonConstant.FAIL);
                        responseJson.setMsg("该账号已锁定");
                    }
                }else {
                    responseJson.setCode(ResponseJsonConstant.FAIL);
                    responseJson.setMsg("密码错误");
                }
            }else{
                responseJson.setCode(ResponseJsonConstant.FAIL);
                responseJson.setMsg("请输入密码");
            }
        }else {
            responseJson.setCode(ResponseJsonConstant.FAIL);
            responseJson.setMsg("账号未注册");
        }*/

        //账号是否存在
        //账密是否正确
        //账号是否已登录
        //账密是否已达到失败上限
        return true;
        //ResponseJson responseJson = check(donAccountInfoEntity);
        //todo yyf
        /*if ("Y".equals(donAccountInfoEntityByResult.getAccountStatus())){
            return true;
        }else {
            responseJson.setCode(ResponseJsonConstant.FAIL);
            responseJson.setMsg("该账号已锁定");
            //todo yyf
            return true;
        }*/
        //上一验证码是否失效
        //发送验证码
    }


    /**
     * 判断账号是用户Id/用户名
     * @param donAccountInfoEntity
     * @return
     */
    private boolean checkAlien(DonAccountInfoEntity donAccountInfoEntity) {
        String account =donAccountInfoEntity.getAlien();
        donAccountInfoEntity.setAlienVerify(false);
        if (LogicUtil.noEmpty(account)) {
            //TODO YYF 不校验手机号
            /*if(checkPhoneNo(donAccountInfoEntity)){
                donAccountInfoEntity.setAlienVerify(true);
                return true;
            }
            if(checkEmail(donAccountInfoEntity)){
                donAccountInfoEntity.setAlienVerify(true);
                return true;
            }*/
            if(checkUserId(donAccountInfoEntity)){
                donAccountInfoEntity.setAlienVerify(true);
                return true;
            }
            if(checkUserName(donAccountInfoEntity)){
                donAccountInfoEntity.setAlienVerify(true);
                return true;
            }
        }else{
            donAccountInfoEntity.setMsg("账号为空");
        }
        return false;
    }

    //用户id验证：大小写字母和数字下划线,6-20位
    private boolean checkUserId(DonAccountInfoEntity donAccountInfoEntity) {
        String regex = "^[a-zA-Z0-9_]{6,20}$";
        if (donAccountInfoEntity.getAlien().matches(regex)){
            donAccountInfoEntity.setUserId(donAccountInfoEntity.getAlien());
            return true;
        }
        return false;
    }
    //用户名验证：大小写字母和数字下划线,6-20位
    private boolean checkUserName(DonAccountInfoEntity donAccountInfoEntity) {
        String regex = "^.{6,20}$";
        if (donAccountInfoEntity.getAlien().matches(regex)){
            donAccountInfoEntity.setUserName(donAccountInfoEntity.getAlien());
            return true;
        }
        return false;
    }
    //手机验证
    private boolean checkPhoneNo(DonAccountInfoEntity donAccountInfoEntity) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (donAccountInfoEntity.getAlien().length() == 11) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(donAccountInfoEntity.getAlien());
            if (m.matches()){
                donAccountInfoEntity.setPhoneNo(donAccountInfoEntity.getAlien());
                return true;
            }
        }
        return false;
    }

    //email验证
    private boolean checkEmail(DonAccountInfoEntity donAccountInfoEntity) {
        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(donAccountInfoEntity.getAlien());
        if (m.matches()){
            donAccountInfoEntity.setEmail(donAccountInfoEntity.getAlien());
            return true;
        }
        return false;
    }

}
