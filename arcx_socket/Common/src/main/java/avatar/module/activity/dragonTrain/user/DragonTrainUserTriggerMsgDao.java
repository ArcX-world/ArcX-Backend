package avatar.module.activity.dragonTrain.user;

import avatar.entity.activity.dragonTrain.user.DragonTrainUserTriggerMsgEntity;
import avatar.util.GameData;

/**
 * 龙珠玛丽机玩家触发信息数据接口
 */
public class DragonTrainUserTriggerMsgDao {
    private static final DragonTrainUserTriggerMsgDao instance = new DragonTrainUserTriggerMsgDao();
    public static final DragonTrainUserTriggerMsgDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public DragonTrainUserTriggerMsgEntity insert(DragonTrainUserTriggerMsgEntity entity){
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            return entity;
        }else{
            return null;
        }
    }
}
