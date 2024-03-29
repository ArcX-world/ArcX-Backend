package avatar.task.login;

import avatar.module.user.info.UserRegisterIpDao;
import avatar.module.user.thirdpart.UserThirdPartUidMsgDao;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.Map;

/**
 * 登录注册后续处理定时器
 */
public class LoginRegisterDealTask extends ScheduledTask {

    private int userId;//玩家ID

    private String userIp;//玩家IP

    private String mac;//设备唯一ID

    private String thirdPartUid;//第三方唯一ID

    private Map<String, Object> paramsMap;//前端传的参数信息

    public LoginRegisterDealTask(int userId, String userIp, String mac, String thirdPartUid, Map<String, Object> paramsMap) {
        super("登录注册后续处理定时器");
        this.userId = userId;
        this.userIp = userIp;
        this.mac = mac;
        this.thirdPartUid = thirdPartUid;
        this.paramsMap = paramsMap;
    }

    @Override
    public void run() {
        try {
            if (!StrUtil.checkEmpty(userIp)) {
                //添加玩家注册IP信息
                UserRegisterIpDao.getInstance().insert(UserUtil.initUserRegisterIpEntity(userId, userIp, paramsMap));
            }
            //添加设备唯一ID信息
            if (!StrUtil.checkEmpty(mac)) {
                UserUtil.addRegisterUserMacMsg(userId, mac);
            }
            //添加第三方唯一ID
            if (!StrUtil.checkEmpty(thirdPartUid) && UserThirdPartUidMsgDao.getInstance().loadByUid(thirdPartUid) == 0) {
                UserUtil.addUserThirdPartMsg(userId, thirdPartUid);
            }
            //添加注册日志
            UserOperateLogUtil.register(userId);
            //添加注册福利
            UserUtil.addRegisterWelfare(userId);
            //添加3.0账号
            SchedulerSample.delayed(1, new Web3AccountDealTask(userId));
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
    }
}
