package avatar.module.basic.img;

import avatar.entity.basic.img.ImgAwardMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**
 * 奖励图片数据接口
 */
public class ImgAwardMsgDao {
    private static final ImgAwardMsgDao instance = new ImgAwardMsgDao();
    public static final ImgAwardMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public ImgAwardMsgEntity loadByImgId(int imgId) {
        //从缓存获取
        ImgAwardMsgEntity entity = loadCache(imgId);
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
    private ImgAwardMsgEntity loadCache(int imgId){
        return (ImgAwardMsgEntity) GameData.getCache().get(PrefixMsg.IMG_AWARD_MSG+"_"+imgId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int imgId, ImgAwardMsgEntity entity){
        GameData.getCache().set(PrefixMsg.IMG_AWARD_MSG+"_"+imgId, entity);
    }

    /**
     * 删除缓存
     */
    private void removeCache(int imgId){
        GameData.getCache().removeCache(PrefixMsg.IMG_AWARD_MSG+"_"+imgId);
    }

    //=========================db===========================

    /**
     * 根据ID查询
     */
    private ImgAwardMsgEntity loadDbById(int id) {
        return GameData.getDB().get(ImgAwardMsgEntity.class, id);
    }

}
