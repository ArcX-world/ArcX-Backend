package avatar.task.normalProduct;

import avatar.util.LogUtil;
import avatar.util.normalProduct.InnerNormalProductDealUtil;
import avatar.util.normalProduct.InnerNormalProductOperateUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 重连内部普通设备服务端socket定时器
 */
public class ReconnectInnerNormalProductSocketTask extends ScheduledTask {
    private String host;//连接的服务器IP

    private int port;//端口

    public ReconnectInnerNormalProductSocketTask(String host, int port) {
        super("重连内部普通设备服务端socket定时器");
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        LogUtil.getLogger().info("重连普通设备服务端websocket，ip:{},port:{}------", host, port);
        //重连
        InnerNormalProductOperateUtil.loadClient(host, port);
        //socket关闭处理
        InnerNormalProductDealUtil.socketCloseDeal(host, port+"");
    }
}
