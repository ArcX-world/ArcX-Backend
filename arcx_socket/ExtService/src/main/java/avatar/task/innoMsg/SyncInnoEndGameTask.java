package avatar.task.innoMsg;

import avatar.data.product.innoMsg.InnoEndGameMsg;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.util.innoMsg.*;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 推送自研设备服务器退出游戏定时器
 */
public class SyncInnoEndGameTask extends ScheduledTask {
    private String host;//连接的服务器IP

    private int port;//端口

    private InnoEndGameMsg endGameMsg;//结束游戏信息

    public SyncInnoEndGameTask(String host, int port, InnoEndGameMsg endGameMsg) {
        super("推送自研设备服务器退出游戏定时器");
        this.host = host;
        this.port = port;
        this.endGameMsg = endGameMsg;
    }

    @Override
    public void run() {
        //发送心跳
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        if (client != null && client.isOpen()) {
            //推送结束游戏请求
            SyncInnoUtil.endGame(client, endGameMsg);
        }else{
            InnoSendWebsocketUtil.sendWebsocketMsg(endGameMsg.getUserId(),
                    ProductOperationEnum.OFF_LINE.getCode(), ClientCode.PRODUCT_EXCEPTION.getCode(), 0,
                    endGameMsg.getProductId());
        }
    }
}
