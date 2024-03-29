package avatar.service.login;

import avatar.entity.user.info.UserInfoEntity;
import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.user.info.UserInfoDao;
import avatar.module.user.token.UserAccessTokenDao;
import avatar.module.user.token.UserTokenMsgDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.checkParams.LoginCheckParamsUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.login.LoginUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录接口实现类
 */
public class LoginDealService {
    /**
     * 注销账号
     */
    public static void logoutAccount(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = CheckParamsUtil.checkAccessToken(map);
        if (ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            //查询玩家信息
            UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
            if(userInfoEntity!=null && userInfoEntity.getStatus()!= UserStatusEnum.LOGOUT.getCode()){
                userInfoEntity.setStatus(UserStatusEnum.LOGOUT.getCode());//注销
                //更新
                boolean flag = UserInfoDao.getInstance().update(userInfoEntity);
                if(!flag){
                    status = ClientCode.FAIL.getCode();
                }else{
                    //添加操作日志
                    UserOperateLogUtil.logoutAccount(userId);
                }
            }else{
                status = ClientCode.STATUS_ERROR.getCode();//状态错误
            }
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 凭证刷新
     */
    public static void refreshToken(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = LoginCheckParamsUtil.checkRefreshToken(map);
        if(ParamsUtil.isSuccess(status)){
            String accessToken = ParamsUtil.accessToken(map);//调用凭证
            String refreshToken = map.get("refTkn").toString();//刷新凭证
            //查询玩家ID
            int userId = UserAccessTokenDao.getInstance().loadByToken(accessToken);
            //查询token信息
            UserTokenMsgEntity entity = UserTokenMsgDao.getInstance().loadByUserId(userId);
            if(!entity.getRefreshToken().equals(refreshToken)) {
                status = ClientCode.REFRESH_TOKEN_NOT_FIT.getCode();//刷新凭证不匹配
            }else{
                if (entity.getRefreshOutTime() <= TimeUtil.getNowTime()) {
                    status = ClientCode.REFRESH_TOKEN_OUT_TIME.getCode();//刷新凭证过期
                }else{
                    //更新凭证信息
                    UserTokenMsgEntity msgEntity = UserTokenMsgDao.getInstance().update(userId);
                    if(msgEntity!=null){
                        dataMap.put("aesTkn", msgEntity.getAccessToken());//调用凭证
                        dataMap.put("aesOt", msgEntity.getAccessOutTime());//调用凭证过期时间
                        dataMap.put("refTkn", msgEntity.getRefreshToken());//刷新凭证
                        dataMap.put("refOt", msgEntity.getRefreshOutTime());//刷新凭证过期时间
                    }else{
                        status = ClientCode.FAIL.getCode();//失败
                    }
                }
                //更新玩家IP
                UserUtil.updateIpMsg(userId, ParamsUtil.ip(map));
            }
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 玩家登录
     */
    public static void userLogin(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        int status = LoginCheckParamsUtil.userLogin(map);//状态
        if(ParamsUtil.isSuccess(status)) {
            //处理登录
            int userId = LoginUtil.userLogin(map);
            if (userId == 0) {
                status = ClientCode.FAIL.getCode();//失败
            }else if(userId==-1){
                status = ClientCode.CHECK_CODE_FAIL.getCode();//校验失败
            }else if(userId>0 && UserUtil.loadUserStatus(userId)!= UserStatusEnum.NORMAL.getCode()){
                status = ClientCode.ACCOUNT_DISABLED.getCode();//账号异常
            }
            if(ParamsUtil.isSuccess(status)) {
                dataMap.put("arcxUid", userId);//玩家ID
                //玩家登录返回处理
                status = UserUtil.tokenLoginRet(userId, dataMap);
            }
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 邮箱验证码
     */
    public static void emailVerifyCode(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = LoginCheckParamsUtil.emailVerifyCode(map);
        if(ParamsUtil.isSuccess(status)){
            String email = ParamsUtil.stringParmasNotNull(map, "email");//邮箱
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.EMAIL_VERIFY_CODE_LOCK + "_" + email,
                    2000);
            try {
                if (lock.lock()) {
                    status = LoginUtil.verifySendEmailCode(email);
                    if(ParamsUtil.isSuccess(status)) {
                        //发送验证码
                        LoginUtil.sendEmailVerifyCode(email);
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }
}
