package avatar.task.innoMsg;

import avatar.util.innoMsg.SyncInnoConnectUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 开始链接自研设备服务器
 */
public class ConnectInnoProductTask extends ScheduledTask {
    public ConnectInnoProductTask() {
        super("开始链接自研设备服务器");
    }

    @Override
    public void run() {
        SyncInnoConnectUtil.connectInnoProductServer();
    }
}
