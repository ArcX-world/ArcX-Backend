package avatar.module.product.pileTower;

import avatar.entity.product.pileTower.ProductPileTowerUserMsgEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**
 * 设备炼金塔堆塔玩家信息数据接口
 */
public class ProductPileTowerUserMsgDao {
    private static final ProductPileTowerUserMsgDao instance = new ProductPileTowerUserMsgDao();
    public static final ProductPileTowerUserMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public ProductPileTowerUserMsgEntity loadByUserId(int userId) {
        //从缓存获取
        ProductPileTowerUserMsgEntity entity = loadCache(userId);
        if(entity==null){
            //查询数据库
            entity = loadDbByUserId(userId);
            if(entity!=null){
                //设置缓存
                setCache(userId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private ProductPileTowerUserMsgEntity loadCache(int userId){
        return (ProductPileTowerUserMsgEntity) GameData.getCache().get(ProductPrefixMsg.PRODUCT_PILE_TOWER_USER_MSG+"_"+userId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, ProductPileTowerUserMsgEntity entity){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_PILE_TOWER_USER_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private ProductPileTowerUserMsgEntity loadDbByUserId(int userId) {
        String sql = "select * from product_pile_tower_user_msg where user_id=? order by create_time desc";
        return GameData.getDB().get(ProductPileTowerUserMsgEntity.class, sql, new Object[]{userId});
    }

    /**
     * 添加数据
     */
    public boolean insert(ProductPileTowerUserMsgEntity entity){
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            //设置缓存
            setCache(entity.getUserId(), entity);
            return true;
        }
        return false;
    }
}
