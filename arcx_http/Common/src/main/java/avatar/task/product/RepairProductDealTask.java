package avatar.task.product;

import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.thirdpart.OfficialAccountUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 报修设备后续处理定时器
 */
public class RepairProductDealTask extends ScheduledTask {

    private int productId;//设备ID

    private int userId;//玩家ID

    public RepairProductDealTask(int productId, int userId) {
        super("报修设备后续处理定时器");
        this.productId = productId;
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            //推送公众号通知机修
            OfficialAccountUtil.sendOfficalAccount(productId);
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
    }
}
