package avatar.task.product.innoMsg;

import avatar.util.user.UserUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 自研设备奖励通用处理
 */
public class InnoProductWinPrizeNormalDealTask extends ScheduledTask {

    //设备ID
    private int productId;

    //玩家ID
    private int userId;

    //设备奖励类型
    private int awardType;

    public InnoProductWinPrizeNormalDealTask(int productId, int userId, int awardType) {
        super("自研设备奖励通用处理");
        this.productId = productId;
        this.userId = userId;
        this.awardType = awardType;
    }

    @Override
    public void run() {
        //更新玩家设备大奖信息
        UserUtil.updateUserGrandPrizeMsg(userId, awardType, productId);
    }
}
