package avatar.task.product.general;

import avatar.data.product.gamingMsg.DollAwardCommodityMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.util.log.CostUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 添加娃娃机奖励定时器
 */
public class AddPresentAwardTask extends ScheduledTask {
    //设备ID
    private int productId;

    //玩家ID
    private int userId;

    //奖励信息
    private DollAwardCommodityMsg awardMsg;

    public AddPresentAwardTask(int productId, int userId, DollAwardCommodityMsg awardMsg) {
        super("添加娃娃机奖励定时器");
        this.productId = productId;
        this.userId = userId;
        this.awardMsg = awardMsg;
    }

    @Override
    public void run() {
        int commodityType = awardMsg.getCommodityType();//商品类型
        int awardNum = awardMsg.getAwardNum();//奖励数量
        if(commodityType== CommodityTypeEnum.GOLD_COIN.getCode()){
            //金币
            CostUtil.addProductCommodityCoin(userId, productId, commodityType, awardNum);
        }
    }
}
