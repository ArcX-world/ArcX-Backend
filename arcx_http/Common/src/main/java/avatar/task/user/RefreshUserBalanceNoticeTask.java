package avatar.task.user;

import avatar.global.basicConfig.basic.ConfigMsg;
import avatar.global.linkMsg.NoticeHttpCmdName;
import avatar.util.system.HttpClientUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserOnlineUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.HashMap;
import java.util.Map;

/**
 * 刷新玩家余额定时器
 */
public class RefreshUserBalanceNoticeTask extends ScheduledTask {

    //玩家ID
    private int userId;

    public RefreshUserBalanceNoticeTask(int userId) {
        super("刷新玩家余额定时器");
        this.userId = userId;
    }

    @Override
    public void run() {
        if(UserOnlineUtil.isOnline(userId)){
            sendNotice(userId);
        }
    }

    /**
     * 通知
     */
    private static void sendNotice(int userId) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("dealUserId", userId);//玩家ID
        paramsMap.put("platform", ConfigMsg.sysPlatform);//平台：内部
        //获取请求url
        String httpRequest = ParamsUtil.httpRequestProduct(NoticeHttpCmdName.REFRESH_USER_BALANCE_NOTICE, paramsMap);
        if (!StrUtil.checkEmpty(httpRequest)) {
            //发送请求
            HttpClientUtil.sendHttpGet(httpRequest);
        }
    }

}
