package avatar.task.innoMsg;

import avatar.data.product.innoMsg.InnoStartGameMsg;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.util.innoMsg.*;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 推送自研设备服务器开始游戏定时器
 */
public class SyncInnoStartGameTask extends ScheduledTask {
    private String host;//连接的服务器IP

    private int port;//端口

    private InnoStartGameMsg startGameMsg;

    public SyncInnoStartGameTask(String host, int port, InnoStartGameMsg startGameMsg) {
        super("推送自研设备服务器开始游戏定时器");
        this.host = host;
        this.port = port;
        this.startGameMsg = startGameMsg;
    }

    @Override
    public void run() {
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);//连接信息
        //发送心跳
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        if (client != null && client.isOpen()) {
            //推送开始游戏请求
            SyncInnoUtil.startGame(client, startGameMsg);
        }else{
            InnoSendWebsocketUtil.sendWebsocketMsg(startGameMsg.getUserId(),
                    ProductOperationEnum.START_GAME.getCode(), ClientCode.PRODUCT_EXCEPTION.getCode(), 0,
                    startGameMsg.getProductId());
        }
    }
}
