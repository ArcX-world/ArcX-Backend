package avatar.util.crossServer;

import avatar.data.crossServer.CrossServerSearchProductPrizeMsg;
import avatar.data.crossServer.CrossServerUserSearchMsg;
import avatar.data.crossServer.GeneralCrossServerUserMsg;
import avatar.data.crossServer.ServerTypeUserMsg;
import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.entity.crossServer.CrossServerDomainEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.global.enumMsg.system.ServerSideTypeEnum;
import avatar.module.crossServer.*;
import avatar.module.user.info.UserDefaultHeadImgDao;
import avatar.module.user.info.UserInfoDao;
import avatar.task.crossServer.UpdateGeneralCrossServerUserMsgTask;
import avatar.util.basic.general.MediaUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.*;
import avatar.util.trigger.SchedulerSample;

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
     * 头像处理
     */
    public static String imgDeal(int serverSideType, String imgUrl) {
        return (isMetaPusherServer(serverSideType) || imgUrl.contains("default"))?
                UserCrossPlatformImgDao.getInstance().loadMsg():imgUrl;
    }

    /**
     * 是否metaPusher服务端
     */
    public static boolean isMetaPusherServer(int serverSideType){
        return serverSideType==metaPusherServer();
    }

    /**
     * 获取MP服务端类型
     */
    public static int metaPusherServer(){
        return ServerSideTypeEnum.META_PUSHER.getCode();
    }

    /**
     * 填充服务端玩家信息
     */
    public static ServerTypeUserMsg initServerTypeUserMsg(int userId, int serverSideType) {
        ServerTypeUserMsg userMsg = new ServerTypeUserMsg();
        userMsg.setSvTp(serverSideType);//服务端类型
        userMsg.setPlyId(userId);//玩家ID
        if(isArcxServer(serverSideType)){
            //本服务端
            //查询玩家信息
            UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
            userMsg.setPlyNm(userInfoEntity==null?"":userInfoEntity.getNickName());//玩家昵称
            userMsg.setPlyPct(userInfoEntity==null?"":MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));//玩家头像
        }else{
            if(isGeneralCrossServer(serverSideType)){
                //通用服务端
                GeneralCrossServerUserMsg csUserMsg = CrossServerUserMsgDao.getInstance().loadByMsg(serverSideType, userId);
                userMsg.setPlyNm(csUserMsg==null?"":csUserMsg.getNickName());//玩家昵称
                userMsg.setPlyPct(csUserMsg==null?crossDefaultImg():
                        loadCrossUserImg(csUserMsg.getServerSideType(),csUserMsg.getImgUrl()));//玩家头像
            }else{
                //其他服务端
                ProductGamingUserMsg gamingUserMsg = ServerSideUserMsgDao.getInstance().loadByMsg(userId, serverSideType);
                userMsg.setPlyNm(gamingUserMsg==null?"":gamingUserMsg.getUserName());//玩家昵称
                userMsg.setPlyPct(gamingUserMsg==null?crossDefaultImg():
                        loadCrossUserImg(gamingUserMsg.getServerSideType(), gamingUserMsg.getImgUrl()));//玩家头像
            }
        }
        return userMsg;
    }

    /**
     * 是否跨服通用服务端
     */
    public static boolean isGeneralCrossServer(int serverSideType){
        return CrossServerListDao.getInstance().loadAll().contains(serverSideType);
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
     * 获取玩家信息
     */
    private static String loadUserMsg(int userId, String domainMsg) {
        String retMsg = "";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("csUserId", userId);//玩家ID
        //获取请求url
        String httpRequest = ParamsUtil.httpRequestStr(domainMsg, paramsMap);
        if (!StrUtil.checkEmpty(httpRequest)) {
            //发送请求
            retMsg = HttpClientUtil.sendHttpGet(httpRequest);
        }
        return retMsg;
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
