package avatar.module.product.innoMsg;

import avatar.entity.product.innoMsg.InnoPushCoinWindowMsgEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**
 * 自研设备投币倍率窗口数据接口
 */
public class InnoPushCoinWindowDao {
    private static final InnoPushCoinWindowDao instance = new InnoPushCoinWindowDao();
    public static final InnoPushCoinWindowDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public InnoPushCoinWindowMsgEntity loadBySecondType(int secondType) {
        //从缓存获取
        InnoPushCoinWindowMsgEntity entity = loadCache(secondType);
        if(entity==null){
            //从数据库查询
            entity = loadDbBySecondType(secondType);
            if(entity!=null){
                //设置缓存
                setCache(secondType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private InnoPushCoinWindowMsgEntity loadCache(int secondType){
        return (InnoPushCoinWindowMsgEntity) GameData.getCache().get(
                ProductPrefixMsg.INNO_PRODUCT_PUSH_COIN_WINDOW+"_"+secondType);
    }

    /**
     * 添加缓存
     */
    private void setCache(int secondType, InnoPushCoinWindowMsgEntity entity){
        GameData.getCache().set(ProductPrefixMsg.INNO_PRODUCT_PUSH_COIN_WINDOW+"_"+secondType, entity);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private InnoPushCoinWindowMsgEntity loadDbBySecondType(int secondType) {
        String sql = "select * from inno_push_coin_window_msg where second_type=?";
        return GameData.getDB().get(InnoPushCoinWindowMsgEntity.class, sql, new Object[]{secondType});
    }

}
