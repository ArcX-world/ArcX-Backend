package avatar.task.recharge;

import avatar.util.log.UserCostLogUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 添加官方金币奖励定时器
 */
public class AddOfficialRechargeCoinAwardTask extends ScheduledTask {

    //玩家ID
    private int userId;

    //游戏币
    private long awardNum;

    public AddOfficialRechargeCoinAwardTask(int userId, long awardNum) {
        super("添加官方金币奖励定时器");
        this.userId = userId;
        this.awardNum = awardNum;
    }

    @Override
    public void run() {
        //金币
        UserCostLogUtil.officialRechargeGold(userId, awardNum);
    }

}
