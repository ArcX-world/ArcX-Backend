package avatar.task.product.general;

import avatar.util.LogUtil;
import avatar.util.product.ProductUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 刷新设备信息定时器
 */
public class RefreshProductMsgTask extends ScheduledTask {

    private int productId;//设备ID

    public RefreshProductMsgTask(int productId) {
        super("刷新设备信息定时器");
        this.productId = productId;
    }

    @Override
    public void run() {
        LogUtil.getLogger().info("刷新设备{}的信息--------", productId);
        //刷新
        ProductUtil.refreshRoomMsg(productId);
    }
}
