package avatar.module.basic.img;

import avatar.entity.basic.img.ImgProductPresentMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**
 * 设备礼物图片
 */
public class ImgProductPresentDao {
    private static final ImgProductPresentDao instance = new ImgProductPresentDao();
    public static final ImgProductPresentDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public ImgProductPresentMsgEntity loadById(int id) {
        //从缓存获取
        ImgProductPresentMsgEntity entity = loadCache(id);
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
    private ImgProductPresentMsgEntity loadCache(int id){
        return (ImgProductPresentMsgEntity) GameData.getCache().get(PrefixMsg.IMG_PRODUCT_PRESENT+"_"+id);
    }

    /**
     * 添加缓存
     */
    private void setCache(int id, ImgProductPresentMsgEntity entity){
        GameData.getCache().set(PrefixMsg.IMG_PRODUCT_PRESENT+"_"+id, entity);
    }

    //=========================db===========================

    /**
     * 根据ID查询
     */
    public ImgProductPresentMsgEntity loadDbById(int id) {
        return GameData.getDB().get(ImgProductPresentMsgEntity.class, id);
    }

}
