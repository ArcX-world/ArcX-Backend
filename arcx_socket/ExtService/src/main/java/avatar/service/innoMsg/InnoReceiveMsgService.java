package avatar.service.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.innoMsg.*;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.innoMsg.InnoProductOperateTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.product.info.ProductAliasDao;
import avatar.service.jedis.RedisLock;
import avatar.task.innoMsg.SyncInnoOccpuyCheckTask;
import avatar.task.product.innoMsg.InnoAwardLockDealTask;
import avatar.task.product.innoMsg.InnoAwardScoreMultiDealTask;
import avatar.task.product.innoMsg.InnoSettlementWindowTask;
import avatar.task.product.innoMsg.InnoVoiceNoticeTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.innoMsg.InnoProductOperateUtil;
import avatar.util.innoMsg.InnoSendWebsocketUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.product.ProductDealUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 收到自研设备的信息
 */
public class InnoReceiveMsgService {

    /**
     * 开始游戏
     */
    public static void startGame(Map<String, Object> jsonMap) {
        LogUtil.getLogger().info("接收到自研设备服务器的开始游戏信息，内容为{}------",
                JsonUtil.mapToJson(jsonMap));
        InnoReceiveStartGameMsg startGameMsg = InnoParamsUtil.startGameMsg(jsonMap);
        if(startGameMsg!=null){
            int status = InnoParamsUtil.loadClientCode(startGameMsg.getStatus());//转换错误码
            int userId = startGameMsg.getUserId();//玩家ID
            int productId = ProductAliasDao.getInstance().loadByAlias(startGameMsg.getAlias());//设备ID
            //获取设备锁
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                    2000);
            try {
                if (lock.lock()) {
                    if (!ParamsUtil.isSuccess(status)) {
                        //推送服务端
                        InnoSendWebsocketUtil.sendWebsocketMsg(userId,
                                ProductOperationEnum.START_GAME.getCode(), status, 0,
                                productId);
                    } else {
                        //走开始游戏流程
                        InnoProductOperateUtil.startGame(startGameMsg, productId);
                    }
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }finally {
                lock.unlock();
            }
        }
    }

