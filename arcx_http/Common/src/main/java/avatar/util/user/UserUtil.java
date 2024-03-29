package avatar.util.user;

import avatar.data.crossServer.CrossServerSearchProductPrizeMsg;
import avatar.data.user.info.UserGrandPrizeSearchMsg;
import avatar.entity.basic.img.ImgProductGrandPrizeMsgEntity;
import avatar.entity.user.attribute.UserAttributeMsgEntity;
import avatar.entity.user.info.UserGrandPrizeMsgEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.entity.user.info.UserMacMsgEntity;
import avatar.entity.user.info.UserRegisterIpEntity;
import avatar.entity.user.thirdpart.UserThirdPartUidMsgEntity;
import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.enumMsg.user.SexEnum;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.module.basic.img.ImgProductGrandPrizeMsgDao;
import avatar.module.user.attribute.UserAttributeMsgDao;
import avatar.module.user.info.*;
import avatar.module.user.thirdpart.UserThirdPartUidMsgDao;
import avatar.module.user.token.UserTokenMsgDao;
import avatar.util.LogUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.login.LoginUtil;
import avatar.util.system.MD5Util;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 玩家工具类
 */
public class UserUtil {
    /**
     * 获取玩家状态
     */
    public static int loadUserStatus(int userId) {
        //查询玩家信息
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        return userInfoEntity==null?-1:userInfoEntity.getStatus();
    }

    /**
     * 更新IP信息
     */
    public static void updateIpMsg(int userId, String userIp) {
        //查询玩家信息
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        if(!userInfoEntity.getIp().equals(userIp)){
            userInfoEntity.setIp(userIp);//玩家IP
            UserInfoDao.getInstance().update(userInfoEntity);
        }
    }

    /**
     * 登录token处理返回
     */
    public static int tokenLoginRet(int userId, Map<String, Object> dataMap) {
        int status = ClientCode.SUCCESS.getCode();//错误码，成功
        boolean updateFlag = updateUserTokenMsg(userId, dataMap);
        if(!updateFlag){
            status = ClientCode.FAIL.getCode();//失败
        }
        return status;
    }

    /**
     * 更新玩家凭证信息
     */
    private static boolean updateUserTokenMsg(int userId, Map<String, Object> dataMap) {
        UserTokenMsgEntity tokenMsgEntity = UserTokenMsgDao.getInstance().update(userId);
        if(tokenMsgEntity!=null){
            dataMap.put("aesTkn", tokenMsgEntity.getAccessToken());//调用凭证
            dataMap.put("aesOt", tokenMsgEntity.getAccessOutTime());//调用凭证过期时间
            dataMap.put("refTkn", tokenMsgEntity.getRefreshToken());//刷新凭证
            dataMap.put("refOt", tokenMsgEntity.getRefreshOutTime());//刷新凭证过期时间
            return true;
        }else {
            return false;
        }
    }

    /**
     * 填充玩家设备唯一ID信息
     */
    public static UserMacMsgEntity initUserMacMsgEntity(int userId, String mac, int isRegister) {
        UserMacMsgEntity entity = new UserMacMsgEntity();
        entity.setUserId(userId);//玩家ID
        entity.setMac(mac);//设备唯一ID
        entity.setIsRegister(isRegister);//是否注册
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }

    /**
     * 填充玩家实体信息
     */
    public static UserInfoEntity initUserInfoEntity(String nickName, String imgUrl, String userIp, String email,
            int loginWayType, int mobilePlatformType) {
        UserInfoEntity entity = new UserInfoEntity();
        entity.setNickName(nickName);//昵称
        entity.setImgUrl(imgUrl);//头像
        entity.setIp(userIp);//玩家IP
        entity.setLoginWay(loginWayType);//登录方式
        entity.setMobilePlatformType(mobilePlatformType);//手机平台类型
        entity.setSex(SexEnum.MALE.getCode());//性别：男
        entity.setEmail(email);//邮箱
        entity.setPassword("");//密码
        entity.setStatus(UserStatusEnum.NORMAL.getCode());//状态：正常
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime("");//更新时间
        return entity;
    }

