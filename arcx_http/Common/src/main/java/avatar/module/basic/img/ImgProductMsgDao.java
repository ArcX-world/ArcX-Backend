package avatar.module.basic.img;

import avatar.entity.basic.img.ImgProductMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**
 * 设备图片数据接口
 */
public class ImgProductMsgDao {
    private static final ImgProductMsgDao instance = new ImgProductMsgDao();
    public static final ImgProductMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public ImgProductMsgEntity loadByImgId(int imgId) {
        //从缓存获取
        ImgProductMsgEntity entity = loadCache(imgId);
        if(entity==null){
            //从数据库查询
            entity = loadDbById(imgId);
            if(entity!=null) {
                //设置缓存
                setCache(imgId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private ImgProductMsgEntity loadCache(int imgId){
        return (ImgProductMsgEntity) GameData.getCache().get(PrefixMsg.IMG_PRODUCT_MSG+"_"+imgId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int imgId, ImgProductMsgEntity entity){
        GameData.getCache().set(PrefixMsg.IMG_PRODUCT_MSG+"_"+imgId, entity);
    }

    //=========================db===========================

    /**
     * 根据ID查询
     */
    private ImgProductMsgEntity loadDbById(int id) {
        return GameData.getDB().get(ImgProductMsgEntity.class, id);
    }

}
