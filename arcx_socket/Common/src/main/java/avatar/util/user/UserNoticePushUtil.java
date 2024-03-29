package avatar.util.user;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.product.gamingMsg.LotteryMsg;
import avatar.data.user.attribute.UserEnergyMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.module.user.attribute.UserAttributeMsgDao;
import avatar.util.LogUtil;
import avatar.util.basic.general.ImgUtil;
import avatar.util.basic.general.MediaUtil;
import avatar.util.product.ProductUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.JsonUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家通知推送工具类
 */
public class UserNoticePushUtil {
    /**
     * 玩家余额信息推送
     */
    public static void userBalancePush(int userId) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("gdAmt", UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode()));//金币
        dataJson.put("axcAmt", UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.AXC.getCode()));//axc
        //推送前端
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_USER_BALANCE,
                ClientCode.SUCCESS.getCode(), userId, dataJson);
    }

    /**
     * 推送彩票进度通知
     */
    public static void pushLotteryNotice(int productId, int secondType, int userId, int addLotteryNum, int addCoinNum) {
        JSONObject dataJson = new JSONObject();
        LotteryMsg msg = ProductUtil.initUserLotteryMsg(userId, secondType, addCoinNum, addLotteryNum);
        if(msg!=null) {
            dataJson.put("devId", productId);//设备ID
            dataJson.put("awdLotAmt", addLotteryNum);//添加彩票数
            dataJson.put("lotAmy", msg.getNum());//当前彩票数
            dataJson.put("amtUpp", msg.getMaxNum());//彩票数上限
            dataJson.put("awdGdAmt", addCoinNum);//添加的游戏币
            //推送前端
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_PRODUCT_LOTTERY_PROGRESS,
                    ClientCode.SUCCESS.getCode(), userId, dataJson);
        }
    }

    /**
     * 推送炼金塔堆塔奖励通知
     */
    public static void pushPileTowerAward(int userId, int productId, int awardNum, int awardImgId) {
        if(UserOnlineUtil.isOnline(userId)) {
            JSONObject dataJson = new JSONObject();
            dataJson.put("devId", productId);//设备ID
            dataJson.put("awdTbln", initPileAwardList(awardNum, awardImgId));//奖励的游戏币数量
            //推送前端
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_PILE_TOWER_AWARD,
                    ClientCode.SUCCESS.getCode(), userId, dataJson);
        }
    }

    /**
     * 填充堆塔奖励信息
     */
    private static List<GeneralAwardMsg> initPileAwardList(int awardNum, int awardImgId) {
        List<GeneralAwardMsg> retList = new ArrayList<>();
        GeneralAwardMsg msg = new GeneralAwardMsg();
        msg.setCmdTp(CommodityTypeEnum.GOLD_COIN.getCode());//商品类型
        msg.setAwdPct(MediaUtil.getMediaUrl(ImgUtil.loadAwardImg(awardImgId)));//奖励图片
        msg.setAwdAmt(awardNum);//奖励数量
        retList.add(msg);
        return retList;
    }

    /**
     * 推送炼金塔堆塔状态通知
     */
    public static void pileTower(int userId, int productId, int isStop) {
        if(UserOnlineUtil.isOnline(userId)) {
            JSONObject dataJson = new JSONObject();
            dataJson.put("devId", productId);//设备ID
            dataJson.put("stpFlg", isStop);//是否停止
            //推送前端
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_PILE_TOWER_STATUS_NOTICE,
                    ClientCode.SUCCESS.getCode(), userId, dataJson);
        }
    }

    /**
     * 踢出设备推送
     */
    public static void kickOutProductPush(int userId, int productId) {
        JSONObject dataJson = new JSONObject();//返回前端的参数信息
        dataJson.put("devId", productId);//设备ID
        //推送前端
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_KICK_OUT_PRODUCT, ClientCode.SUCCESS.getCode(),
                userId, dataJson);
    }

    /**
     * 系统通知推送
     */
    public static void systemNoticePush(int userId, String content) {
        JSONObject dataJson = new JSONObject();//返回前端的参数信息
        dataJson.put("ntcCt", content);//通知内容
        //推送前端
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_SYSTEM_NOTICE, ClientCode.SUCCESS.getCode(),
                userId, dataJson);
    }

    /**
     * 能量信息
     */
    public static void userEnergyMsg(int userId, long changeNum) {
        UserEnergyMsg msg = UserAttributeUtil.userEnergyMsg(UserAttributeMsgDao.getInstance().loadMsg(userId));
        JSONObject dataJson = new JSONObject();//返回前端的参数信息
        dataJson.put("cnAmt", msg.getCnAmt());//当前进度数量
        dataJson.put("ttAmt", msg.getTtAmt());//总进度数量
        dataJson.put("cgAmt", changeNum);//变更数量
        dataJson.put("lfTm", msg.getLfTm());//下次刷新时间
        //推送前端
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_ENERGY_MSG, ClientCode.SUCCESS.getCode(),
                userId, dataJson);
    }

    /**
     * 推送前端龙珠中奖信息
     */
    public static void pushDragonAwardMsg(int userId, JSONObject dataJson) {
        LogUtil.getLogger().info("推送玩家{}自研设备龙珠奖励信息通知的信息：{}--------", userId, JsonUtil.mapToJson(dataJson));
        //推送前端
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_INNO_DRAGON_AWARD_MSG,
                ClientCode.SUCCESS.getCode(), userId, dataJson);
    }
}
