package avatar.util.login;

import avatar.data.user.info.EmailVerifyCodeMsg;
import avatar.entity.basic.systemConfig.SystemConfigEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.global.basicConfig.basic.CheckConfigMsg;
import avatar.global.basicConfig.basic.ConfigMsg;
import avatar.global.basicConfig.basic.UserConfigMsg;
import avatar.global.enumMsg.basic.system.LoginWayTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.linkMsg.HttpCmdName;
import avatar.global.lockMsg.LockMsg;
import avatar.module.basic.systemConfig.SystemConfigDao;
import avatar.module.user.info.*;
import avatar.module.user.thirdpart.UserThirdPartUidMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.login.LoginMsgUpdateTask;
import avatar.task.login.LoginRegisterDealTask;
import avatar.util.LogUtil;
import avatar.util.basic.CheckUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.*;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserUtil;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 登录工具类
 */
public class LoginUtil {
    /**
     * 玩家登录
     */
    public static int userLogin(Map<String, Object> map) {
        int loginWayType = ParamsUtil.loginWayType(map);//登录方式
        String mac = ParamsUtil.macId(map);//设备唯一ID
        String code = ParamsUtil.stringParmas(map, "iosTkn");//授权code
        String userIp = ParamsUtil.ip(map);//玩家IP
        String email = ParamsUtil.stringParmas(map, "email");//邮箱
        String thirdPartUid = "";//第三方唯一ID
        int mobilePlatformType = ParamsUtil.loadMobilePlatform(map);//手机平台类型
        String lockMsg = mac+email;//设备唯一ID
        int userId = 0;//玩家ID
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.LOGIN_LOCK+"_"+lockMsg,
                500);
        try {
            if (lock.lock()) {
                boolean isRegister = false;//是否注册
                if (loginWayType == LoginWayTypeEnum.APPLE.getCode()) {
                    //苹果登录（先用设备ID登录，再走第三方）
                    thirdPartUid = loadAppleUid(code);
                } else if (loginWayType == LoginWayTypeEnum.EMAIL.getCode()) {
                    //邮箱登录
                    userId = EmailUserDao.getInstance().loadMsg(email);
                } else if(loginWayType==LoginWayTypeEnum.TOURIST.getCode()){
                    //游客登录
                    userId = UserMacMsgDao.getInstance().loadByMacId(mac);
                }
                if (userId == 0) {
                    //根据第三方类型获取玩家ID
                    if (!StrUtil.checkEmpty(thirdPartUid)) {
                        userId = UserThirdPartUidMsgDao.getInstance().loadByUid(thirdPartUid);
                    } else if (!StrUtil.checkEmpty(code) && StrUtil.checkEmpty(thirdPartUid)) {
                        userId = -1;
                    }
                }
                if (userId != -1) {
                    //走注册流程
                    if (userId == 0 && (loginWayType == LoginWayTypeEnum.TOURIST.getCode() || !StrUtil.checkEmpty(thirdPartUid)
                            || loginWayType == LoginWayTypeEnum.EMAIL.getCode())) {
                        String nickName = loginNickName(loadNickNamePrefix(loginWayType));//昵称
                        userId = register(nickName, userIp, email, loginWayType, mobilePlatformType);
                        isRegister = true;
                    }
                    //后续流程处理
                    if (isRegister && userId > 0) {
                        SchedulerSample.delayed(10, new LoginRegisterDealTask(userId, userIp, mac,
                                thirdPartUid, map));
                    } else if (!isRegister && userId > 0) {
                        //登录后续处理
                        SchedulerSample.delayed(10, new LoginMsgUpdateTask(userId, thirdPartUid, userIp, map));
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        return userId;
    }

    /**
     * 获取苹果的第三方唯一ID
     */
    private static String loadAppleUid(String code) {
        String sub = "";
        //需要访问外网
        String url = CheckConfigMsg.checkIp + HttpCmdName.APPLE_USER_INFO_LOAD+"?identityToken="+code;
        //发起请求
        String ret =  HttpClientUtil.sendHttpGet(url);
        if(!StrUtil.checkEmpty(ret)) {
            Map<String, Object> retMap = JsonUtil.strToMap(ret);
            if (retMap.size() > 0 && retMap.containsKey("data")) {
                String tokenStr = retMap.get("data").toString();
                if (!StrUtil.checkEmpty(tokenStr)) {
                    Map<String, Object> tokenMap = JsonUtil.strToMap(tokenStr);
                    if(tokenMap.size()>0 && tokenMap.containsKey("sub")){
                        sub = tokenMap.get("sub").toString();//苹果用户信息sub
                    }else{
                        LogUtil.getLogger().info("苹果登录{}获取苹果用户信息失败----------", code);
                    }
                }
            }
        }
        return sub;
    }

    /**
     * 添加绑定mac
     */
    private static void addBindMac(int userId, String mac) {
        int bindUserId = UserMacMsgDao.getInstance().loadByMacId(mac);
        if(userId!=bindUserId && !MacUserListMsgDao.getInstance().loadByMacId(mac).contains(userId)){
            UserMacMsgDao.getInstance().insert(UserUtil.initUserMacMsgEntity(userId, mac, YesOrNoEnum.NO.getCode()));
        }
    }

    /**
     * 获取昵称前缀
     */
    private static String loadNickNamePrefix(int loginWayType) {
        if(loginWayType==LoginWayTypeEnum.APPLE.getCode()){
            //苹果
            return UserConfigMsg.appleRegisterPrefix;
        }else if(loginWayType==LoginWayTypeEnum.EMAIL.getCode()){
            //邮箱
            return UserConfigMsg.emailRegisterPrefix;
        }else{
            //游客
            return UserConfigMsg.touristRegisterPrefix;
        }
    }

    /**
     * 获取注册的用户昵称
     */
    private static String loginNickName(String prefix) {
        StringBuilder str = new StringBuilder(prefix);
        String ranStr = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,0,1,2,3,4,5,6,7,8,9";
        List<String> ranList = StrUtil.strToStrList(ranStr);
        for(int i=0;i<5;i++){
            str.append(ranList.get((new Random()).nextInt(ranList.size())));
        }
        //查询昵称是否存在
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadDbByNickName(str.toString());
        if(userInfoEntity!=null){
            loginNickName(prefix);
        }
        return str.toString();
    }

    /**
     * 添加注册玩家信息
     */
    private static int register(String nickName, String userIp, String email, int loginWayType, int mobilePlatformType) {
        String imgUrl = UserUtil.loadDefaultImg();
        //注册玩家信息
        return UserInfoDao.getInstance().insert(UserUtil.
                initUserInfoEntity(nickName, imgUrl, userIp, email, loginWayType, mobilePlatformType));
    }

    /**
     * 注册送币
     */
    public static int registerPresentCoin() {
        //查询配置信息
        SystemConfigEntity entity = SystemConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getRegisterCoin();
    }

    /**
     * 发送验证码
     */
    public static void sendEmailVerifyCode(String email) {
        String verifyCode = loadVerifyCode();
        //查询信息
        EmailVerifyCodeMsg msg = EmailVerifyCodeDao.getInstance().loadMsg(email);
        msg.setVerifyCode(verifyCode);//验证码
        msg.setSendTime(TimeUtil.getNowTime());//发送时间
        EmailVerifyCodeDao.getInstance().setCache(email, msg);
    }

    /**
     * 获取验证码
     */
    private static String loadVerifyCode() {
        if(CheckUtil.isTestEnv()){
            return "1111";
        }else {
            int num = (new Random()).nextInt(10000);
            if (num < 1000) {
                int kilobitNum = (new Random()).nextInt(10);
                if (kilobitNum == 0) {
                    kilobitNum = 1;
                }
                num += kilobitNum * 1000;
            }
            return num + "";
        }
    }

    /**
     * 填充邮箱验证码信息
     */
    public static EmailVerifyCodeMsg initEmailVerifyCodeMsg(String email) {
        EmailVerifyCodeMsg msg = new EmailVerifyCodeMsg();
        msg.setEmail(email);//邮箱
        msg.setVerifyCode("");//验证码
        msg.setSendTime(0);//发送时间
        return msg;
    }

    /**
     * 校验发送邮箱验证码
     */
    public static int verifySendEmailCode(String email) {
        int status = ClientCode.SUCCESS.getCode();
        //查询信息
        EmailVerifyCodeMsg msg = EmailVerifyCodeDao.getInstance().loadMsg(email);
        if(msg.getSendTime()>0){
            if((TimeUtil.getNowTime()-msg.getSendTime())<ConfigMsg.verifyCodeSendTime){
                status = ClientCode.FREQUENTLY_SEND.getCode();//频繁发送
            }
        }
        return status;
    }

    /**
     * 校验验证码失效
     */
    public static int verifyEmailCodeOutTime(String email, String verifyCode) {
        int status = ClientCode.VERIFY_CODE_LOSE_EFFICACY.getCode();//验证码失效
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.EMAIL_VERIFY_CODE_LOCK + "_" + email,
                2000);
        try {
            if (lock.lock()) {
                //查询信息
                EmailVerifyCodeMsg msg = EmailVerifyCodeDao.getInstance().loadMsg(email);
                if (!StrUtil.checkEmpty(msg.getVerifyCode()) && msg.getSendTime() > 0) {
                    if (msg.getVerifyCode().equals(verifyCode)) {
                        if ((TimeUtil.getNowTime() - msg.getSendTime()) <= ConfigMsg.verifyCodeOutTime) {
                            status = ClientCode.SUCCESS.getCode();//成功
                            //初始化邮箱验证码信息
                            EmailVerifyCodeDao.getInstance().setCache(email,initEmailVerifyCodeMsg(email));
                        }
                    } else {
                        status = ClientCode.VERIFY_CODE_ERROR.getCode();//验证码错误
                    }
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
        return status;
    }

    /**
     * 邮箱密码登录
     */
    public static int checkEmailUser(String email, String pwd) {
        int status = ClientCode.PASSWORD_ERROR.getCode();//密码错误
        int userId = EmailUserDao.getInstance().loadMsg(email);
        if(userId>0){
            try {
                //查询玩家信息
                UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
                if (entity != null && !StrUtil.checkEmpty(entity.getPassword()) &&
                        entity.getPassword().equals(MD5Util.MD5(pwd))){
                    status = ClientCode.SUCCESS.getCode();//成功
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }
}
