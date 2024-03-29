package avatar.task.product.operate;

import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 设备投币后续处理定时器
 */
public class ProductPushCoinTask extends ScheduledTask {
    //设备ID
    private int productId;

    //玩家ID
    private int userId;

    //投币分数
    private int pushCoin;

    public ProductPushCoinTask(int productId, int userId, int pushCoin) {
        super("设备投币后续处理定时器");
        this.productId = productId;
        this.userId = userId;
        this.pushCoin = pushCoin;
    }

    @Override
    public void run() {
    }
}