    /**
     * 获取默认头像
     */
    public static String loadDefaultImg(){
        List<String> list = UserDefaultHeadImgDao.getInstance().loadAll();
        if(list!=null && list.size()>0){
            Collections.shuffle(list);
            return list.get(0);
        }else{
            LogUtil.getLogger().info("获取玩家默认头像的时候查询不到信息------");
            return "";
        }
    }

    /**
     * 填充注册IP实体信息
     */
    public static UserRegisterIpEntity initUserRegisterIpEntity(int userId, String userIp, Map<String, Object> paramsMap) {
        int loginWayType = ParamsUtil.loginWayType(paramsMap);//登录方式
        int mobilePlatform = ParamsUtil.loadMobilePlatform(paramsMap);//手机平台
        String versionCode = ParamsUtil.versionCode(paramsMap);//版本号
        UserRegisterIpEntity entity = new UserRegisterIpEntity();
        entity.setUserId(userId);//玩家ID
        entity.setIp(userIp);//玩家IP
        entity.setRegisterVersion(versionCode);//注册版本号
        entity.setLoginWayType(loginWayType);//登录方式
        entity.setLoginPlatform(mobilePlatform);//登录平台
        entity.setLastIp(userIp);//当前IP
        entity.setLastLoginWay(loginWayType);//当前登录方式
        entity.setLastLoginPlatform(mobilePlatform);//当前登录平台
        entity.setLastVersion(versionCode);//当前版本号
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }

    /**
     * 添加玩家设备唯一ID信息
     */
    public static void addRegisterUserMacMsg(int userId, String mac) {
        UserMacMsgEntity entity = initUserMacMsgEntity(userId, mac, YesOrNoEnum.YES.getCode());
        UserMacMsgDao.getInstance().insert(entity);
    }

    /**
     * 添加第三方唯一ID
     */
    public static void addUserThirdPartMsg(int userId, String thirdPartUid) {
        UserThirdPartUidMsgEntity entity = initUserThirdPartUidMsgEntity(userId, thirdPartUid);
        UserThirdPartUidMsgDao.getInstance().insert(entity);
    }

    /**
     * 填充玩家第三方唯一ID
     */
    private static UserThirdPartUidMsgEntity initUserThirdPartUidMsgEntity(int userId, String thirdPartUid) {
        UserThirdPartUidMsgEntity entity = new UserThirdPartUidMsgEntity();
        entity.setUserId(userId);//玩家ID
        entity.setUid(thirdPartUid);//唯一ID
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }

    /**
     * 获取玩家IP
     */
    public static String loadUserIp(int userId){
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        return entity==null?"":entity.getIp();
    }

    /**
     * 更新玩家登录信息
     */
    public static void updateUserLoginMsg(int userId, Map<String, Object> paramsMap) {
        int loginWayType = ParamsUtil.loginWayType(paramsMap);//登录方式
        int mobilePlatform = ParamsUtil.loadMobilePlatform(paramsMap);//手机平台
        String versionCode = ParamsUtil.versionCode(paramsMap);//版本号
        String userIp = ParamsUtil.ip(paramsMap);//玩家IP
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        if(entity.getMobilePlatformType()!=mobilePlatform || entity.getLoginWay()!=loginWayType){
            entity.setMobilePlatformType(mobilePlatform);//手机平台类型
            entity.setLoginWay(loginWayType);//登录方式
            UserInfoDao.getInstance().update(entity);
        }
        //更新玩家注册信息
        UserRegisterIpEntity registerIpEntity = UserRegisterIpDao.getInstance().loadDbByUserId(userId);
        if(registerIpEntity!=null){
            registerIpEntity.setLastIp(userIp);//当前登录IP
            registerIpEntity.setLastLoginWay(loginWayType);//当前登录方式
            registerIpEntity.setLastLoginPlatform(mobilePlatform);//当前登录平台
            registerIpEntity.setLastVersion(versionCode);//当前版本号
            UserRegisterIpDao.getInstance().update(registerIpEntity);
        }else{
            //添加注册信息
            UserRegisterIpDao.getInstance().insert(UserUtil.initUserRegisterIpEntity(userId, userIp, paramsMap));
        }
    }

