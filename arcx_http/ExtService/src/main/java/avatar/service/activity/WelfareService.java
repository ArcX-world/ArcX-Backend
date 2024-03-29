package avatar.service.activity;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.global.lockMsg.LockMsg;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.activity.WelfareUtil;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 福利接口实现类
 */
public class WelfareService {
    /**
     * 签到信息
     */
    public static void signMsg(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            int userId = ParamsUtil.userId(map);//玩家ID
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.WELFARE_AWARD_LOCK+"_"+userId,
                    2000);
            try {
                if (lock.lock()) {
                    //签到信息
                    WelfareUtil.signMsg(userId, dataMap);
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }finally {
                lock.unlock();
            }
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 领取签到奖励
     */
    public static void receiveSignAward(Map<String, Object> map, Session session) {
        List<GeneralAwardMsg> retList = new ArrayList<>();
        //检测参数
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.WELFARE_AWARD_LOCK+"_"+userId,
                    2000);
            try {
                if (lock.lock()) {
                    status = WelfareUtil.receiveSignBonus(userId, retList);
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
    }
}
