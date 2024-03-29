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
 * 刷新玩家能量定时器
 */
public class RefreshUserEnergyNoticeTask extends ScheduledTask {

    //玩家ID
    private int userId;

    //能量数
    private int energyNum;

    public RefreshUserEnergyNoticeTask(int userId, int energyNum) {
        super("刷新玩家能量定时器");
        this.userId = userId;
        this.energyNum = energyNum;
    }

    @Override
    public void run() {
        if(UserOnlineUtil.isOnline(userId)){
            sendNotice(userId, energyNum);
        }
    }

    /**
     * 通知
     */
    private static void sendNotice(int userId, int energyNum) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("dealUserId", userId);//玩家ID
        paramsMap.put("changeNum", energyNum);//变更数量
        paramsMap.put("platform", ConfigMsg.sysPlatform);//平台：内部
        //获取请求url
        String httpRequest = ParamsUtil.httpRequestProduct(NoticeHttpCmdName.REFRESH_USER_ENERGY_NOTICE, paramsMap);
        if (!StrUtil.checkEmpty(httpRequest)) {
            //发送请求
            HttpClientUtil.sendHttpGet(httpRequest);
        }
    }

}
