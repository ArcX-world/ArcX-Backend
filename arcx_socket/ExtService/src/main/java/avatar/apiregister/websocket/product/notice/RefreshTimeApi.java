package avatar.apiregister.websocket.product.notice;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.basic.encode.WebsocketEncodeUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductDealUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 刷新时间
 */
@Service
public class RefreshTimeApi extends SystemEventHandler2<Session> {
    protected RefreshTimeApi() {
        super(WebSocketCmd.C2S_REFRESH_TIME);
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
                int userId = UserUtil.loadUserIdByToken(accessToken);//玩家ID
                //查询设备信息
                RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                        2000);
                try {
                    if (lock.lock()) {
                        //查询设备信息
                        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                        if(productRoomMsg!=null && productRoomMsg.getGamingUserId()==userId){
                            //推送刷新
                            ProductDealUtil.productRefreshTime(userId, productId, productRoomMsg.getPushCoinOnTime());
                        }
                    }
                }catch (Exception e){
                    ErrorDealUtil.printError(e);
                }finally {
                    lock.unlock();
                }
            }
        });
        cachedPool.shutdown();
    }
}
