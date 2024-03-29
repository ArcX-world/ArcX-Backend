package avatar.task.innoMsg;

import avatar.data.product.innoMsg.InnoStartGameOccupyMsg;
import avatar.module.product.info.ProductAliasDao;
import avatar.util.innoMsg.SyncInnoClient;
import avatar.util.innoMsg.SyncInnoConnectUtil;
import avatar.util.innoMsg.SyncInnoOperateUtil;
import avatar.util.innoMsg.SyncInnoUtil;
import avatar.util.product.ProductUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 推送自研设备服务器开始游戏占用中校验操作定时器
 */
public class SyncInnoOccpuyCheckTask extends ScheduledTask {
    private InnoStartGameOccupyMsg startGameOccupyMsg;//开始游戏占用中信息

    public SyncInnoOccpuyCheckTask(InnoStartGameOccupyMsg startGameOccupyMsg) {
        super("推送自研设备服务器开始游戏占用中校验操作定时器");
        this.startGameOccupyMsg = startGameOccupyMsg;
    }

    @Override
    public void run() {
        int productId = ProductAliasDao.getInstance().loadByAlias(startGameOccupyMsg.getAlias());//设备ID
        String host = ProductUtil.productIp(productId);//ip
        int port = ProductUtil.productSocketPort(productId);//端口
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);//链接信息
        //推送操作信息
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        if (client != null && client.isOpen()) {
            //推送开始游戏校验返回请求
            SyncInnoUtil.startGameOccupy(client, startGameOccupyMsg);
        }
    }
}
