package avatar.apiregister.websocket.product.notice;

import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.net.session.Session;
import avatar.util.basic.encode.WebsocketEncodeUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserNoticePushUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 设备彩票进度
 */
@Service
public class ProductLotteryProgressApi extends SystemEventHandler2<Session> {
    protected ProductLotteryProgressApi() {
        super(WebSocketCmd.C2S_PRODUCT_LOTTERY_PROGRESS);
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
                int productId = jsonObject.getInteger("devId");//设备ID
                UserNoticePushUtil.pushLotteryNotice(productId, ProductUtil.loadSecondType(productId),
                        UserUtil.loadUserIdByToken(accessToken), 0, 0);
            }
        });
        cachedPool.shutdown();
    }
}
