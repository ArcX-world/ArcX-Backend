package avatar.apiregister.websocket.normalMsg;

import avatar.data.product.general.ResponseGeneralMsg;
import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.net.session.Session;
import avatar.service.normal.InnerProductService;
import avatar.util.innoMsg.InnerEnCodeUtil;
import avatar.util.normalProduct.InnerNormalProductUtil;
import avatar.util.system.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 设备到服务端获得币
 */
@Service
public class InnerGetCoinApi extends SystemEventHandler2<Session> {
    protected InnerGetCoinApi() {
        super(WebsocketInnerCmd.S2C_GET_COIN);
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
//                LogUtil.getLogger().info("收到普通设备服务器发送的订阅获得币信息{}--------", JsonUtil.mapToJson(jsonObject));
                ResponseGeneralMsg responseGeneralMsg = InnerNormalProductUtil.initResponseGeneralMsg(jsonObject);
                int result = jsonObject.get("retNum")==null?0:(int) jsonObject.get("retNum");//获得币结果
                //流程处理
                InnerProductService.describeGetCoinMsg(responseGeneralMsg, result);
            }
        });
        cachedPool.shutdown();
    }
}
