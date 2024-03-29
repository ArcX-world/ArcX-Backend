package avatar.task.product.general;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.entity.product.energy.EnergyExchangeUserAwardEntity;
import avatar.util.basic.general.CommodityUtil;
import avatar.util.log.CostUtil;
import avatar.util.user.UserAttributeUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.List;

/**
 * 添加能量兑换奖励定时器
 */
public class AddEnergyAwardTask extends ScheduledTask {
    //设备ID
    private int productId;

    //玩家ID
    private int userId;

    //获得类型
    private int getType;

    //奖励信息
    private List<GeneralAwardMsg> awardList;

    //玩家奖励信息
    private List<EnergyExchangeUserAwardEntity> awardHistoryList;

    public AddEnergyAwardTask(int productId, int userId, int getType, List<GeneralAwardMsg> awardList,
            List<EnergyExchangeUserAwardEntity> awardHistoryList) {
        super("添加能量兑换奖励定时器");
        this.productId = productId;
        this.userId = userId;
        this.getType = getType;
        this.awardList = awardList;
        this.awardHistoryList = awardHistoryList;
    }

    @Override
    public void run() {
        //添加奖励
        awardList.forEach(msg->{
            if(CommodityUtil.normalFlag(msg.getCmdTp())){
                //普通商品
                CostUtil.addEnergyExchange(userId, productId, msg.getCmdTp(), msg.getAwdAmt());
            }
        });
        //添加历史信息
        UserAttributeUtil.addEnergyExchangeHistory(userId, productId, getType, awardHistoryList);
//        //推送奖励通知
//        if(UserOnlineUtil.isOnline(userId)){
//            UserNoticePushUtil.energyAwardNotice(userId, productId, awardList);
//        }
    }
}
