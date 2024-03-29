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
 * 能量信息
 */
@Service
public class UserEnergyMsgApi extends SystemEventHandler2<Session> {
    protected UserEnergyMsgApi() {
        super(WebSocketCmd.C2S_ENERGY_MSG);
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
                //能量信息
                UserNoticePushUtil.userEnergyMsg(UserUtil.loadUserIdByToken(accessToken), 0);
            }
        });
        cachedPool.shutdown();
    }
}
