package avatar.apiregister.websocket.product.operate;

import avatar.data.product.gamingMsg.DollGamingMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.facade.SystemEventHandler2;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.DollGamingMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.service.product.CatchDollService;
import avatar.service.product.ProductSocketOperateService;
import avatar.util.LogUtil;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
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
 * 抓娃娃操作
 */
@Service
public class CatchDollOperationApi extends SystemEventHandler2<Session> {
    protected CatchDollOperationApi() {
        super(WebSocketCmd.C2S_DOLL_MACHINE_OPERATION);
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
                    if(!ProductUtil.isSpecifyMachine(productId, ProductTypeEnum.DOLL_MACHINE.getCode())){
                        status = ClientCode.PRODUCT_TYPE_ERROR.getCode();//设备类型错误
                    }
                }
                //逻辑处理
                if(ParamsUtil.isSuccess(status)){
                    int userId = UserUtil.loadUserIdByToken(accessToken);//玩家ID
                    int productId = jsonObject.getInteger("devId");//设备ID
                    int operateState = jsonObject.getInteger("hdlTp");//操作状态
                    //获取设备锁
                    RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                            2000);
                    try {
                        if (lock.lock()) {
                            DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
                            ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                            if(gamingMsg.isInitalization()){
                                LogUtil.getLogger().info("娃娃机设备{}正在下爪初始化过程中，不处理其他指令--------",productId);
                            }else {
                                //检测是否操作正常
                                status = CatchDollService.checkOperate(operateState, userId, productRoomMsg);
                                if (ParamsUtil.isSuccess(status)) {
                                    //设备操作
                                    ProductSocketOperateService.catchDollOperate(productId, operateState, userId);
                                }
                                LogUtil.getLogger().info("玩家{}在娃娃机设备{}上做{}操作，结果为{}-------", userId, productId,
                                        ProductOperationEnum.loadByCode(operateState), status);
                            }
                        }
                    }catch (Exception e){
                        ErrorDealUtil.printError(e);
                    }finally {
                        lock.unlock();
                    }
                }else{
                    SendWebsocketMsgUtil.sendByAccessToken(WebSocketCmd.S2C_DOLL_MACHINE_OPERATION, status,
                            accessToken, dataJson);
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            }
        });
        cachedPool.shutdown();
    }

}
