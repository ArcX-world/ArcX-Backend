package avatar.apiregister.websocket.product.operate;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.facade.SystemEventHandler2;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.service.product.CoinPusherInnoService;
import avatar.service.product.ProductSocketOperateService;
import avatar.util.LogUtil;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.InnoProductUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 推币机操作接口
 */
@Service
public class CoinPusherOperationApi extends SystemEventHandler2<Session> {
    protected CoinPusherOperationApi() {
        super(WebSocketCmd.C2S_COIN_PUSHER_OPERATION);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            try {
                //逻辑处理
                String accessToken = session.getAccessToken();//玩家通行证
                //前端传递的参数
                JSONObject jsonObject = JsonUtil.bytesToJson(bytes);
                //推送前端的参数
                JSONObject dataJson = new JSONObject();
                //验证参数
                int status = CheckParamsUtil.checkProductOperation(accessToken, jsonObject, dataJson);
                //查询是否设备类型正确
                if(ParamsUtil.isSuccess(status)){
                    int productId = jsonObject.getInteger("devId");//设备ID
                    if(!ProductUtil.isSpecifyMachine(productId, ProductTypeEnum.PUSH_COIN_MACHINE.getCode())){
                        status = ClientCode.PRODUCT_TYPE_ERROR.getCode();//设备类型错误
                    }
                }
                //逻辑处理
                if(ParamsUtil.isSuccess(status)){
                    int userId = UserUtil.loadUserIdByToken(accessToken);//玩家ID
                    int productId = jsonObject.getInteger("devId");//设备ID
                    int operateState = jsonObject.getInteger("hdlTp");//操作状态
                    int coinMulti = jsonObject.containsKey("gdMul")?jsonObject.getInteger("gdMul"):0;//投币倍率
                    String version = jsonObject.containsKey("vsCd")?jsonObject.getString("vsCd"):"";//版本号
                    boolean unlockFlag = InnoProductUtil.isUnlockVersion(version);//是否不锁定限制
                    //获取设备锁
                    RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                            2000);
                    try {
                        if (lock.lock()) {
                            ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                            //检测是否操作正常
                            status = CoinPusherInnoService.checkOperate(userId, coinMulti, operateState, roomMsg, unlockFlag);
                            if(ParamsUtil.isSuccess(status)){
                                //更新投币倍率信息
                                if(coinMulti>0 && operateState== ProductOperationEnum.START_GAME.getCode()){
                                    ProductGamingUtil.updateUserStartGameMultiMsg(userId, coinMulti);
                                }
                                //设备操作
                                ProductSocketOperateService.coinPusherOperate(productId, operateState, userId);
                            }
                            if(!ParamsUtil.isSuccess(status) || operateState== ProductOperationEnum.OFF_LINE.getCode()) {
                                LogUtil.getLogger().error("玩家{}发送的推币机信息{}，错误码{}-----", userId,
                                        JsonUtil.mapToJson(jsonObject), status);
                            }
                        }
                    }catch (Exception e){
                        ErrorDealUtil.printError(e);
                    }finally {
                        lock.unlock();
                    }
                }else{
                    SendWebsocketMsgUtil.sendByAccessToken(WebSocketCmd.S2C_COIN_PUSHER_OPERATION, status,
                            accessToken, dataJson);
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            }
        });
        cachedPool.shutdown();
    }

}
