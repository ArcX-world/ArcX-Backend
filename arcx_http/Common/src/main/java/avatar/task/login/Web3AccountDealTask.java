package avatar.task.login;

import avatar.global.lockMsg.LockMsg;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.StrUtil;
import avatar.util.thirdpart.GameShiftUtil;
import avatar.util.thirdpart.Web3Util;
import avatar.util.user.UserUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * web3账号处理定时器
 */
public class Web3AccountDealTask extends ScheduledTask {

    private int userId;//玩家ID

    public Web3AccountDealTask(int userId) {
        super("登录注册后续处理定时器");
        this.userId = userId;
    }

    @Override
    public void run() {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.WEB3_LOCK + "_" + userId,
                500);
        try {
            if (lock.lock()) {
                String email = UserUtil.loadUserEmail(userId);
                if(!StrUtil.checkEmpty(email)) {
                    //添加gameshift账号
                    GameShiftUtil.addGameShiftAccount(userId, email);
                }
                //添加axc代币账号+钱包
//                Web3Util.addAxcAccount(userId);
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }
}
