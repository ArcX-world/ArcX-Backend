package avatar.util.user;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.data.user.info.ConciseUserMsg;
import avatar.entity.user.info.UserGrandPrizeMsgEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.code.basicConfig.ConfigMsg;
import avatar.global.enumMsg.basic.system.MobilePlatformTypeEnum;
import avatar.global.enumMsg.product.award.ProductAwardTypeEnum;
import avatar.global.enumMsg.product.info.ProductSecondTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.module.user.info.UserGrandPrizeMsgDao;
import avatar.module.user.info.UserInfoDao;
import avatar.module.user.token.UserAccessTokenDao;
import avatar.module.user.token.UserTokenMsgDao;
import avatar.util.LogUtil;
import avatar.util.basic.general.MediaUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

/**
 * 玩家工具类
 */
public class UserUtil {
    /**
     * 获取通行证
     */
    public static String loadAccessToken(int userId){
        //查询信息
        UserTokenMsgEntity entity = UserTokenMsgDao.getInstance().loadByUserId(userId);
        return entity==null?"":entity.getAccessToken();
    }

    /**
     * 根据accessToken获取玩家ID
     */
    public static int loadUserIdByToken(String accessToken) {
        int userId = 0;
        if(!StrUtil.checkEmpty(accessToken)){
            //查询信息
            userId = UserAccessTokenDao.getInstance().loadByToken(accessToken);
        }
        return userId;
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
     * 检测玩家通行证
     */
    public static int checkAccessToken(String accessToken) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(!StrUtil.checkEmpty(accessToken)){
            //查询对应玩家ID
            int userId = UserAccessTokenDao.getInstance().loadByToken(accessToken);
            if(userId==0){
                status = ClientCode.ACCESS_TOKEN_ERROR.getCode();//调用凭证错误
            }else if(userId>0 && UserUtil.loadUserStatus(userId)!= UserStatusEnum.NORMAL.getCode()){
                status = ClientCode.ACCOUNT_DISABLED.getCode();//账号异常
            }else{
                //查询调用凭证过期时间
                UserTokenMsgEntity tokenMsgEntity = UserTokenMsgDao.getInstance().loadByUserId(userId);
                if(tokenMsgEntity==null || tokenMsgEntity.getAccessOutTime()<=TimeUtil.getNowTime()){
                    status = ClientCode.ACCESS_TOKEN_OUT_TIME.getCode();//调用凭证过期
                }
            }
        }
        return status;
    }

    /**
     * 获取玩家状态
     */
    private static int loadUserStatus(int userId) {
        //查询玩家信息
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        return userInfoEntity==null?-1:userInfoEntity.getStatus();
    }

    /**
     * 是否游客token
     */
    public static boolean isTourist(String accessToken){
        return accessToken.equals(ConfigMsg.touristAccessToken);
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
     * 填充默认的游戏玩家信息
     */
    public static ProductGamingUserMsg initProductGamingUserMsg(int userId, int serverSideType) {
        ProductGamingUserMsg msg = new ProductGamingUserMsg();
        msg.setServerSideType(serverSideType);
        msg.setProductId(0);//设备ID
        msg.setUserId(userId);//玩家ID
        msg.setUserName("");//玩家昵称
        msg.setImgUrl("");//玩家头像
        return msg;
    }

    /**
     * 填充简易玩家信息
     */
    public static ConciseUserMsg initConciseUserMsg(int userId) {
        ConciseUserMsg msg = new ConciseUserMsg();
        msg.setPlyId(userId);//玩家ID
        //正常玩家
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setPlyNm(entity==null?"":entity.getNickName());//玩家昵称
        msg.setPlyPct(entity==null?"": MediaUtil.getMediaUrl(entity.getImgUrl()));//玩家头像
        return msg;
    }

    /**
     * 更新玩家设备大奖信息
     */
    public static void updateUserGrandPrizeMsg(int userId, int awardType, int productId) {
        //查询玩家设备大奖信息
        UserGrandPrizeMsgEntity entity = UserGrandPrizeMsgDao.getInstance().loadByUserId(userId);
        if(entity!=null){
            if(awardType== ProductAwardTypeEnum.DRAGON_BALL.getCode()){
                //七龙珠
                entity.setDragonBall(entity.getDragonBall()+1);
            }else if(awardType==ProductAwardTypeEnum.FREE_GAME.getCode()){
                //免费游戏
                entity.setFreeGame(entity.getFreeGame()+1);
            }else if(awardType==ProductAwardTypeEnum.GRM.getCode()){
                //宝石游戏
                entity.setGem(entity.getGem()+1);
            }else if(awardType==ProductAwardTypeEnum.PRIZE_WHEEL_GRAND.getCode()){
                //大奖转盘-grand奖池
                entity.setPrizeWheelGrand(entity.getPrizeWheelGrand()+1);
            }else if(awardType==ProductAwardTypeEnum.PRIZE_WHEEL_MAJOR.getCode()){
                //大奖转盘-major奖池
                entity.setPrizeWheelMajor(entity.getPrizeWheelMajor()+1);
            }else if(awardType==ProductAwardTypeEnum.PRIZE_WHEEL_MINOR.getCode()){
                //大奖转盘-minor奖池
                entity.setPrizeWheelMinor(entity.getPrizeWheelMinor()+1);
            }else if(awardType==ProductAwardTypeEnum.AGYPT_OPEN_BOX.getCode()){
                //埃及开箱子
                entity.setAgyptBox(entity.getAgyptBox()+1);
            }else if(awardType==ProductAwardTypeEnum.GOSSIP.getCode()){
                //八卦
                entity.setGossip(entity.getGossip()+1);
            }else if(awardType==ProductAwardTypeEnum.HERO_BATTLE.getCode()){
                //英雄战斗
                entity.setHeroBattle(entity.getHeroBattle()+1);
            }else if(awardType==ProductAwardTypeEnum.THUNDER.getCode()){
                //闪电
                entity.setThunder(entity.getThunder()+1);
            }else if(awardType==ProductAwardTypeEnum.WHISTLE.getCode()){
                //口哨
                entity.setWhistle(entity.getWhistle()+1);
            }
            //大奖转盘
            if(isJackpotAward(awardType)){
                int secondType = ProductUtil.loadSecondType(productId);
                if(secondType== ProductSecondTypeEnum.KING_KONG.getCode()){
                    //金刚
                    entity.setJackpotKingkong(entity.getJackpotKingkong()+1);
                }
            }
            UserGrandPrizeMsgDao.getInstance().update(entity);
        }else{
            LogUtil.getLogger().info("玩家{}获取设备大奖信息失败-----------", userId);
        }
    }

    /**
     * 是否大奖转盘奖励
     */
    private static boolean isJackpotAward(int awardType) {
        return awardType==ProductAwardTypeEnum.PRIZE_WHEEL_MINOR.getCode() ||
                awardType==ProductAwardTypeEnum.PRIZE_WHEEL_MAJOR.getCode() ||
                awardType==ProductAwardTypeEnum.PRIZE_WHEEL_GRAND.getCode();
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
     * 是否玩家封禁
     */
    public static boolean isAccountForbid(int userId) {
        //玩家账号异常
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        if(entity!=null && entity.getStatus()!=UserStatusEnum.NORMAL.getCode()){
            return true;
        }
        return false;
    }

    /**
     * 是否苹果玩家
     */
    public static boolean isIosUser(int userId) {
        //查询玩家信息
        //登录平台
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        return userInfoEntity==null || userInfoEntity.getMobilePlatformType()==MobilePlatformTypeEnum.APPLE.getCode();
    }
}
