package avatar.apiregister.websocket.product.notice;

import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.service.product.ProductWebsocketService;
import avatar.util.basic.encode.WebsocketEncodeUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductSocketUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserOnlineUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 退出设备
 */
@Service
public class ExitProductApi extends SystemEventHandler2<Session> {
    protected ExitProductApi() {
        super(WebSocketCmd.C2S_EXIT_PRODUCT);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        //逻辑处理
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            //逻辑处理
            String accessToken = session.getAccessToken();//玩家通行证
            //前端传递的参数
            JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
            int status = WebsocketEncodeUtil.checkEncode(accessToken, false, jsonObject);
            if(ParamsUtil.isSuccess(status)) {
                int userId = UserUtil.loadUserIdByToken(accessToken);//玩家ID
                int productId = jsonObject.getInteger("devId");//设备ID
                if(userId>0) {
                    int pId = UserOnlineUtil.loadOnlineProduct(userId);//玩家所在设备
                    if (pId == productId) {
                        //退出设备
                        ProductWebsocketService.exitProduct(userId, productId);
                    }
                }
                //处理退出设备（公共处理）
                dealExitProduct(productId, session);
            }
        });
        cachedPool.shutdown();
    }

    /**
     * 处理退出设备
     */
    private void dealExitProduct(int productId, Session session) {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_SESSION_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                ProductSocketUtil.exitProduct(productId, session);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
