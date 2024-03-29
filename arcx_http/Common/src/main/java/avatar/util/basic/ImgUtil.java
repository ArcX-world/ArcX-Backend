package avatar.util.basic;

import avatar.entity.basic.img.ImgAwardMsgEntity;
import avatar.entity.basic.img.ImgNftEntity;
import avatar.entity.basic.img.ImgProductDetailEntity;
import avatar.entity.basic.img.ImgProductMsgEntity;
import avatar.module.basic.img.ImgAwardMsgDao;
import avatar.module.basic.img.ImgNftDao;
import avatar.module.basic.img.ImgProductDetailDao;
import avatar.module.basic.img.ImgProductMsgDao;

/**
 * 蹄片工具类
 */
public class ImgUtil {
    /**
     * 获取设备图片
     */
    public static String loadProductImg(int productImgId) {
        //查询设备图片信息
        ImgProductMsgEntity entity = ImgProductMsgDao.getInstance().loadByImgId(productImgId);
        return entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl());
    }

    /**
     * 设备详情
     */
    public static String loadProductDetailImg(int imgProductDetailId) {
        String retMsg = "";
        ImgProductDetailEntity entity = ImgProductDetailDao.getInstance().loadById(imgProductDetailId);
        if(entity!=null){
            retMsg = entity.getFileUrl();
        }
        return MediaUtil.getMediaUrl(retMsg);
    }

    /**
     * 获取奖励图片
     */
    public static String loadAwardImg(int awardImgId){
        //查询奖励图片信息
        ImgAwardMsgEntity entity = ImgAwardMsgDao.getInstance().loadByImgId(awardImgId);
        return entity==null?"":entity.getImgUrl();
    }

    /**
     * NFT图片
     */
    public static String nftImg(int imgId) {
        ImgNftEntity entity = ImgNftDao.getInstance().loadByImgId(imgId);
        return entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl());
    }

}
