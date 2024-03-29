package avatar.task.innoMsg;

import avatar.util.innoMsg.InnoProductSpecialUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 自研推送奖励定时器
 */
public class InnoPushAwardTask extends ScheduledTask {

    //玩家ID
    private int userId;

    //设备ID
    private int productId;

    //奖励类型
    private int awardType;

    public InnoPushAwardTask(int userId, int productId, int awardType) {
        super("自研推送奖励定时器");
        this.userId = userId;
        this.productId = productId;
        this.awardType = awardType;
    }

    @Override
    public void run() {
        InnoProductSpecialUtil.specialAward(userId, productId, awardType);
    }
}
