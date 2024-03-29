package avatar.task.product.general;

import avatar.util.product.ProductOperateUtil;
import avatar.util.user.UserNoticePushUtil;
import avatar.util.user.UserOnlineUtil;
import avatar.util.user.UserUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 踢出设备
 */
public class UserKickOutProductNoticeTask extends ScheduledTask {
    private int userId;//玩家ID

    private int productId;//设备ID

    public UserKickOutProductNoticeTask(int userId, int productId) {
        super("踢出设备");
        this.userId = userId;
        this.productId = productId;
    }

    @Override
    public void run() {
        if(UserOnlineUtil.isOnline(userId) || !UserUtil.isIosUser(userId)) {
            //处理踢出设备
            ProductOperateUtil.kickOut(userId, productId);
        }else{
            //推送socket
            UserNoticePushUtil.kickOutProductPush(userId, productId);
        }
    }
}
