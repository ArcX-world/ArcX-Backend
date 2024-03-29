package avatar.module.activity.dragonTrain.user;

import avatar.entity.activity.dragonTrain.user.DragonTrainTriggerAwardMsgEntity;
import avatar.util.GameData;

import java.util.List;

/**
 * 龙珠玛丽机触发中奖信息数据接口
 */
public class DragonTrainUserTriggerAwardMsgDao {
    private static final DragonTrainUserTriggerAwardMsgDao instance = new DragonTrainUserTriggerAwardMsgDao();
    public static final DragonTrainUserTriggerAwardMsgDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public boolean insert(List<DragonTrainTriggerAwardMsgEntity> list){
        return GameData.getDB().insert(list);
    }
}
