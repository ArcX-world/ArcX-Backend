package avatar.task.innoMsg;

import avatar.data.product.innoMsg.InnoProductOperateMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.util.innoMsg.*;
import avatar.util.product.InnoParamsUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 推送自研设备服务器设备操作定时器
 */
public class SyncInnoProductOperateTask extends ScheduledTask {
    private String host;//连接的服务器IP

    private int port;//端口

    private InnoProductOperateMsg productOperateMsg;//设备操作信息

    public SyncInnoProductOperateTask(String host, int port, InnoProductOperateMsg productOperateMsg) {
        super("推送自研设备服务器设备操作定时器");
        this.host = host;
        this.port = port;
        this.productOperateMsg = productOperateMsg;
    }

    @Override
    public void run() {
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);//链接信息
        //推送操作信息
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        if (client != null && client.isOpen()) {
            //推送设备操作请求
            SyncInnoUtil.productOperate(client, productOperateMsg);
        }else{
            InnoSendWebsocketUtil.sendWebsocketMsg(productOperateMsg.getUserId(),
                    InnoParamsUtil.loadProductOperateType(productOperateMsg.getInnoProductOperateType()), ClientCode.PRODUCT_EXCEPTION.getCode(), 0,
                    productOperateMsg.getProductId());
        }
    }
}
