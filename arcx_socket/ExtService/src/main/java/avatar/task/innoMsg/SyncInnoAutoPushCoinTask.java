package avatar.task.innoMsg;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.util.LogUtil;
import avatar.util.innoMsg.SyncInnoClient;
import avatar.util.innoMsg.SyncInnoConnectUtil;
import avatar.util.innoMsg.SyncInnoOperateUtil;
import avatar.util.innoMsg.SyncInnoUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.product.ProductUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 推送自研设备服务器自动投币定时器
 */
public class SyncInnoAutoPushCoinTask extends ScheduledTask {

    private int userId;//玩家ID

    private int productId;//设备ID

    private int isStart;//是否开始自动投币

    public SyncInnoAutoPushCoinTask(int userId, int productId, int isStart) {
        super("推送自研设备服务器自动投币定时器");
        this.userId = userId;
        this.productId = productId;
        this.isStart = isStart;
    }

    @Override
    public void run() {
        //发送心跳
        String host = ProductUtil.productIp(productId);
        int port = ProductUtil.productSocketPort(productId);
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);//链接信息
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        if (client != null && client.isOpen()) {
            LogUtil.getLogger().info("推送自研设备{}做操作：{}--------", productId, isStart== YesOrNoEnum.YES.getCode()?
                    "开始自动投币":"取消自动投币");
            //推送自动投币请求请求
            SyncInnoUtil.authPushCoin(client, InnoParamsUtil.initInnoAutoPushCoinMsg(productId,
                    ProductUtil.loadProductAlias(productId), userId, isStart));
        }
    }
}
