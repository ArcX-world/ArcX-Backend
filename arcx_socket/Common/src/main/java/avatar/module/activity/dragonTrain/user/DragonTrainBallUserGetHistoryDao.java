package avatar.module.activity.dragonTrain.user;

import avatar.entity.activity.dragonTrain.user.DragonTrainBallUserGetHistoryEntity;
import avatar.util.GameData;

/**
 * 龙珠玛丽机龙珠玩家获得历史数据接口
 */
public class DragonTrainBallUserGetHistoryDao {
    private static final DragonTrainBallUserGetHistoryDao instance = new DragonTrainBallUserGetHistoryDao();
    public static final DragonTrainBallUserGetHistoryDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public boolean insert(DragonTrainBallUserGetHistoryEntity entity){
        return GameData.getDB().insert(entity);
    }
}
