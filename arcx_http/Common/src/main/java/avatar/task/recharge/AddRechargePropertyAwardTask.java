package avatar.task.recharge;

import avatar.entity.recharge.property.RechargePropertyMsgEntity;
import avatar.util.log.UserCostLogUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 添加充值道具奖励定时器
 */
public class AddRechargePropertyAwardTask extends ScheduledTask {

    //玩家ID
    private int userId;

    //商品信息
    private RechargePropertyMsgEntity entity;

    public AddRechargePropertyAwardTask(int userId, RechargePropertyMsgEntity entity) {
        super("添加充值道具奖励定时器");
        this.userId = userId;
        this.entity = entity;
    }

    @Override
    public void run() {
        //道具
        UserCostLogUtil.rechargePropertyAward(userId, entity);
    }

}
