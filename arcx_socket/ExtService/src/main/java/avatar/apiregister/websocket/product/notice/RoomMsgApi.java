package avatar.apiregister.websocket.product.notice;

import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.net.session.Session;
import avatar.service.product.ProductWebsocketService;
import avatar.task.product.general.RefreshProductMsgTask;
import avatar.util.basic.encode.WebsocketEncodeUtil;
import avatar.util.product.ProductSocketUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserOnlineUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 房间信息
 */
@Service
public class RoomMsgApi extends SystemEventHandler2<Session> {
    protected RoomMsgApi() {
        super(WebSocketCmd.C2S_ROOM_MSG);
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
                int productId = jsonObject.getInteger("devId");//设备ID
                int userId = UserUtil.loadUserIdByToken(accessToken);//玩家ID
                int pId = userId>0? UserOnlineUtil.loadOnlineProduct(userId):0;//玩家所在设备ID
                //处理进入设备（公共处理）
                ProductSocketUtil.dealJoinProduct(productId, session);
                if(pId==0){
                    //走加入房间流程
                    ProductWebsocketService.joinProduct(userId, productId, false);
                }else {
                    //刷新房间信息
                    SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
                }

            }
        });
        cachedPool.shutdown();
    }
}
