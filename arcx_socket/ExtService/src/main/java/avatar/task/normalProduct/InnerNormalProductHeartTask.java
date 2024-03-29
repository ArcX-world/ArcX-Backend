package avatar.task.normalProduct;

import avatar.module.product.normalProduct.InnerNormalProductHeartTimeDao;
import avatar.util.normalProduct.InnerNormalProductClient;
import avatar.util.normalProduct.InnerNormalProductConnectUtil;
import avatar.util.normalProduct.InnerNormalProductOperateUtil;
import avatar.util.normalProduct.InnerNormalProductUtil;
import avatar.util.trigger.SchedulerSample;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 普通设备内部app服务端连接服务器心跳
 */
public class InnerNormalProductHeartTask extends ScheduledTask {
    private String host;//连接的服务器IP

    private int port;//端口

    private long startTime;//开始时间戳

    public InnerNormalProductHeartTask(String host, int port, long startTime) {
        super("普通设备内部app服务端连接服务器心跳");
        this.host = host;
        this.port = port;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        String linkMsg = host + port + InnerNormalProductConnectUtil.loadUid(host, port);//链接信息
        if(startTime== InnerNormalProductHeartTimeDao.getInstance().loadTime(linkMsg)) {
            //发送心跳
            InnerNormalProductClient client = InnerNormalProductOperateUtil.webSocket.get(linkMsg);
            if (client != null && client.isOpen()) {
                //推送心跳
                InnerNormalProductUtil.heart(client);
            }
            //10秒后再次发送
            SchedulerSample.delayed(10000, new InnerNormalProductHeartTask(host, port, startTime));
        }
    }
}
