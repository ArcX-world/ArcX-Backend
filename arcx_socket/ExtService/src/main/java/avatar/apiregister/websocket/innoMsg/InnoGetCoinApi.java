package avatar.apiregister.websocket.innoMsg;

import avatar.data.product.innoMsg.InnoGetCoinMsg;
import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.SelfInnoWebsocketInnerCmd;
import avatar.net.session.Session;
import avatar.service.innoMsg.InnoProductService;
import avatar.util.innoMsg.InnerEnCodeUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.system.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自研到设备获得币（订阅）
 */
@Service
public class InnoGetCoinApi extends SystemEventHandler2<Session> {
    protected InnoGetCoinApi() {
        super(SelfInnoWebsocketInnerCmd.S2P_GET_COIN);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        //逻辑处理
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            //前端传递的参数
            JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
            if(InnerEnCodeUtil.checkEncode(jsonObject)) {
                //转换参数
//                LogUtil.getLogger().info("收到自研设备服务器发送的订阅获得币信息{}--------", JsonUtil.mapToJson(jsonObject));
                InnoGetCoinMsg innoGetCoinMsg = InnoParamsUtil.initInnoGetCoinMsg(jsonObject);
                //流程处理
                InnoProductService.describeGetCoinMsg(innoGetCoinMsg);
            }
        });
        cachedPool.shutdown();
    }
}
