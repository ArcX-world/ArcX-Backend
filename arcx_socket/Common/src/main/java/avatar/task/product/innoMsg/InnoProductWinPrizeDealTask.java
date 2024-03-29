package avatar.task.product.innoMsg;

import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 自研设备中奖处理定时器
 */
public class InnoProductWinPrizeDealTask extends ScheduledTask {

    //设备中奖类型
    private int awardType;

    //设备ID
    private int productId;

    //玩家ID
    private int userId;

    //设备中奖数量
    private int awardNum;

    //是否开始中奖
    private int isStart;

    public InnoProductWinPrizeDealTask(int awardType, int productId, int userId, int awardNum, int isStart) {
        super("自研设备中奖处理定时器");
        this.awardType = awardType;
        this.productId = productId;
        this.userId = userId;
        this.awardNum = awardNum;
        this.isStart = isStart;
    }

    @Override
    public void run() {
        if(UserOnlineUtil.isOnline(userId)){
            JSONObject dataJson = new JSONObject();
            dataJson.put("devId", productId);//设备ID
            dataJson.put("devAwdTp", awardType);//设备中奖类型
            dataJson.put("awdAmt", awardNum);//获得游戏币
            dataJson.put("stFlg", isStart);//是否开始
            //推送前端
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_INNO_WIN_PRIZE, ClientCode.SUCCESS.getCode(), userId, dataJson);
        }
    }
}
