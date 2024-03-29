package avatar.apiregister.notice.user;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.code.basicConfig.ConfigMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.http.NoticeHttpCmdName;
import avatar.net.session.Session;
import avatar.task.user.SystemNoticeNoticeTask;
import avatar.util.GameData;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 系统通知
 */
@Service
public class SystemNoticeApi extends SystemEventHttpHandler<Session> {
    protected SystemNoticeApi() {
        super(NoticeHttpCmdName.SYSTEM_NOTICE);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            //逻辑处理
            try {
                int userId = ParamsUtil.intParmasNotNull(map, "dealUserId");//玩家ID
                String content = ParamsUtil.stringParmasNotNull(map, "content");//通知内容
                String platform = ParamsUtil.stringParmasNotNull(map, "platform");//平台信息
                if (platform.equals(ConfigMsg.sysPlatform)) {
                    String accessToken = UserUtil.loadAccessToken(userId);//玩家通行证
                    if(!StrUtil.checkEmpty(accessToken)) {
                        Session userSession = GameData.getSessionManager().getSessionByAccesstoken(accessToken);
                        if (userSession != null) {
                            SchedulerSample.delayed(10, new SystemNoticeNoticeTask(userId, content));
                        }
                    }
                } else {
                    LogUtil.getLogger().info("接收到玩家{}系统通知的请求，但是平台信息不符合--------", userId);
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }
            SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), new HashMap<>());
        });
        cachedPool.shutdown();
    }
}
