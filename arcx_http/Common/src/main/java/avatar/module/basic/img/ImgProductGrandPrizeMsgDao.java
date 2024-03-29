package avatar.module.basic.img;

import avatar.entity.basic.img.ImgProductGrandPrizeMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.Collections;
import java.util.List;

/**
 * 设备大奖图片数据接口
 */
public class ImgProductGrandPrizeMsgDao {
    private static final ImgProductGrandPrizeMsgDao instance = new ImgProductGrandPrizeMsgDao();
    public static final ImgProductGrandPrizeMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<ImgProductGrandPrizeMsgEntity> loadAll() {
        //从缓存获取
        List<ImgProductGrandPrizeMsgEntity> list = loadCache();
        if(list==null){
            //从数据库查询
            list = loadDbAll();
            //设置缓存
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<ImgProductGrandPrizeMsgEntity> loadCache(){
        return (List<ImgProductGrandPrizeMsgEntity>) GameData.getCache().get(PrefixMsg.IMG_PRODUCT_GRAND_PRIZE_MSG);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<ImgProductGrandPrizeMsgEntity> entity){
        GameData.getCache().set(PrefixMsg.IMG_PRODUCT_GRAND_PRIZE_MSG, entity);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private List<ImgProductGrandPrizeMsgEntity> loadDbAll() {
        String sql = SqlUtil.loadList("img_product_grand_prize_msg", Collections.singletonList("sequence")).toString();
        return GameData.getDB().list(ImgProductGrandPrizeMsgEntity.class, sql, new Object[]{});
    }
}
