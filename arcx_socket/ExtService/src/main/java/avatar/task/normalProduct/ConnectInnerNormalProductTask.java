package avatar.task.normalProduct;

import avatar.util.normalProduct.InnerNormalProductConnectUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 开始连接普通设备服务器
 */
public class ConnectInnerNormalProductTask extends ScheduledTask {
    public ConnectInnerNormalProductTask() {
        super("开始连接普通设备服务器");
    }

    @Override
    public void run() {
        InnerNormalProductConnectUtil.connectProductServer();
    }
}
