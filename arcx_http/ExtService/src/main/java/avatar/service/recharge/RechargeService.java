package avatar.service.recharge;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.lockMsg.LockMsg;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.checkParams.RechargeCheckParamsUtil;
import avatar.util.recharge.RechargePropertyUtil;
import avatar.util.recharge.RechargeUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.ParamsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 充值接口实现类
 */
public class RechargeService {

    /**
     * 商城中心
     */
    public static void shoppingMall(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        int userId = ParamsUtil.userId(map);//玩家ID
        //填充信息
        RechargeUtil.shoppingMall(userId, dataMap);
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), dataMap);
    }

    /**
     * 刷新商城道具
     */
    public static void refreshMallProperty(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = CheckParamsUtil.checkAccessToken(map);
        if (ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PROPERTY_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    //填充信息
                    status = RechargePropertyUtil.refreshMallProperty(userId);
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 商品直购
     */
    public static void commodityDirectPurchase(Map<String, Object> map, Session session) {
        List<GeneralAwardMsg> retList = new ArrayList<>();
        //检测参数
        int status = RechargeCheckParamsUtil.commodityDirectPurchase(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.RECHARGE_LOCK+"_"+userId,
                    2000);
            try {
                if (lock.lock()) {
                    status = RechargeUtil.commodityDirectPurchase(map, retList);
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }finally {
                lock.unlock();
            }
        }
        //传输的jsonMap，先填充list
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("serverTbln", retList);
        //推送结果
        SendMsgUtil.sendBySessionAndList(session, status, jsonMap);
        LogUtil.getLogger().error("商品直购返回信息{}-----", JsonUtil.mapToJson(jsonMap));
    }
}
