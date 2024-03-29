package avatar.apiregister.websocket.product.notice;

import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.net.session.Session;
import avatar.service.product.ProductWebsocketService;
import avatar.util.basic.encode.WebsocketEncodeUtil;
import avatar.util.product.ProductSocketUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.ForbidUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 进入设备
 */
@Service
public class JoinProductApi extends SystemEventHandler2<Session> {
    protected JoinProductApi() {
        super(WebSocketCmd.C2S_JOIN_PRODUCT);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        //逻辑处理
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            String accessToken = session.getAccessToken();//玩家通行证
            //前端传递的参数
            JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
            int status = WebsocketEncodeUtil.checkEncode(accessToken, false, jsonObject);
            if(ParamsUtil.isSuccess(status)) {
                int userId = UserUtil.loadUserIdByToken(accessToken);//玩家ID
                int productId = jsonObject.getInteger("devId");//设备ID
                if(userId>0){
                    status = ForbidUtil.checkForbidProduct(userId, productId);
                    if(ParamsUtil.isSuccess(status)) {
                        //刷新房间信息
                        ProductWebsocketService.joinProduct(userId, productId, true);
                    }
                }
                //处理进入设备（公共处理）
                ProductSocketUtil.dealJoinProduct(productId, session);
            }
        });
        cachedPool.shutdown();
    }

}
