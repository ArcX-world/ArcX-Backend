package avatar.task.product.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.innoMsg.InnoAwardScoreMultiMsg;
import avatar.global.enumMsg.product.innoMsg.InnoAwardScoreMultiEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 中奖得分倍数处理定时器
 */
public class InnoAwardScoreMultiDealTask extends ScheduledTask {

    //中奖得分倍数
    private InnoAwardScoreMultiMsg innoAwardScoreMultiMsg;

    //设备ID
    private int productId;

    public InnoAwardScoreMultiDealTask(InnoAwardScoreMultiMsg innoAwardScoreMultiMsg, int productId) {
        super("中奖得分倍数处理定时器");
        this.innoAwardScoreMultiMsg = innoAwardScoreMultiMsg;
        this.productId = productId;
    }

    @Override
    public void run() {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int userId = innoAwardScoreMultiMsg.getUserId();//玩家ID
                int awardMultiScore = innoAwardScoreMultiMsg.getAwardMulti();//中奖得分倍数
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg.getGamingUserId()==innoAwardScoreMultiMsg.getUserId() &&
                        CrossServerMsgUtil.isArcxServer(innoAwardScoreMultiMsg.getServerSideType())){
                    LogUtil.getLogger().info("推送玩家{}在设备{}上的中奖得分倍数{}--------", userId, productId,
                            InnoAwardScoreMultiEnum.getNameByCode(awardMultiScore));
                    //推送通知
                    pushNotice(innoAwardScoreMultiMsg);
                }else{
                    LogUtil.getLogger().error("设备{}中奖得分倍数处理定时器关闭，接收到的信息不是当前上机玩家---------", productId);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 推送通知
     */
    private void pushNotice(InnoAwardScoreMultiMsg innoAwardScoreMultiMsg) {
        int userId = innoAwardScoreMultiMsg.getUserId();//玩家ID
        if(UserOnlineUtil.isOnline(userId)){
            JSONObject dataJson = new JSONObject();
            dataJson.put("devId", productId);//设备ID
            dataJson.put("awdMt", innoAwardScoreMultiMsg.getAwardMulti());//中奖得分倍数
            //推送前端
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_AWARD_SCORE_MULTI_NOTICE,
                    ClientCode.SUCCESS.getCode(), userId, dataJson);
        }
    }
}
