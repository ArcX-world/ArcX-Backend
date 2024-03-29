package avatar.module.activity.dragonTrain.info;

import avatar.entity.activity.dragonTrain.info.DragonTrainWheelIconMsgEntity;
import avatar.global.prefixMsg.ActivityPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 龙珠玛丽机转轮图标信息数据接口
 */
public class DragonTrainWheelIconMsgDao {
    private static final DragonTrainWheelIconMsgDao instance = new DragonTrainWheelIconMsgDao();
    public static final DragonTrainWheelIconMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<DragonTrainWheelIconMsgEntity> loadMsg() {
        //从缓存获取
        List<DragonTrainWheelIconMsgEntity> list = loadCache();
        if(list==null){
            //从数据库查询
            list = loadDbMsg();
            //设置缓存
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<DragonTrainWheelIconMsgEntity> loadCache(){
        return (List<DragonTrainWheelIconMsgEntity>) GameData.getCache().get(ActivityPrefixMsg.DRAGON_TRAIN_WHEEL_ICON);
    }

    /**
     * 设置缓存
     */
    private void setCache(List<DragonTrainWheelIconMsgEntity> list){
        GameData.getCache().set(ActivityPrefixMsg.DRAGON_TRAIN_WHEEL_ICON, list);
    }

    //=========================db===========================

    /**
     * 查询配置信息
     */
    private List<DragonTrainWheelIconMsgEntity> loadDbMsg() {
        String sql = SqlUtil.loadList("dragon_train_wheel_icon_msg",
                Collections.singletonList("id")).toString();
        List<DragonTrainWheelIconMsgEntity> list = GameData.getDB().list(DragonTrainWheelIconMsgEntity.class,
                sql, new Object[]{});
        return list==null?new ArrayList<>():list;
    }
}
