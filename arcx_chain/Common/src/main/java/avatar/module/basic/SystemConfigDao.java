package avatar.module.basic;

import avatar.entity.basic.systemConfig.SystemConfigEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;

/**
 * 系统配置信息数据接口
 */
public class SystemConfigDao {
    private static final SystemConfigDao instance = new SystemConfigDao();
    public static final SystemConfigDao getInstance() {
        return instance;
    }

    /**
     * 查询系统配置信息
     */
    public SystemConfigEntity loadMsg() {
        //从缓存获取
        SystemConfigEntity entity = loadCache();
        if (entity==null) {
            //查询数据库
            entity = loadDbMsg();
            //设置缓存
            if (entity != null) {
                setCache(entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private SystemConfigEntity loadCache() {
        return (SystemConfigEntity)
                GameData.getCache().get(PrefixMsg.SYSTEM_CONFIG);
    }

    /**
     * 设置缓存
     *
     */
    private void setCache(SystemConfigEntity entity) {
        GameData.getCache().set(PrefixMsg.SYSTEM_CONFIG, entity);
    }

    //=========================db===========================

    /**
     * 查询配置信息
     */
    private SystemConfigEntity loadDbMsg() {
        String sql = SqlUtil.getSql("system_config", new HashMap<>(), new HashMap<>(),
                new HashMap<>(), new HashMap<>()).toString();
        return GameData.getDB().get(SystemConfigEntity.class, sql, new Object[]{});
    }

}
