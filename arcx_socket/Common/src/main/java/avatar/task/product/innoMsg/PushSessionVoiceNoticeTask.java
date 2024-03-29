package avatar.task.product.innoMsg;

import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.net.session.Session;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 推送session声音处理定时器
 */
public class PushSessionVoiceNoticeTask extends ScheduledTask {

    //声音通知信息
    private JSONObject jsonMap;

    //设备ID
    private int productId;

    //session信息
    private Session session;

    public PushSessionVoiceNoticeTask(JSONObject jsonMap, int productId, Session session) {
        super("推送session声音处理定时器");
        this.jsonMap = jsonMap;
        this.productId = productId;
        this.session = session;
    }

    @Override
    public void run() {
        jsonMap.put("devId", productId);
        //推送前端
        SendWebsocketMsgUtil.sendBySession(WebSocketCmd.S2C_PRODUCT_VOICE_NOTICE,
                ClientCode.SUCCESS.getCode(), session, jsonMap);
    }
}
