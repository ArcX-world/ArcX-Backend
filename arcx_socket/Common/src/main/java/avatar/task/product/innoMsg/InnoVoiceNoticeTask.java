package avatar.task.product.innoMsg;

import avatar.global.lockMsg.LockMsg;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.product.ProductSocketUtil;
import avatar.util.trigger.SchedulerSample;
import com.alibaba.fastjson.JSONObject;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.List;

/**
 * 设备声音通知处理定时器
 */
public class InnoVoiceNoticeTask extends ScheduledTask {

    //声音通知信息
    private JSONObject jsonMap;

    //设备ID
    private int productId;

    public InnoVoiceNoticeTask(JSONObject jsonMap, int productId) {
        super("设备声音通知处理定时器");
        this.jsonMap = jsonMap;
        this.productId = productId;
    }

    @Override
    public void run() {
//        LogUtil.getLogger().info("推送在设备{}上的声音通知，内容信息{}--------",
//                productId, JsonUtil.mapToJson(jsonMap));
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_SESSION_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                //获取设备的session信息，直接推送
                List<Session> onlineSessionList = ProductSocketUtil.dealOnlineSession(productId);
                if(onlineSessionList.size()>0){
                    JSONObject clientMsg = InnoParamsUtil.loadClientDirectMsg(jsonMap);
                    for(Session sessionMsg : onlineSessionList){
                        //推送声音
                        SchedulerSample.delayed(1,
                                new PushSessionVoiceNoticeTask(clientMsg, productId, sessionMsg));
                    }
                }

            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

}
