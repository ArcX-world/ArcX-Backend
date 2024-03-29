package avatar.task.product.general;

import avatar.global.lockMsg.LockMsg;
import avatar.service.jedis.RedisLock;
import avatar.util.basic.general.CheckUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.user.UserAttributeUtil;
import avatar.util.user.UserNoticePushUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 添加玩家游戏经验
 */
public class AddUserGameExpTask extends ScheduledTask {
    private int userId;//玩家ID

    private long coinNum;//游戏币数量

    public AddUserGameExpTask(int userId, long coinNum) {
        super("添加玩家游戏经验");
        this.userId = userId;
        this.coinNum = coinNum;
    }

    @Override
    public void run() {
        if(!CheckUtil.isSystemMaintain()) {
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ATTRIBUTE_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    UserAttributeUtil.addUserGameExp(userId, coinNum);
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
    }
}
