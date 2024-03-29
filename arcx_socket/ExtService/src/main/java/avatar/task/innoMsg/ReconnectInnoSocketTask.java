package avatar.task.innoMsg;

import avatar.util.LogUtil;
import avatar.util.innoMsg.SyncInnoDealUtil;
import avatar.util.innoMsg.SyncInnoOperateUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 重连自研设备socket定时器
 */
public class ReconnectInnoSocketTask extends ScheduledTask {
    private String host;//连接的服务器IP

    private int port;//端口

    private String fromKey;

    private String toKey;

    public ReconnectInnoSocketTask(String host, int port, String fromKey, String toKey) {
        super("重连自研设备socket定时器");
        this.host = host;
        this.port = port;
        this.fromKey = fromKey;
        this.toKey = toKey;
    }

    @Override
    public void run() {
        LogUtil.getLogger().info("重连自研设备websocket，ip:{},port:{}------", host, port);
        //重连
        SyncInnoOperateUtil.loadClient(host, port);
        //socket关闭处理
        SyncInnoDealUtil.socketCloseDeal(fromKey, toKey);

    }
}
