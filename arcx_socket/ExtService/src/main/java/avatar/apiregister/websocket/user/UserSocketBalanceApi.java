package avatar.apiregister.websocket.user;

import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.net.session.Session;
import avatar.util.basic.encode.WebsocketEncodeUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserNoticePushUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 玩家余额信息
 */
@Service
public class UserSocketBalanceApi extends SystemEventHandler2<Session> {
    protected UserSocketBalanceApi() {
        super(WebSocketCmd.C2S_USER_BALANCE);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        //逻辑处理
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            String accessToken = session.getAccessToken();//玩家通行证
            //前端传递的参数
            JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
            int status = WebsocketEncodeUtil.checkEncode(accessToken, true, jsonObject);
            if(ParamsUtil.isSuccess(status)) {
                //余额信息
                UserNoticePushUtil.userBalancePush(UserUtil.loadUserIdByToken(accessToken));
            }
        });
        cachedPool.shutdown();
    }
}
