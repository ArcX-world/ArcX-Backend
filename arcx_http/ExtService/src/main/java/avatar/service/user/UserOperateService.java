package avatar.service.user;

import avatar.global.lockMsg.LockMsg;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.checkParams.UserCheckParamsUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserAttributeUtil;
import avatar.util.user.UserPropertyUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家操作接口实现类
 */
public class UserOperateService {
    /**
     * 属性升级
     */
    public static void upgradeAttribute(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = UserCheckParamsUtil.upgradeAttribute(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            int attributeType = ParamsUtil.intParmasNotNull(map, "atbTp");//属性类型
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ATTRIBUTE_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    status = UserAttributeUtil.checkUpgradeAttribute(userId, attributeType);
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
     * 使用道具
     */
    public static void useProperty(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = UserCheckParamsUtil.useProperty(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            int propertyType = ParamsUtil.intParmasNotNull(map, "pptTp");//道具类型
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PROPERTY_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    status = UserPropertyUtil.useProperty(userId, propertyType);
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
}
