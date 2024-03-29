package avatar.apiregister.notice.user;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.code.basicConfig.ConfigMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.http.NoticeHttpCmdName;
import avatar.net.session.Session;
import avatar.util.GameData;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserNoticePushUtil;
import avatar.util.user.UserUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 刷新玩家余额通知
 */
@Service
public class RefreshUserBalanceNoticeApi extends SystemEventHttpHandler<Session> {
    protected RefreshUserBalanceNoticeApi() {
        super(NoticeHttpCmdName.REFRESH_USER_BALANCE_NOTICE);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), new HashMap<>());
            //逻辑处理
            try {
                int userId = ParamsUtil.intParmasNotNull(map, "dealUserId");//玩家ID
                String platform = ParamsUtil.stringParmasNotNull(map, "platform");//平台信息
                if (platform.equals(ConfigMsg.sysPlatform)) {
                    String accessToken = UserUtil.loadAccessToken(userId);//玩家通行证
                    if(!StrUtil.checkEmpty(accessToken)) {
                        Session userSession = GameData.getSessionManager().getSessionByAccesstoken(accessToken);
                        if (userSession != null) {
                            //刷新余额通知
                            UserNoticePushUtil.userBalancePush(userId);
                        }
                    }
                } else {
                    LogUtil.getLogger().info("接收到玩家{}刷新玩家余额通知的请求，但是平台信息不符合--------", userId);
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }
        });
        cachedPool.shutdown();
    }
}
