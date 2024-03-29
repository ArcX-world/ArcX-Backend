package avatar.task.innoMsg;

import avatar.util.innoMsg.SyncInnoClient;
import avatar.util.innoMsg.SyncInnoConnectUtil;
import avatar.util.innoMsg.SyncInnoOperateUtil;
import avatar.util.innoMsg.SyncInnoUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.product.ProductUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 推送自研设备服务器变更权重等级定时器
 */
public class SyncInnoChangeCoinWeightTask extends ScheduledTask {

    private int userId;//玩家ID

    private int productId;//设备ID

    private int coinWeight;//权重等级

    public SyncInnoChangeCoinWeightTask(int userId, int productId, int coinWeight) {
        super("推送自研设备服务器变更权重等级定时器");
        this.userId = userId;
        this.productId = productId;
        this.coinWeight = coinWeight;
    }

    @Override
    public void run() {
        //发送心跳
        String host = ProductUtil.productIp(productId);
        int port = ProductUtil.productSocketPort(productId);
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);//链接信息
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        if (client != null && client.isOpen()) {
            //推送等级变更请求请求
            SyncInnoUtil.changeCoinWeight(client, InnoParamsUtil.initInnoChangeCoinWeightMsg(productId,
                    ProductUtil.loadProductAlias(productId), userId, coinWeight));
        }
    }
}
