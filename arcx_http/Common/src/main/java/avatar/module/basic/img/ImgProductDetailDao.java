package avatar.module.basic.img;

import avatar.entity.basic.img.ImgProductDetailEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**
 * 设备详情
 */
public class ImgProductDetailDao {
    private static final ImgProductDetailDao instance = new ImgProductDetailDao();
    public static final ImgProductDetailDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public ImgProductDetailEntity loadById(int id) {
        //从缓存获取
        ImgProductDetailEntity entity = loadCache(id);
        if(entity==null){
            //从数据库查询
            entity = loadDbById(id);
            if(entity!=null) {
                //设置缓存
                setCache(id, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private ImgProductDetailEntity loadCache(int id){
        return (ImgProductDetailEntity) GameData.getCache().get(PrefixMsg.IMG_PRODUCT_DETAIL+"_"+id);
    }

    /**
     * 添加缓存
     */
    private void setCache(int id, ImgProductDetailEntity entity){
        GameData.getCache().set(PrefixMsg.IMG_PRODUCT_DETAIL+"_"+id, entity);
    }

    //=========================db===========================

    /**
     * 根据ID查询
     */
    public ImgProductDetailEntity loadDbById(int id) {
        return GameData.getDB().get(ImgProductDetailEntity.class, id);
    }

}
