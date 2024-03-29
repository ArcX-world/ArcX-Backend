package avatar.task.product.general;

import avatar.global.code.basicConfig.ConfigMsg;
import avatar.module.product.gaming.UserJoinProductTimeDao;
import avatar.util.LogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.TimeUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 进入设备通知定时器
 */
public class PushJoinProductTask extends ScheduledTask {

    private int productId;//设备ID

    private int userId;//玩家ID

    public PushJoinProductTask(int productId, int userId) {
        super("进入设备通知定时器");
        this.productId = productId;
        this.userId = userId;
    }

    @Override
    public void run() {
        long time = UserJoinProductTimeDao.getInstance().loadCache(userId, productId);
        if(time==0 || (TimeUtil.getNowTime()-time)>=ConfigMsg.joinProductTime*1000) {
            LogUtil.getLogger().info("推送玩家{}进入设备{}的信息--------", userId, productId);
            //推送通知
            UserJoinProductTimeDao.getInstance().setCache(userId, productId, TimeUtil.getNowTime());
            //进入设备
            ProductUtil.joinProductNotice(productId, userId);
        }else{
            LogUtil.getLogger().info("玩家{}进入设备{}的时间间隔低于设定的时间，不推送----------", userId, productId);
        }
    }
}
