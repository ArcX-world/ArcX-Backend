package avatar.task.login;

import avatar.module.user.info.EmailUserDao;
import avatar.module.user.thirdpart.UserThirdPartUidMsgDao;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.Map;

/**
 * 登录后续处理定时器
 */
public class LoginMsgUpdateTask extends ScheduledTask {

    private int userId;//玩家ID

    private String thirdPartUid;//第三方唯一ID

    private String userIp;//玩家IP

    private Map<String, Object> paramsMap;//前端传的参数信息

    public LoginMsgUpdateTask(int userId, String thirdPartUid, String userIp, Map<String, Object> paramsMap) {
        super("登录注册后续处理定时器");
        this.userId = userId;
        this.thirdPartUid = thirdPartUid;
        this.userIp = userIp;
        this.paramsMap = paramsMap;
    }

    @Override
    public void run() {
        //更新玩家登录信息
        UserUtil.updateUserLoginMsg(userId, paramsMap);
        //添加第三方唯一ID
        if(!StrUtil.checkEmpty(thirdPartUid) && UserThirdPartUidMsgDao.getInstance().loadByUid(thirdPartUid)==0){
            UserUtil.addUserThirdPartMsg(userId, thirdPartUid);
        }
        if(!StrUtil.checkEmpty(userIp)) {
            //更新玩家IP信息
            UserUtil.updateIpMsg(userId, userIp);
        }
        //添加3.0账号
        SchedulerSample.delayed(1, new Web3AccountDealTask(userId));
    }
}