    /**
     * 设备操作
     */
    public static void productOperate(Map<String, Object> jsonMap) {
        LogUtil.getLogger().info("接收到自研设备服务器的设备操作信息，内容为{}------",
                JsonUtil.mapToJson(jsonMap));
        InnoReceiveProductOperateMsg productOperateMsg = InnoParamsUtil.productOperateMsg(jsonMap);
        if(productOperateMsg!=null){
            int status = InnoParamsUtil.loadClientCode(productOperateMsg.getStatus());//转换错误码
            int operateType = productOperateMsg.getInnoProductOperateType();//操作类型
            int userId = productOperateMsg.getUserId();//玩家ID
            int productId = ProductAliasDao.getInstance().loadByAlias(productOperateMsg.getAlias());//设备ID
            if(!ParamsUtil.isSuccess(status) && InnoParamsUtil.loadProductOperateType(operateType)!=-1) {
                //投币失败，技能环节，返还技能或者不处理，正常环节，返回游戏币
                if(operateType== InnoProductOperateTypeEnum.PUSH_COIN.getCode()){
                    //投币失败处理
                    ProductDealUtil.pushCoinFailDeal(userId, productId, productOperateMsg);
                }
            }else{
                //获取设备锁
                RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                        2000);
                try {
                    if (lock.lock()) {
                        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                        if(roomMsg.getGamingUserId()==userId &&
                                roomMsg.getOnProductTime()==productOperateMsg.getOnProductTime()){
                            //走设备操作流程
                            InnoProductOperateUtil.productOperate(productOperateMsg, productId);
                        }else{
                            LogUtil.getLogger().info("接收到自研设备服务器{}的设备操作信息，但是上机时间或者上机玩家不一致，" +
                                            "当前上机时间{}---收到时间{}，当前上机玩家{}---收到玩家{}不处理后续流程------",
                                    productId, roomMsg.getOnProductTime(), productOperateMsg.getOnProductTime(),
                                    roomMsg.getGamingUserId(), userId);
                        }
                    }
                }catch (Exception e){
                    ErrorDealUtil.printError(e);
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 自研到设备开始游戏玩家校验
     */
    public static void startGmmeOccopyCheck(Map<String, Object> jsonMap) {
        LogUtil.getLogger().info("接收到自研设备服务器的开始游戏玩家校验信息，内容为{}------",
                JsonUtil.mapToJson(jsonMap));
        InnoStartGameOccupyMsg startGameOccopyMsg = InnoParamsUtil.startGameOccupyMsg(jsonMap);
        if(startGameOccopyMsg!=null) {
            int userId = startGameOccopyMsg.getUserId();//玩家ID
            int productId = ProductAliasDao.getInstance().loadByAlias(startGameOccopyMsg.getAlias());//设备ID
            //获取设备锁
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                    2000);
            try {
                if (lock.lock()) {
                    ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                    int gamingUserId = roomMsg.getGamingUserId();//游戏中的玩家ID
                    if(gamingUserId==0){
                        //推送自研设备清除玩家信息
                        LogUtil.getLogger().error("自研设备服务器推送设备{}开始游戏占用中的玩家校验，" +
                                "当前无在玩玩家，返回的玩家{}推送自研设备处理-----", productId, userId);
                        SchedulerSample.delayed(1, new SyncInnoOccpuyCheckTask(startGameOccopyMsg));
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 设备信息推送
     */
    public static void productMsg(Map<String, Object> jsonMap) {
        LogUtil.getLogger().info("接收到自研设备服务器的设备信息，内容为{}------",
                JsonUtil.mapToJson(jsonMap));
        String alias = jsonMap.get("alias").toString();//设备号
        int productId = ProductAliasDao.getInstance().loadByAlias(alias);
        if(productId>0) {
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                    2000);
            try {
                if (lock.lock()) {
                    //更新设备信息
                    ProductGamingUtil.updateGamingUserMsg(productId, InnoParamsUtil.initInnoProductMsg(jsonMap));
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 中奖锁处理信息
     */
    public static void awardLockDealMsg(Map<String, Object> jsonMap) {
        LogUtil.getLogger().info("接收到自研设备服务器的中奖锁信息，内容为{}------",
                JsonUtil.mapToJson(jsonMap));
        InnoAwardLockMsg innoAwardLockMsg = InnoParamsUtil.initInnoAwardLockMsg(jsonMap);
        int productId = ProductAliasDao.getInstance().loadByAlias(innoAwardLockMsg.getAlias());//设备ID
        //获取设备锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                2000);
        try {
            if (lock.lock()) {
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg.getGamingUserId()==innoAwardLockMsg.getUserId() &&
                        CrossServerMsgUtil.isArcxServer(innoAwardLockMsg.getServerSideType())){
                    SchedulerSample.delayed(1,
                            new InnoAwardLockDealTask(innoAwardLockMsg, productId));
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 结算窗口通知
     */
    public static void settlementWindowMsg(Map<String, Object> jsonMap) {
        LogUtil.getLogger().info("接收到自研设备服务器的结算窗口通知信息，内容为{}------",
                JsonUtil.mapToJson(jsonMap));
        InnoSettlementWindowMsg innoSettlementWindowMsg = InnoParamsUtil.initInnoSettlementWindowMsg(jsonMap);
        int productId = ProductAliasDao.getInstance().loadByAlias(innoSettlementWindowMsg.getAlias());//设备ID
        //获取设备锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                2000);
        try {
            if (lock.lock()) {
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg.getGamingUserId()==innoSettlementWindowMsg.getUserId() &&
                        CrossServerMsgUtil.isArcxServer(innoSettlementWindowMsg.getServerSideType())){
                    SchedulerSample.delayed(1,
                            new InnoSettlementWindowTask(innoSettlementWindowMsg, productId));
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 中奖得分倍数通知
     */
    public static void awardScoreMulti(Map<String, Object> jsonMap) {
        LogUtil.getLogger().info("接收到自研设备服务器的中奖得分倍数通知信息，内容为{}------",
                JsonUtil.mapToJson(jsonMap));
        InnoAwardScoreMultiMsg innoAwardScoreMultiMsg = InnoParamsUtil.initInnoAwardScoreMultiMsg(jsonMap);
        int productId = ProductAliasDao.getInstance().loadByAlias(innoAwardScoreMultiMsg.getAlias());//设备ID
        //获取设备锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                2000);
        try {
            if (lock.lock()) {
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg.getGamingUserId()==innoAwardScoreMultiMsg.getUserId() &&
                        CrossServerMsgUtil.isArcxServer(innoAwardScoreMultiMsg.getServerSideType())){
                    SchedulerSample.delayed(1,
                            new InnoAwardScoreMultiDealTask(innoAwardScoreMultiMsg, productId));
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设备声音通知
     */
    public static void voiceNotice(JSONObject jsonMap) {
        String alias = InnoParamsUtil.loadStringParams(jsonMap, "alias");
        if(!StrUtil.checkEmpty(alias)) {
            int productId = ProductAliasDao.getInstance().loadByAlias(alias);//设备ID
            if(productId>0) {
                SchedulerSample.delayed(1,
                        new InnoVoiceNoticeTask(jsonMap, productId));
            }
        }else{
            LogUtil.getLogger().error("接收到声音推送信息，但是解析不到对应的设备号---------");
        }
    }
}
