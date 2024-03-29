package avatar.util.product;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.general.ResponseGeneralMsg;
import avatar.data.product.innoMsg.*;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductSecondTypeEnum;
import avatar.global.enumMsg.product.innoMsg.InnoProductOperateTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.basic.errrorCode.InnoClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.user.info.UserInfoDao;
import avatar.util.LogUtil;
import avatar.util.basic.general.MediaUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.system.TimeUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 自研设备参数信息工具类
 */
public class InnoParamsUtil {
    /**
     * 填充推送自研设备的开始游戏参数
     */
    public static InnoStartGameMsg initInnoStartGameMsg(int productId, String alias, int userId) {
        InnoStartGameMsg msg = new InnoStartGameMsg();
        msg.setProductId(productId);//设备ID
        msg.setAlias(alias);//设备号
        msg.setUserId(userId);//玩家ID
        //查询玩家信息
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setUserName(userInfoEntity==null?"":userInfoEntity.getNickName());//玩家昵称
        msg.setImgUrl(userInfoEntity==null?"": MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));//玩家头像
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());//服务端类型
        msg.setRequestTime(TimeUtil.getNowTime());//请求时间
        return msg;
    }

    /**
     * 填充开始游戏信息
     */
    public static Map<Object, Object> initStartGameMsg(InnoStartGameMsg startGameMsg) {
        Map<Object, Object> paramsMap = new HashMap<>();//参数信息
        paramsMap.put("alias", startGameMsg.getAlias());//设备号
        int userId = startGameMsg.getUserId();//玩家ID
        paramsMap.put("userId", userId);//玩家ID
        paramsMap.put("userName", startGameMsg.getUserName());//玩家昵称
        paramsMap.put("imgUrl", startGameMsg.getImgUrl());//玩家头像
        paramsMap.put("serverSideType", startGameMsg.getServerSideType());//服务端类型
        paramsMap.put("coinLevelWeight", InnoProductUtil.userCoinWeight(userId, startGameMsg.getProductId()));//游戏币权重
        paramsMap.put("requestTime", startGameMsg.getRequestTime());//请求时间
        return paramsMap;
    }

    /**
     * 将自研设备的设备操作类型转换成对应的设备操作类型
     */
    public static int loadProductOperateType(int operateType) {
        int retOperateType = -1;
        if(operateType==InnoProductOperateTypeEnum.PUSH_COIN.getCode()){
            //投币
            retOperateType = ProductOperationEnum.PUSH_COIN.getCode();
        }else if(operateType==InnoProductOperateTypeEnum.WIPER.getCode()){
            //雨刷
            retOperateType = ProductOperationEnum.ROCK.getCode();
        }else if(operateType==InnoProductOperateTypeEnum.AUTO_PUSH_COIN.getCode()){
            //自动投币
            retOperateType = ProductOperationEnum.AUTO_SHOOT.getCode();
        }else if(operateType==InnoProductOperateTypeEnum.CANCEL_AUTO_PUSH_COIN.getCode()){
            //取消自动投币
            retOperateType = ProductOperationEnum.CANCEL_AUTO_SHOOT.getCode();
        }
        return retOperateType;
    }

    /**
     * 填充自研设备设备操作信息
     */
    public static InnoProductOperateMsg initInnoProductOperateMsg(int productId, String alias, int userId,
            long onProductTime, int operateType, boolean innoFreeLink) {
        InnoProductOperateMsg msg = new InnoProductOperateMsg();
        msg.setProductId(productId);//设备ID
        msg.setAlias(alias);//设备号
        msg.setUserId(userId);//玩家ID
        //查询玩家信息
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setUserName(userInfoEntity==null?"":userInfoEntity.getNickName());//玩家昵称
        msg.setImgUrl(userInfoEntity==null?"": MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));//玩家头像
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());//服务端类型
        msg.setRequestTime(TimeUtil.getNowTime());//请求时间
        msg.setOnProductTime(onProductTime);//上机时间
        msg.setInnoProductOperateType(operateType);//自研设备操作类型
        msg.setProductCost(ProductUtil.productCost(productId));//设备币值
        int secondType = ProductUtil.loadSecondType(productId);//设备二级分类
        msg.setAgyptOpenBox((innoFreeLink && secondType== ProductSecondTypeEnum.AGYPT.getCode())
                ? YesOrNoEnum.YES.getCode(): YesOrNoEnum.NO.getCode());//埃及开箱子
        msg.setClownCircusFerrule((innoFreeLink && secondType== ProductSecondTypeEnum.CLOWN_CIRCUS.getCode())
                ? YesOrNoEnum.YES.getCode(): YesOrNoEnum.NO.getCode());//小丑动物套圈
        msg.setPirateCannon((innoFreeLink && secondType== ProductSecondTypeEnum.PIRATE.getCode())
                ? YesOrNoEnum.YES.getCode(): YesOrNoEnum.NO.getCode());//海盗开炮
        msg.setCoinLevelWeight(InnoProductUtil.userCoinWeight(userId, productId));//权重等级
        msg.setPayFlag(InnoNaPayUtil.isPay(userId)? YesOrNoEnum.YES.getCode(): YesOrNoEnum.NO.getCode());//是否付费
        return msg;
    }

    /**
     * 填充推送自研设备的退出设备参数
     */
    public static InnoEndGameMsg initInnoEndGameMsg(int productId, String alias, int userId) {
        InnoEndGameMsg msg = new InnoEndGameMsg();
        msg.setProductId(productId);//设备ID
        msg.setAlias(alias);//设备号
        msg.setUserId(userId);//玩家ID
        //查询玩家信息
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setUserName(userInfoEntity==null?"":userInfoEntity.getNickName());//玩家昵称
        msg.setImgUrl(userInfoEntity==null?"": MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));//玩家头像
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());//服务端类型
        msg.setRequestTime(TimeUtil.getNowTime());//请求时间
        msg.setProductMulti(ProductGamingUtil.loadMultiLevel(productId));
        return msg;
    }

    /**
     * 填充结束游戏信息
     */
    public static Map<Object, Object> initEndGameMsg(InnoEndGameMsg endGameMsg) {
        Map<Object, Object> paramsMap = new HashMap<>();//参数信息
        paramsMap.put("alias", endGameMsg.getAlias());//设备号
        paramsMap.put("userId", endGameMsg.getUserId());//玩家ID
        paramsMap.put("userName", endGameMsg.getUserName());//玩家昵称
        paramsMap.put("imgUrl", endGameMsg.getImgUrl());//玩家头像
        paramsMap.put("serverSideType", endGameMsg.getServerSideType());//服务端类型
        paramsMap.put("requestTime", endGameMsg.getRequestTime());//请求时间
        paramsMap.put("productMulti", endGameMsg.getProductMulti());//设备倍率
        return paramsMap;
    }

    /**
     * 填充设备操作信息
     */
    public static Map<Object, Object> initProductOperateMsg(InnoProductOperateMsg productOperateMsg) {
        Map<Object, Object> paramsMap = new HashMap<>();//参数信息
        try {
            int userId = productOperateMsg.getUserId();//玩家ID
            String alias = productOperateMsg.getAlias();//设备号
            paramsMap.put("alias", productOperateMsg.getAlias());//设备号
            paramsMap.put("userId", userId);//玩家ID
            paramsMap.put("userName", productOperateMsg.getUserName());//玩家昵称
            paramsMap.put("imgUrl", productOperateMsg.getImgUrl());//玩家头像
            paramsMap.put("serverSideType", productOperateMsg.getServerSideType());//服务端类型
            paramsMap.put("requestTime", productOperateMsg.getRequestTime());//请求时间
            paramsMap.put("innoProductOperateType", productOperateMsg.getInnoProductOperateType());//自研设备操作类型
            paramsMap.put("onProductTime", productOperateMsg.getOnProductTime());//上机时间
            paramsMap.put("productCost", productOperateMsg.getProductCost());//设备币值
            paramsMap.put("agyptOpenBox", productOperateMsg.getAgyptOpenBox());//埃及开箱子
            paramsMap.put("clownCircusFerrule", productOperateMsg.getClownCircusFerrule());//小丑动物套圈
            paramsMap.put("pirateCannon", productOperateMsg.getPirateCannon());//海盗开炮
            if (productOperateMsg.getInnoProductOperateType() == InnoProductOperateTypeEnum.PUSH_COIN.getCode()) {
                paramsMap.put("coinLevelWeight", productOperateMsg.getCoinLevelWeight());//权重等级
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return paramsMap;
    }

    /**
     * 填充开始游戏占用中玩家校验返回的参数
     */
    public static Map<Object, Object> initStartGameOccupyMsg(InnoStartGameOccupyMsg startGameOccupyMsg) {
        Map<Object, Object> paramsMap = new HashMap<>();//参数信息
        paramsMap.put("alias", startGameOccupyMsg.getAlias());//设备号
        paramsMap.put("requestTime", TimeUtil.getNowTime());//请求时间
        paramsMap.put("onProductTime", startGameOccupyMsg.getOnProductTime());//上机时间
        return paramsMap;
    }

    /**
     * 填充变更权重等级信息
     */
    public static Map<Object, Object> initChangeCoinWeightMsg(InnoChangeCoinWeightMsg changeCoinWeightMsg) {
        Map<Object, Object> paramsMap = new HashMap<>();//参数信息
        paramsMap.put("alias", changeCoinWeightMsg.getAlias());//设备号
        paramsMap.put("userId", changeCoinWeightMsg.getUserId());//玩家ID
        paramsMap.put("userName", changeCoinWeightMsg.getUserName());//玩家昵称
        paramsMap.put("imgUrl", changeCoinWeightMsg.getImgUrl());//玩家头像
        paramsMap.put("serverSideType", changeCoinWeightMsg.getServerSideType());//服务端类型
        paramsMap.put("requestTime", changeCoinWeightMsg.getRequestTime());//请求时间
        paramsMap.put("coinLevelWeight", changeCoinWeightMsg.getCoinLevelWeight());//游戏币权重
        return paramsMap;
    }

    /**
     * 变更游戏币权重的参数信息
     */
    public static InnoChangeCoinWeightMsg initInnoChangeCoinWeightMsg(int productId, String alias, int userId,
            int coinWeight) {
        InnoChangeCoinWeightMsg msg = new InnoChangeCoinWeightMsg();
        msg.setProductId(productId);//设备ID
        msg.setAlias(alias);//设备号
        msg.setUserId(userId);//玩家ID
        //查询玩家信息
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setUserName(userInfoEntity==null?"":userInfoEntity.getNickName());//玩家昵称
        msg.setImgUrl(userInfoEntity==null?"": MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));//玩家头像
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());//服务端类型
        msg.setRequestTime(TimeUtil.getNowTime());//请求时间
        msg.setCoinLevelWeight(coinWeight);//权重等级
        return msg;
    }

    /**
     * 将自研设备的错误码转换成系统的错误码
     */
    public static int loadClientCode(int status) {
        if(status==InnoClientCode.PRODUCT_EXCEPTION.getCode()){
            //设备异常
            status = ClientCode.PRODUCT_EXCEPTION.getCode();
        }else if(status==InnoClientCode.PRODUCT_OCCUPY.getCode()){
            //设备占用中
            status = ClientCode.PRODUCT_OCCUPY.getCode();//设备占用中
        }
        return status;
    }

    /**
     * 开始游戏的参数
     */
    public static InnoReceiveStartGameMsg startGameMsg(Map<String, Object> jsonMap) {
        InnoReceiveStartGameMsg msg = new InnoReceiveStartGameMsg();
        try{
            msg.setStatus((int)jsonMap.get("status"));//错误码
            msg.setAlias(jsonMap.get("alias").toString());//设备号
            msg.setUserId((int)jsonMap.get("userId"));//玩家ID
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            msg = null;
            LogUtil.getLogger().info("解析自研设备服务器推送开始游戏的参数失败----------");
        }
        return msg;
    }

    /**
     * 设备操作的参数
     */
    public static InnoReceiveProductOperateMsg productOperateMsg(Map<String, Object> jsonMap) {
        InnoReceiveProductOperateMsg msg = new InnoReceiveProductOperateMsg();
        try{
            msg.setStatus((int)jsonMap.get("status"));//错误码
            msg.setAlias(jsonMap.get("alias").toString());//设备号
            msg.setUserId((int)jsonMap.get("userId"));//玩家ID
            msg.setUserName((String)jsonMap.get("userName"));//玩家昵称
            msg.setImgUrl((String)jsonMap.get("imgUrl"));//玩家头像
            msg.setServerSideType((int)jsonMap.get("serverSideType"));//服务端类型
            msg.setInnoProductOperateType((int)jsonMap.get("innoProductOperateType"));//自研设备操作类型
            msg.setOnProductTime(Long.parseLong(jsonMap.get("onProductTime").toString()));//上机时间
            //设备中奖类型
            if(jsonMap.get("awardType")!=null){
                msg.setAwardType((int)jsonMap.get("awardType"));
            }
            //设备故障类型
            if(jsonMap.get("breakType")!=null){
                msg.setBreakType((int)jsonMap.get("breakType"));
            }
            //获得币数量
            if(jsonMap.get("coinNum")!=null){
                msg.setCoinNum((int)jsonMap.get("coinNum"));
            }
            //设备显示奖励游戏币
            if(jsonMap.get("awardNum")!=null){
                msg.setAwardNum((int)jsonMap.get("awardNum"));
            }
            //设备显示是否开始中奖
            if(jsonMap.get("isStart")!=null){
                msg.setIsStart((int)jsonMap.get("isStart"));
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            msg = null;
            LogUtil.getLogger().info("解析自研设备服务器推送设备操作的参数失败----------");
        }
        return msg;
    }

    /**
     * 开始游戏占用中玩家校验的参数
     */
    public static InnoStartGameOccupyMsg startGameOccupyMsg(Map<String, Object> jsonMap) {
        InnoStartGameOccupyMsg msg = new InnoStartGameOccupyMsg();
        try{
            msg.setAlias(jsonMap.get("alias").toString());//设备号
            msg.setUserId((int)jsonMap.get("userId"));//玩家ID
            msg.setUserName((String)jsonMap.get("userName"));//玩家昵称
            msg.setImgUrl((String)jsonMap.get("imgUrl"));//玩家头像
            msg.setServerSideType((int)jsonMap.get("serverSideType"));//服务端类型
            msg.setOnProductTime(Long.parseLong(jsonMap.get("onProductTime").toString()));//上机时间
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            msg = null;
            LogUtil.getLogger().info("解析自研设备服务器推送设备开始游戏占用中玩家校验的参数失败----------");
        }
        return msg;
    }

    /**
     * 填充设备信息
     */
    public static InnoProductMsg initInnoProductMsg(Map<String, Object> jsonMap) {
        InnoProductMsg msg = new InnoProductMsg();
        msg.setAlias((String) jsonMap.get("alias"));//设备号
        msg.setUserId((int) jsonMap.get("userId"));//玩家ID
        msg.setUserName((String) jsonMap.get("userName"));//玩家昵称
        msg.setImgUrl((String) jsonMap.get("imgUrl"));//玩家头像
        msg.setServerSideType((int) jsonMap.get("serverSideType"));//服务端类型
        msg.setProductMulti(jsonMap.containsKey("productMulti")?
                (int)jsonMap.get("productMulti"):0);//设备倍率
        return msg;
    }

    /**
     * 填充设备信息
     */
    public static ResponseGeneralMsg initResponseGeneralMsg(String alias, int userId) {
        ResponseGeneralMsg msg = new ResponseGeneralMsg();
        msg.setAlias(alias);//设备号
        msg.setUserId(userId);//玩家ID
        //查询玩家信息
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setUserName(userInfoEntity==null?"":userInfoEntity.getNickName());//玩家昵称
        msg.setImgUrl(userInfoEntity==null?"": MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));//玩家头像
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());//服务端类型
        return msg;
    }

    /**
     * 填充获得币信息
     */
    public static InnoGetCoinMsg initInnoGetCoinMsg(JSONObject jsonObject) {
        InnoGetCoinMsg msg = new InnoGetCoinMsg();
        msg.setAlias(jsonObject.getString("alias"));//设备号
        msg.setUserId(jsonObject.getInteger("userId"));//玩家ID
        msg.setUserName(jsonObject.getString("userName"));//玩家昵称
        msg.setImgUrl(jsonObject.getString("imgUrl"));//玩家头像
        msg.setServerSideType(jsonObject.getInteger("serverSideType"));//服务端类型
        msg.setRetNum(jsonObject.getInteger("retNum"));//获得游戏币数
        return msg;
    }

    /**
     * 填充中奖锁信息
     */
    public static InnoAwardLockMsg initInnoAwardLockMsg(Map<String, Object> jsonMap) {
        InnoAwardLockMsg msg = new InnoAwardLockMsg();
        msg.setAlias(jsonMap.get("alias").toString());//设备号
        msg.setUserId((int)jsonMap.get("userId"));//玩家ID
        msg.setServerSideType((int)jsonMap.get("serverSideType"));//服务端类型
        msg.setIsStart((int)jsonMap.get("isStart"));//填充中奖锁信息
        return msg;
    }

    /**
     * 填充自动投币信息
     */
    public static Map<Object, Object> initAutoPushCoinMsg(InnoAutoPushCoinMsg autoPushCoinMsg) {
        Map<Object, Object> paramsMap = new HashMap<>();//参数信息
        paramsMap.put("alias", autoPushCoinMsg.getAlias());//设备号
        paramsMap.put("userId", autoPushCoinMsg.getUserId());//玩家ID
        paramsMap.put("userName", autoPushCoinMsg.getUserName());//玩家昵称
        paramsMap.put("imgUrl", autoPushCoinMsg.getImgUrl());//玩家头像
        paramsMap.put("serverSideType", autoPushCoinMsg.getServerSideType());//服务端类型
        paramsMap.put("requestTime", autoPushCoinMsg.getRequestTime());//请求时间
        paramsMap.put("isStart", autoPushCoinMsg.getIsStart());//是否开始自动投币
        return paramsMap;
    }

    /**
     * 自动投币的参数信息
     */
    public static InnoAutoPushCoinMsg initInnoAutoPushCoinMsg(int productId, String alias, int userId, int isStart) {
        InnoAutoPushCoinMsg msg = new InnoAutoPushCoinMsg();
        msg.setProductId(productId);//设备ID
        msg.setAlias(alias);//设备号
        msg.setUserId(userId);//玩家ID
        //查询玩家信息
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        msg.setUserName(userInfoEntity==null?"":userInfoEntity.getNickName());//玩家昵称
        msg.setImgUrl(userInfoEntity==null?"": MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));//玩家头像
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());//服务端类型
        msg.setRequestTime(TimeUtil.getNowTime());//请求时间
        msg.setIsStart(isStart);//是否开始自动投币
        return msg;
    }

    /**
     * 填充结算窗口信息
     */
    public static InnoSettlementWindowMsg initInnoSettlementWindowMsg(Map<String, Object> jsonMap) {
        InnoSettlementWindowMsg msg = new InnoSettlementWindowMsg();
        msg.setAlias(jsonMap.get("alias").toString());//设备号
        msg.setUserId((int)jsonMap.get("userId"));//玩家ID
        msg.setServerSideType((int)jsonMap.get("serverSideType"));//服务端类型
        return msg;
    }

    /**
     * 填充中奖得分倍数信息
     */
    public static InnoAwardScoreMultiMsg initInnoAwardScoreMultiMsg(Map<String, Object> jsonMap) {
        InnoAwardScoreMultiMsg msg = new InnoAwardScoreMultiMsg();
        msg.setAlias(jsonMap.get("alias").toString());//设备号
        msg.setUserId((int)jsonMap.get("userId"));//玩家ID
        msg.setServerSideType((int)jsonMap.get("serverSideType"));//服务端类型
        msg.setAwardMulti((int)jsonMap.get("awardMulti"));//中奖得分倍数
        return msg;
    }

    /**
     * 填充设备声音通知信息
     */
    public static InnoVoiceNoticeMsg initInnoVoiceNoticeMsg(Map<String, Object> jsonMap) {
        InnoVoiceNoticeMsg msg = new InnoVoiceNoticeMsg();
        msg.setAlias(jsonMap.get("alias").toString());//设备号
        msg.setUserId((int)jsonMap.get("userId"));//玩家ID
        msg.setServerSideType((int)jsonMap.get("serverSideType"));//服务端类型
        msg.setVoiceType((int)jsonMap.get("voiceType"));//声音类型
        msg.setIsStart((int)jsonMap.get("isStart"));//是否开始播放音效
        msg.setIsEndSwitch((int)jsonMap.get("isStart"));//是否有开始结束
        return msg;
    }

    /**
     * 获取string类型的参数
     */
    public static String loadStringParams(Map<String, Object> jsonMap, String paramsName) {
        return jsonMap.containsKey(paramsName)? jsonMap.get(paramsName).toString():"";
    }

    /**
     * 获取int类型的参数
     */
    public static int intParams(Map<String, Object> jsonMap, String paramsName) {
        return jsonMap.containsKey(paramsName)?Integer.parseInt(jsonMap.get(paramsName).toString()):0;
    }

    /**
     * 获取直连的参数信息
     */
    public static JSONObject loadClientDirectMsg(JSONObject jsonMap) {
        JSONObject retMsg = new JSONObject();
        if(jsonMap.containsKey("data")){
            Map<String, Object> dataMap = (Map<String, Object>)jsonMap.get("data");
            if(dataMap!=null && dataMap.containsKey("detailMsg")){
                retMsg = (JSONObject) dataMap.get("detailMsg");
            }
        }
        return retMsg;
    }

    /**
     * 填充中奖订阅通知信息
     */
    public static InnoProductAwardMsg initInnoProductAwardMsg(JSONObject jsonObject) {
        InnoProductAwardMsg msg = new InnoProductAwardMsg();
        msg.setAlias(jsonObject.getString("alias"));//设备号
        msg.setUserId(jsonObject.getInteger("userId"));//玩家ID
        msg.setUserName(jsonObject.getString("userName"));//玩家昵称
        msg.setImgUrl(jsonObject.getString("imgUrl"));//玩家头像
        msg.setServerSideType(jsonObject.getInteger("serverSideType"));//服务端类型
        msg.setAwardType(jsonObject.getInteger("awardType"));//中奖类型
        return msg;
    }

    /**
     * 填充龙珠订阅通知信息
     */
    public static InnoDragonMsg initInnoDragonMsg(JSONObject jsonObject) {
        InnoDragonMsg msg = new InnoDragonMsg();
        msg.setAlias(jsonObject.getString("alias"));//设备号
        return msg;
    }

    /**
     * 填充设备操作信息
     */
    public static Map<Object, Object> initProductOperateMsg(int productId, ProductInfoEntity productInfoEntity,
            int userId, int awardType) {
        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        InnoProductOperateMsg productOperateMsg = InnoParamsUtil.initInnoProductOperateMsg(productId, productInfoEntity.getAlias(), userId,
                productRoomMsg.getOnProductTime(), InnoProductOperateTypeEnum.PUSH_COIN.getCode(), false);
        Map<Object, Object> paramsMap = new HashMap<>();//参数信息
        paramsMap.put("alias", productOperateMsg.getAlias());//设备号
        paramsMap.put("userId", userId);//玩家ID
        paramsMap.put("userName", productOperateMsg.getUserName());//玩家昵称
        paramsMap.put("imgUrl", productOperateMsg.getImgUrl());//玩家头像
        paramsMap.put("serverSideType", productOperateMsg.getServerSideType());//服务端类型
        paramsMap.put("requestTime", productOperateMsg.getRequestTime());//请求时间
        paramsMap.put("innoProductOperateType", productOperateMsg.getInnoProductOperateType());//自研设备操作类型
        paramsMap.put("onProductTime", productOperateMsg.getOnProductTime());//上机时间
        paramsMap.put("productCost", productOperateMsg.getProductCost());//设备币值
        paramsMap.put("productAwardType",awardType);//设备中奖类型
        return paramsMap;
    }
}
