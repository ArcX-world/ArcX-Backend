package avatar.apiregister.websocket.normalMsg;

import avatar.data.product.general.ResponseGeneralMsg;
import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.net.session.Session;
import avatar.service.normal.InnerProductService;
import avatar.util.LogUtil;
import avatar.util.innoMsg.InnerEnCodeUtil;
import avatar.util.normalProduct.InnerNormalProductUtil;
import avatar.util.system.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 设备到服务端设备信息
 */
@Service
public class InnerProductMsgApi extends SystemEventHandler2<Session> {
    protected InnerProductMsgApi() {
        super(WebsocketInnerCmd.S2C_PRODUCT_MSG);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        //逻辑处理
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            //前端传递的参数
            JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
            //转换参数
            if(InnerEnCodeUtil.checkEncode(jsonObject)){
                LogUtil.getLogger().info("收到普通设备服务器发送的订阅设备信息{}--------", JsonUtil.mapToJson(jsonObject));
                ResponseGeneralMsg responseGeneralMsg = InnerNormalProductUtil.initResponseGeneralMsg(jsonObject);
                //流程处理
                InnerProductService.describeProductMsg(responseGeneralMsg);
            }
        });
        cachedPool.shutdown();
    }
}
