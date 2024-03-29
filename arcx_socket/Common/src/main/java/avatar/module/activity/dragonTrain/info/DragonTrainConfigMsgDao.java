package avatar.module.activity.dragonTrain.info;

import avatar.entity.activity.dragonTrain.info.DragonTrainConfigMsgEntity;
import avatar.global.prefixMsg.ActivityPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;

/**
 * 龙珠玛丽机配置信息数据接口
 */
public class DragonTrainConfigMsgDao {
    private static final DragonTrainConfigMsgDao instance = new DragonTrainConfigMsgDao();
    public static final DragonTrainConfigMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public DragonTrainConfigMsgEntity loadMsg() {
        //从缓存获取
        DragonTrainConfigMsgEntity entity = loadCache();
        if(entity==null){
            //从数据库查询
            entity = loadDbMsg();
            if(entity!=null) {
                //设置缓存
                setCache(entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private DragonTrainConfigMsgEntity loadCache(){
        return (DragonTrainConfigMsgEntity) GameData.getCache().get(ActivityPrefixMsg.DRAGON_TRAIN_CONFIG);
    }

    /**
     * 设置缓存
     */
    private void setCache(DragonTrainConfigMsgEntity entity){
        GameData.getCache().set(ActivityPrefixMsg.DRAGON_TRAIN_CONFIG, entity);
    }

    //=========================db===========================

    /**
     * 查询配置信息
     */
    private DragonTrainConfigMsgEntity loadDbMsg() {
        String sql = SqlUtil.getSql("dragon_train_config_msg", new HashMap<>()).toString();
        return GameData.getDB().get(DragonTrainConfigMsgEntity.class, sql, new Object[]{});
    }
}