    /**
     * 填充跨服玩家设备大奖信息
     */
    public static List<UserGrandPrizeSearchMsg> initProductGrandPrize(CrossServerSearchProductPrizeMsg productPrizeMsg) {
        List<UserGrandPrizeSearchMsg> retList = new ArrayList<>();
        if(productPrizeMsg!=null) {
            //查询设备大奖图片信息
            List<ImgProductGrandPrizeMsgEntity> imgList = ImgProductGrandPrizeMsgDao.getInstance().loadAll();
            if (imgList.size() > 0) {
                imgList.forEach(imgEntity -> {
                    int prizeNum = dealCrossPrizeNum(productPrizeMsg, imgEntity.getEnConciseName());//中奖数量
                    if (prizeNum != -1) {
                        retList.add(initUserGrandPrizeSearchMsg(prizeNum, imgEntity));
                    }
                });
            }
        }
        return retList;
    }

    /**
     * 填充玩家设备大奖查询信息
     */
    private static UserGrandPrizeSearchMsg initUserGrandPrizeSearchMsg(int prizeNum, ImgProductGrandPrizeMsgEntity imgEntity) {
        UserGrandPrizeSearchMsg msg = new UserGrandPrizeSearchMsg();
        msg.setPzPic(MediaUtil.getMediaUrl(imgEntity.getImgUrl()));//设备大奖图片
        msg.setPzQt(prizeNum);//设备大奖数量
        return msg;
    }

    /**
     * 处理中奖数量
     */
    private static int dealCrossPrizeNum(CrossServerSearchProductPrizeMsg msg, String name) {
        int retNum = -1;
        switch (name) {
            case "gossip":
                //八卦
                retNum = msg==null?0:msg.getGossip();
                break;
            case "gem":
                //宝石
                retNum = msg==null?0:msg.getGem();
                break;
            case "pileTower":
                //炼金塔堆塔
                retNum = msg==null?0:msg.getPileTower();
                break;
            case "agyptBox":
                //金字塔
                retNum = msg==null?0:msg.getAgyptBox();
                break;
            case "allDragon":
                //龙珠
                retNum = msg==null?0:msg.getAllDragon();
                break;
            case "heroJackpot":
                //三国大奖转盘
                retNum = msg==null?0:msg.getHeroJackpot();
                break;
            case "freeGame":
                //免费游戏
                retNum = msg==null?0:msg.getFreeGame();
                break;
            case "jackpotMinor":
                //大奖转盘-minor奖池
                retNum = msg==null?0:msg.getJackpotMinor();
                break;
            case "jackpotMajor":
                //大奖转盘-major奖池
                retNum = msg==null?0:msg.getJackpotMajor();
                break;
            case "jackpotGrand":
                //大奖转盘-grand奖池
                retNum = msg==null?0:msg.getJackpotGrand();
                break;
            case "thunder":
                //闪电
                retNum = msg==null?0:msg.getThunder();
                break;
            case "collectCard":
                //碎片
                retNum = msg==null?0:msg.getCollectCard();
                break;
            case "whistle":
                //口哨
                retNum = msg==null?0:msg.getWhistle();
                break;
            case "kingkongJackpot":
                //金刚大奖转盘
                retNum = msg==null?0:msg.getKingkongJackpot();
                break;
        }
        return retNum;
    }

