package avatar.task.product.innoMsg;

import avatar.global.enumMsg.product.award.ProductAwardTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.innoMsg.SelfSpecialAwardMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.Map;

/**
 * 自研设备特殊奖项处理定时器
 */
public class InnoSpecialAwardTask extends ScheduledTask {

    //玩家ID
    private int userId;

    //设备ID
    private int productId;

    //设备奖励类型
    private int productAwardType;

    //是否开始
    private int isStart;

    public InnoSpecialAwardTask(int userId, int productId, int productAwardType, int isStart) {
        super("自研设备特殊奖项处理定时器");
        this.productId = productId;
        this.userId = userId;
        this.productAwardType = productAwardType;
        this.isStart = isStart;
    }

    @Override
    public void run() {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.INNO_SPECIAL_AWARD_LOCK + "_" + productId,
                2000);
        try {
            if (lock.lock()) {
                if(productAwardType== ProductAwardTypeEnum.AGYPT_OPEN_BOX.getCode()){
                    //埃及开箱子
                    LogUtil.getLogger().info("玩家{}在自研设备{}上埃及开箱子中奖{}-------", userId, productId,
                            ParamsUtil.isConfirm(isStart)?"开始":"结束");
                }else if(productAwardType== ProductAwardTypeEnum.WHISTLE.getCode()){
                    //小丑马戏团套圈
                    LogUtil.getLogger().info("玩家{}在自研设备{}上小丑马戏团动物套圈中奖{}-------", userId, productId,
                            ParamsUtil.isConfirm(isStart)?"开始":"结束");
                }else if(productAwardType== ProductAwardTypeEnum.PIRATE_CANNON.getCode()){
                    //海盗开炮
                    LogUtil.getLogger().info("玩家{}在自研设备{}上海盗开炮中奖{}-------", userId, productId,
                            ParamsUtil.isConfirm(isStart)?"开始":"结束");
                }
                Map<Integer, Long> awardTypeMap = SelfSpecialAwardMsgDao.getInstance().loadByProductId(productId);
                if(isStart== YesOrNoEnum.YES.getCode()){
                    awardTypeMap.put(productAwardType, TimeUtil.getNowTime());
                }else if(isStart== YesOrNoEnum.NO.getCode()){
                    awardTypeMap.remove(productAwardType);
                }
                //重置缓存
                SelfSpecialAwardMsgDao.getInstance().setCache(productId, awardTypeMap);
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }
}
