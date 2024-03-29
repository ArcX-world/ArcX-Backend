package avatar.module.basic.img;

import avatar.entity.basic.img.ImgNftEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**
 * NFT图片数据接口
 */
public class ImgNftDao {
    private static final ImgNftDao instance = new ImgNftDao();
    public static final ImgNftDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public ImgNftEntity loadByImgId(int imgId) {
        //从缓存获取
        ImgNftEntity entity = loadCache(imgId);
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
    private ImgNftEntity loadCache(int imgId){
        return (ImgNftEntity) GameData.getCache().get(PrefixMsg.IMG_NFT+"_"+imgId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int imgId, ImgNftEntity entity){
        GameData.getCache().set(PrefixMsg.IMG_NFT+"_"+imgId, entity);
    }

    //=========================db===========================

    /**
     * 根据ID查询
     */
    private ImgNftEntity loadDbById(int id) {
        return GameData.getDB().get(ImgNftEntity.class, id);
    }

}
