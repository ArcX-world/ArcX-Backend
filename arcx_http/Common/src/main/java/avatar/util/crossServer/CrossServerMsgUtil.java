package avatar.util.crossServer;

import avatar.data.crossServer.ConciseServerUserMsg;
import avatar.data.crossServer.CrossServerSearchProductPrizeMsg;
import avatar.data.crossServer.CrossServerUserSearchMsg;
import avatar.data.crossServer.GeneralCrossServerUserMsg;
import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.entity.crossServer.CrossServerDomainEntity;
import avatar.entity.user.info.UserGrandPrizeMsgEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.global.basicConfig.basic.CrossServerConfigMsg;
import avatar.global.enumMsg.basic.system.ServerSideTypeEnum;
import avatar.module.crossServer.CrossServerDomainDao;
import avatar.module.crossServer.CrossServerListDao;
import avatar.module.crossServer.CrossServerUserMsgDao;
import avatar.module.crossServer.UserCrossPlatformImgDao;
import avatar.module.user.info.UserDefaultHeadImgDao;
import avatar.module.user.info.UserGrandPrizeMsgDao;
import avatar.module.user.info.UserInfoDao;
import avatar.task.crossServer.UpdateGeneralCrossServerUserMsgTask;
import avatar.util.basic.MediaUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.*;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 跨服信息工具类
 */
public class CrossServerMsgUtil {
    /**
     * 是否arcx街机服务端
     */
    public static boolean isArcxServer(int serverSideType){
        return serverSideType==0 || serverSideType==arcxServer();
    }

    /**
     * arcx服务端类型
     */
    public static int arcxServer() {
        return ServerSideTypeEnum.ARCX.getCode();
    }

    /**
     * 获取MP服务端类型
     */
    private static int metaPusherServer(){
        return ServerSideTypeEnum.META_PUSHER.getCode();
    }

    /**
     * 是否metaPusher服务端
     */
    public static boolean isMetaPusherServer(int serverSideType){
        return serverSideType==metaPusherServer();
    }

    /**
     * 是否跨服通用服务端
     */
    public static boolean isGeneralCrossServer(int serverSideType){
        return CrossServerListDao.getInstance().loadAll().contains(serverSideType);
    }

    /**
     * 填充简易服务端类型玩家信息
     */
    public static ConciseServerUserMsg initConciseServerUserMsg(ProductGamingUserMsg gamingUserMsg) {
        int userId = gamingUserMsg.getUserId();//玩家ID
        if(isArcxServer(gamingUserMsg.getServerSideType())){
            //本服务端
            return initConciseServerUserMsg(userId, gamingUserMsg.getProductId());
        }else {
            ConciseServerUserMsg msg = new ConciseServerUserMsg();
            msg.setPlyId(userId);//玩家ID
            msg.setPlyNm(gamingUserMsg.getUserName());//玩家昵称
            msg.setPlyPct(loadCrossUserImg(gamingUserMsg.getServerSideType(), gamingUserMsg.getImgUrl()));//玩家头像
            msg.setSvTp(gamingUserMsg.getServerSideType());//服务端类型
            return msg;
        }
    }