    /**
     * 填充玩家设备获奖信息
     */
    public static UserGrandPrizeMsgEntity initUserGrandPrizeMsgEntity(int userId) {
        UserGrandPrizeMsgEntity entity = new UserGrandPrizeMsgEntity();
        entity.setUserId(userId);//玩家ID
        entity.setPileTower(0);//炼金塔堆塔
        entity.setDragonBall(0);//龙珠
        entity.setRoomRankFirst(0);//房间排行榜第一名
        entity.setRoomRankSecond(0);//房间排行榜第二名
        entity.setRoomRankThird(0);//房间排行榜第三名
        entity.setFreeGame(0);//免费游戏
        entity.setGem(0);//宝石游戏
        entity.setPrizeWheelGrand(0);//大奖转盘-grand奖池
        entity.setPrizeWheelMajor(0);//大奖转盘-major奖池
        entity.setPrizeWheelMinor(0);//大奖转盘-grand奖池
        entity.setAgyptBox(0);//埃及开箱子
        entity.setGossip(0);//八卦
        entity.setHeroBattle(0);//英雄战斗
        entity.setThunder(0);//闪电
        entity.setJackpotKingkong(0);//金刚大奖转盘
        entity.setJackpotHero(0);//三国大奖转盘
        entity.setWhistle(0);//口哨
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        return entity;
    }

    /**
     * 添加注册送币
     */
    public static void addRegisterWelfare(int userId) {
        int presentCoin = LoginUtil.registerPresentCoin();//注册送币
        if(presentCoin>0){
            //添加到玩家账户
            boolean flag = UserBalanceUtil.addUserBalance(userId, CommodityUtil.gold(), presentCoin);
            if(flag) {
                //添加日志
                UserCostLogUtil.registerWelfare(userId, presentCoin);
            }else{
                LogUtil.getLogger().error("添加玩家{}注册赠送的{}游戏币失败------", userId, presentCoin);
            }
        }
        //TODO 暂时送10000USDT
        UserUsdtUtil.addUsdtBalance(userId, 10000);
    }

    /**
     * 是否玩家存在
     */
    public static boolean existUser(int userId) {
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        return entity!=null && entity.getStatus()== UserStatusEnum.NORMAL.getCode();
    }

    /**
     * 更新玩家信息
     */
    public static void updateUserInfo(int userId, String nickName, int sex) {
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        entity.setNickName(nickName);//玩家昵称
        entity.setSex(sex);//性别
        UserInfoDao.getInstance().update(entity);
    }

    /**
     * 更新玩家密码
     */
    public static void updateUserPassword(int userId, String email, String password) {
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        try {
            boolean emailBindFlag = StrUtil.checkEmpty(entity.getEmail());//是否绑定邮箱
            entity.setEmail(email);//邮箱
            entity.setPassword(MD5Util.MD5(password));//密码
            boolean flag = UserInfoDao.getInstance().update(entity);
            if(flag && emailBindFlag){
                //设置邮箱玩家
                EmailUserDao.getInstance().setCache(email, userId);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
    }

    /**
     * 填充玩家信息
     */
    public static void loadUserInfo(int userId, Map<String, Object> dataMap) {
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        dataMap.put("plyNm", entity.getNickName());//玩家昵称
        dataMap.put("plyPct", MediaUtil.getMediaUrl(entity.getImgUrl()));//玩家头像
        dataMap.put("email", entity.getEmail());//邮箱
        dataMap.put("sex", entity.getSex());//性别
        //查询玩家属性信息
        UserAttributeMsgEntity userAttribute = UserAttributeMsgDao.getInstance().loadMsg(userId);
        if(userAttribute!=null) {
            dataMap.put("atbTbln", UserAttributeUtil.userAttributeMsg(userAttribute));//玩家属性
        }else{
            LogUtil.getLogger().error("查询玩家{}信息的时候，填充玩家的属性信息失败------", userId);
        }
    }

    /**
     * 更新玩家邮箱信息
     */
    public static void updateUserEmail(int userId, String email) {
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        if(email!=null){
            entity.setEmail(email);
            boolean flag = UserInfoDao.getInstance().update(entity);
            if(flag){
                EmailUserDao.getInstance().setCache(email, userId);
            }
        }
    }

    /**
     * 获取玩家邮箱
     */
    public static String loadUserEmail(int userId) {
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        return entity==null?"":entity.getEmail();
    }
}
