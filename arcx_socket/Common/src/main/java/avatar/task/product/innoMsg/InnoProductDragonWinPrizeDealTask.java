package avatar.task.product.innoMsg;

import avatar.global.enumMsg.product.award.EnergyExchangeGetTypeEnum;
import avatar.util.activity.DragonTrainUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.user.UserAttributeUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 自研设备龙珠中奖处理定时器
 */
public class InnoProductDragonWinPrizeDealTask extends ScheduledTask {

    //设备ID
    private int productId;

    //玩家ID
    private int userId;

    public InnoProductDragonWinPrizeDealTask(int productId, int userId) {
        super("自研设备龙珠中奖处理定时器");
        this.productId = productId;
        this.userId = userId;
    }

    @Override
    public void run() {
        //龙珠玛丽机处理
        DragonTrainUtil.addUserDragon(userId, productId);
    }
}