    /**
     * 填充简易玩家信息
     */
    private static ConciseServerUserMsg initConciseServerUserMsg(int userId, int productId) {
        ConciseServerUserMsg msg = new ConciseServerUserMsg();
        msg.setPlyId(userId);//玩家ID
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setPlyNm(entity==null?"":entity.getNickName());//玩家昵称
        msg.setPlyPct(entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl()));//玩家头像
        msg.setSvTp(arcxServer());
        return msg;
    }

    /**
     * 填充跨服玩家信息
     */
    public static void initGeneralUserMsg(int userId, int serverSideType, Map<String, Object> dataMap) {
        //通用服务端
        GeneralCrossServerUserMsg userMsg = CrossServerUserMsgDao.getInstance().
                loadByMsg(serverSideType, userId);
        if(userMsg!=null) {
            dataMap.put("plyNm", userMsg.getNickName());//昵称
            dataMap.put("devPzTbln", UserUtil.initProductGrandPrize(userMsg.getProductPrizeMsg()));//设备大奖信息
            dataMap.put("plyPct", loadCrossUserImg(userMsg.getServerSideType(), userMsg.getImgUrl()));
        }
    }

    /**
     * 查询跨服玩家信息
     */
    public static GeneralCrossServerUserMsg loadGeneralCrossServerUserMsg(int serverSideType, int userId) {
        GeneralCrossServerUserMsg msg = null;
        try{
            String domainMsg = loadCrossServerUserDomain(serverSideType);
            if(!StrUtil.checkEmpty(domainMsg)){
                String dataMsg = loadUserMsg(userId, domainMsg);
                if(!StrUtil.checkEmpty(dataMsg)){
                    msg = dealRetGeneralCrossServerUserMsg(serverSideType, userId, dataMsg);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            msg = null;
        }
        return msg;
    }

    /**
     * 处理返回信息
     */
    private static GeneralCrossServerUserMsg dealRetGeneralCrossServerUserMsg(int serverSideType,
            int userId, String dataMsg) {
        GeneralCrossServerUserMsg msg = new GeneralCrossServerUserMsg();
        Map<String, Object> searchMap = JsonUtil.strToMap(dataMsg);
        Map<String, Object> dataMap = (Map<String, Object>) searchMap.get("data");
        Map<String, Object> userMap = (Map<String, Object>) dataMap.get("userMsg");
        CrossServerUserSearchMsg searchMsg = initDataUserMsg(userMap);
        msg.setServerSideType(serverSideType);//服务端类型
        msg.setUserId(userId);//玩家id
        msg.setNickName(searchMsg.getNickName());//玩家昵称
        msg.setImgUrl(loadCrossUserImg(serverSideType, searchMsg.getImgUrl()));//玩家头像
        msg.setNationCode(searchMsg.getNationCode());//国家信息
        msg.setNationEn(searchMsg.getNationEn());//国家全拼
        msg.setUserLevel(searchMsg.getUserLevel());//玩家等级
        msg.setVipLevel(searchMsg.getVipLevel());//vip等级
        msg.setProductPrizeMsg(searchMsg.getProductPrizeMsg());//设备奖励信息
        msg.setCreateTime(TimeUtil.getNowTime());//创建时间
        return msg;
    }

    /**
     * 填充数据返回的玩家信息
     */
    private static CrossServerUserSearchMsg initDataUserMsg(Map<String, Object> dataMap) {
        CrossServerUserSearchMsg retMsg = new CrossServerUserSearchMsg();
        retMsg.setUserId((int) dataMap.get("userId"));//玩家id
        retMsg.setNickName((String) dataMap.get("nickName"));//玩家昵称
        retMsg.setImgUrl((String) dataMap.get("imgUrl"));//玩家头像
        retMsg.setNationCode((String) dataMap.get("nationCode"));//国家信息
        retMsg.setNationEn((String) dataMap.get("nationEn"));//国家全拼
        retMsg.setVipLevel(dataMap.containsKey("vipLevel")?((int) dataMap.get("vipLevel")):1);//vip等级
        retMsg.setUserLevel(dataMap.containsKey("userLevel")?((int) dataMap.get("userLevel")):1);//玩家等级
        retMsg.setProductPrizeMsg(initDataProductPrize(dataMap));//设备奖励信息
        return retMsg;
    }

    /**
     * 填充数据返回的设备奖励信息
     */
    private static CrossServerSearchProductPrizeMsg initDataProductPrize(Map<String, Object> dataMap) {
        Map<String, Integer> prizeMap = (Map<String, Integer>) dataMap.get("productPrizeMsg");
        CrossServerSearchProductPrizeMsg msg = new CrossServerSearchProductPrizeMsg();
        msg.setPileTower(prizeMap.get("pileTower"));//炼金塔堆塔
        msg.setAllDragon(prizeMap.get("allDragon"));//龙珠
        msg.setRoomRankFirst(prizeMap.get("roomRankFirst"));//房间排行榜第一名
        msg.setRoomRankSecond(prizeMap.get("roomRankSecond"));//房间排行榜第二名
        msg.setRoomRankThird(prizeMap.get("roomRankThird"));//房间排行榜第三名
        msg.setFreeGame(prizeMap.get("freeGame"));//免费游戏
        msg.setGem(prizeMap.get("gem"));//宝石游戏
        msg.setJackpotMinor(prizeMap.get("jackpotMinor"));//大奖转盘-minord奖池
        msg.setJackpotMajor(prizeMap.get("jackpotMajor"));//大奖转盘-major奖池
        msg.setJackpotGrand(prizeMap.get("jackpotGrand"));//大奖转盘-grand奖池
        msg.setCollectCard(prizeMap.get("collectCard"));//集卡数
        msg.setAgyptBox(prizeMap.get("agyptBox"));//金字塔
        msg.setGossip(prizeMap.get("gossip"));//八卦
        msg.setHeroBattle(prizeMap.get("heroBattle"));//三国战斗
        msg.setThunder(prizeMap.get("thunder"));//闪电
        msg.setKingkongJackpot(prizeMap.get("kingkongJackpot"));//金刚大奖转盘
        msg.setHeroJackpot(prizeMap.get("heroJackpot"));//三国大奖转盘
        msg.setWhistle(prizeMap.get("whistle"));//口哨
        return msg;
    }

    /**
     * 获取玩家信息
     */
    private static String loadUserMsg(int userId, String domainMsg) {
        String retMsg = "";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("csUserId", userId);//玩家ID
        //获取请求url
        String httpRequest = ParamsUtil.httpRequestDomain(domainMsg, paramsMap);
        if (!StrUtil.checkEmpty(httpRequest)) {
            //发送请求
            retMsg = HttpClientUtil.sendHttpGet(httpRequest);
        }
        return retMsg;
    }

    /**
     * 获取跨服玩家域名信息
     */
    private static String loadCrossServerUserDomain(int serverSideType) {
        CrossServerDomainEntity entity = CrossServerDomainDao.getInstance().loadByMsg(serverSideType);
        return entity==null?"":entity.getDomainMsg();
    }

    /**
     * 处理返回信息
     */
    public static void dealCrossServerUserMsg(GeneralCrossServerUserMsg userMsg) {
        if(userMsg!=null && userMsg.getCreateTime()< TimeUtil.getTodayLongTime()){
            SchedulerSample.delayed(5, new UpdateGeneralCrossServerUserMsgTask(userMsg));
        }
    }

    /**
     * 获取其他平台玩家信息
     */
    public static void initElseServerUserMsg(ProductGamingUserMsg msg, Map<String, Object> dataMap) {
        dataMap.put("usrNm", msg==null?"":msg.getUserName());//昵称
        dataMap.put("usrPic", msg==null?crossDefaultImg():loadCrossUserImg(msg.getServerSideType(), msg.getImgUrl()));//头像
        dataMap.put("eqmPz", UserUtil.initProductGrandPrize(null));//设备大奖信息
    }

    /**
     * 填充跨服玩家信息
     */
    public static CrossServerUserSearchMsg initUserMsg(int userId) {
        CrossServerUserSearchMsg msg = new CrossServerUserSearchMsg();
        msg.setUserId(userId);//玩家ID
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        if(entity!=null){
            //基础信息
            dealBasicUserMsg(msg, entity);
            //设备大奖信息
            dealProductPrizeMsg(userId, msg);
        }
        return msg;
    }

    /**
     * 处理基础玩家信息
     */
    private static void dealBasicUserMsg(CrossServerUserSearchMsg msg, UserInfoEntity entity) {
        int userId = entity.getId();//玩家ID
        msg.setNickName(entity.getNickName());//昵称
        msg.setImgUrl(entity.getImgUrl());//头像
        msg.setNationCode(CrossServerConfigMsg.defaultNationCode);//香港
        msg.setNationEn(CrossServerConfigMsg.defaultNationEn);//国家全拼
        msg.setUserLevel(1);//玩家等级
        msg.setVipLevel(CrossServerConfigMsg.defaultVipLevel);//vip等级
    }

    /**
     * 处理设备大奖信息
     */
    private static void dealProductPrizeMsg(int userId, CrossServerUserSearchMsg msg) {
        CrossServerSearchProductPrizeMsg prizeMsg = new CrossServerSearchProductPrizeMsg();
        //查询设备大奖信息
        UserGrandPrizeMsgEntity entity = UserGrandPrizeMsgDao.getInstance().loadByUserId(userId);
        if(entity!=null){
            prizeMsg.setPileTower(entity.getPileTower());//炼金塔堆塔
            prizeMsg.setAllDragon(entity.getDragonBall());//龙珠
            prizeMsg.setRoomRankFirst(entity.getRoomRankFirst());//房间排行榜第一名
            prizeMsg.setRoomRankSecond(entity.getRoomRankSecond());//房间排行榜第二名
            prizeMsg.setRoomRankThird(entity.getRoomRankThird());//房间排行榜第三名
            prizeMsg.setFreeGame(entity.getFreeGame());//免费游戏
            prizeMsg.setGem(entity.getGem());//宝石游戏
            prizeMsg.setJackpotGrand(entity.getPrizeWheelGrand());//大奖转盘-grand奖池
            prizeMsg.setJackpotMajor(entity.getPrizeWheelMajor());//大奖转盘-major奖池
            prizeMsg.setJackpotMinor(entity.getPrizeWheelMinor());//大奖转盘-minord奖池
            prizeMsg.setCollectCard(0);//集卡数
            prizeMsg.setAgyptBox(entity.getAgyptBox());//金字塔
            prizeMsg.setGossip(entity.getGossip());//八卦
            prizeMsg.setHeroBattle(entity.getHeroBattle());//三国战斗
            prizeMsg.setThunder(entity.getThunder());//闪电
            prizeMsg.setKingkongJackpot(entity.getJackpotKingkong());//金刚大奖转盘
            prizeMsg.setHeroJackpot(entity.getJackpotHero());//三国大奖转盘
            prizeMsg.setWhistle(entity.getWhistle());//口哨
        }
        msg.setProductPrizeMsg(prizeMsg);
    }

    /**
     * 跨服默认头像
     */
    private static String crossDefaultImg(){
        String imgUrl = UserCrossPlatformImgDao.getInstance().loadMsg();
        if(StrUtil.checkEmpty(imgUrl)) {
            List<String> list = UserDefaultHeadImgDao.getInstance().loadAll();
            imgUrl = MediaUtil.getMediaUrl(list.get(0));
        }
        return imgUrl;
    }

    /**
     * 获取跨服玩家头像
     */
    public static String loadCrossUserImg(int serverSideType, String imgUrl) {
        if(!StrUtil.checkEmpty(imgUrl)) {
            String url = isArcxServer(serverSideType) ? MediaUtil.getMediaUrl(imgUrl) :
                    MediaUtil.getCrossServerMediaUrl(serverSideType, imgUrl);
            if (StrUtil.checkEmpty(url)) {
                url = crossDefaultImg();
            }
            return url;
        }else{
            return "";
        }
    }
}
