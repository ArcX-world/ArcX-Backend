package avatar.task.innoMsg;

import avatar.module.product.innoMsg.SyncInnoHeartTimeDao;
import avatar.util.innoMsg.SyncInnoClient;
import avatar.util.innoMsg.SyncInnoConnectUtil;
import avatar.util.innoMsg.SyncInnoOperateUtil;
import avatar.util.innoMsg.SyncInnoUtil;
import avatar.util.trigger.SchedulerSample;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 自研设备服务器心跳
 */
public class SyncInnoHeartTask extends ScheduledTask {
    private String host;//连接的服务器IP

    private int port;//端口

    private long startTime;//开始时间戳

    public SyncInnoHeartTask(String host, int port, long startTime) {
        super("websocket自研设备服务器内部心跳");
        this.host = host;
        this.port = port;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);
        if(startTime== SyncInnoHeartTimeDao.getInstance().loadTime(linkMsg)) {
            //发送心跳
            SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
            if (client != null && client.isOpen()) {
                //推送心跳
                SyncInnoUtil.heart(client);
            }
            //10秒后再次发送
            SchedulerSample.delayed(10000, new SyncInnoHeartTask(host, port, startTime));
        }
    }
}
