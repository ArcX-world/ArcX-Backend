package avatar.module.product.innoMsg;

import avatar.entity.product.innoMsg.InnoProductWinPrizeMsgEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.TimeUtil;

/**
 * 自研设备获奖信息信息数据接口
 */
public class InnoProductWinPrizeMsgDao {
    private static final InnoProductWinPrizeMsgDao instance = new InnoProductWinPrizeMsgDao();
    public static final InnoProductWinPrizeMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public InnoProductWinPrizeMsgEntity loadMsg(int userId, int productId, int productAwardType) {
        //从缓存获取
        InnoProductWinPrizeMsgEntity entity = loadCache(userId, productId, productAwardType);
        if(entity==null){
            //从数据库查询
            entity = loadDbMsg(userId, productId, productAwardType);
            if(entity!=null) {
                //设置缓存
                setCache(userId, productId, productAwardType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private InnoProductWinPrizeMsgEntity loadCache(int userId, int productId, int productAwardType){
        return (InnoProductWinPrizeMsgEntity) GameData.getCache().get(
                ProductPrefixMsg.INNO_USER_WIN_PRIZE+"_"+productId+"_"+userId+"_"+productAwardType);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, int productId, int productAwardType, InnoProductWinPrizeMsgEntity entity){
        GameData.getCache().set(ProductPrefixMsg.INNO_USER_WIN_PRIZE+"_"+productId+"_"+userId+"_"+productAwardType, entity);
    }

    //=========================db===========================

    /**
     * 根据ID查询
     */
    public InnoProductWinPrizeMsgEntity loadDbById(long awardId) {
        return GameData.getDB().get(InnoProductWinPrizeMsgEntity.class, awardId);
    }

    /**
     * 根据信息查询
     */
    private InnoProductWinPrizeMsgEntity loadDbMsg(int userId, int productId, int awardType) {
        String sql = "select * from inno_product_win_prize_msg where user_id=? and product_id=? " +
                "and award_type=? order by create_time desc ";
        return GameData.getDB().get(InnoProductWinPrizeMsgEntity.class, sql, new Object[]{userId, productId, awardType});
    }

    /**
     * 更新
     */
    public void update(InnoProductWinPrizeMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //设置缓存
            setCache(entity.getUserId(), entity.getProductId(), entity.getAwardType(), entity);
        }
    }

    /**
     * 添加数据
     */
    public long insert(InnoProductWinPrizeMsgEntity entity){
        long id = GameData.getDB().insertAndReturn(entity);
        if(id>0) {
            entity.setId(id);
            //设置缓存
            setCache(entity.getUserId(), entity.getProductId(), entity.getAwardType(), entity);
        }
        return id;
    }
}
