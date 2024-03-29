package avatar.task.recharge;

import avatar.entity.recharge.superPlayer.SuperPlayerAwardEntity;
import avatar.util.basic.CommodityUtil;
import avatar.util.log.UserCostLogUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.List;

/**
 * 添加超级玩家奖励定时器
 */
public class AddSuperPlayerAwardTask extends ScheduledTask {

    //玩家ID
    private int userId;

    //奖励列表
    private List<SuperPlayerAwardEntity> awardList;

    public AddSuperPlayerAwardTask(int userId, List<SuperPlayerAwardEntity> awardList) {
        super("添加超级玩家奖励定时器");
        this.userId = userId;
        this.awardList = awardList;
    }

    @Override
    public void run() {
        awardList.forEach(entity->{
            if(entity.getAwardNum()>0){
                int awardType = entity.getAwardType();//商品类型
                int awardId = entity.getAwardId();//奖励ID
                int awardNum = entity.getAwardNum();//奖励数量
                if(awardType==CommodityUtil.gold()){
                    //金币
                    UserCostLogUtil.superPlayerGold(userId, awardNum);
                }else if(awardType==CommodityUtil.property()){
                    //道具
                    UserCostLogUtil.superPlayerProperty(userId, awardId, awardNum);
                }
            }
        });
    }

}
