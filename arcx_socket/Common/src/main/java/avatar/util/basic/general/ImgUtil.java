package avatar.util.basic.general;

import avatar.entity.basic.img.ImgAwardMsgEntity;
import avatar.entity.basic.img.ImgProductPresentMsgEntity;
import avatar.module.basic.img.ImgAwardMsgDao;
import avatar.module.basic.img.ImgProductPresentDao;

/**
 * 图片工具类
 */
public class ImgUtil {
    /**
     * 获取奖励图片
     */
    public static String loadAwardImg(int awardImgId){
        //查询奖励图片信息
        ImgAwardMsgEntity entity = ImgAwardMsgDao.getInstance().loadByImgId(awardImgId);
        return entity==null?"":entity.getImgUrl();
    }

    /**
     * 获取设备奖励图片
     */
    public static String productAwardImg(int awardImgId) {
        String awardImgUrl = "";
        if(awardImgId>0){
            //查询设备奖励信息
            ImgProductPresentMsgEntity entity = ImgProductPresentDao.getInstance().loadById(awardImgId);
            awardImgUrl = entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl());
        }
        return awardImgUrl;
    }


}
