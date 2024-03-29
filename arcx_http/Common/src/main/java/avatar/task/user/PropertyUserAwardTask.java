package avatar.task.user;

import avatar.entity.basic.systemMsg.PropertyMsgEntity;
import avatar.global.enumMsg.basic.commodity.PropertyUseTypeEnum;
import avatar.module.basic.systemMsg.PropertyMsgDao;
import avatar.util.user.UserAttributeUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 使用道具奖励定时器
 */
public class PropertyUserAwardTask extends ScheduledTask {

    //玩家ID
    private int userId;

    //道具类型
    private int propertyType;

    public PropertyUserAwardTask(int userId, int propertyType) {
        super("使用道具奖励定时器");
        this.userId = userId;
        this.propertyType = propertyType;
    }

    @Override
    public void run() {
        //查询道具信息
        PropertyMsgEntity entity = PropertyMsgDao.getInstance().loadMsg(propertyType);
        if(entity!=null){
            int userType = entity.getPropertyUseType();//使用类型
            int num = entity.getNum();//奖励数量
            if(userType== PropertyUseTypeEnum.RESTORE_ENERGY.getCode() && num>0){
                //恢复能量
                UserAttributeUtil.addUserEnergy(userId, num);
            }
        }
    }

}
