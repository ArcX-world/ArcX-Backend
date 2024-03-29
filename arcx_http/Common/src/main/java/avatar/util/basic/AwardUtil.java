package avatar.util.basic;

import avatar.data.basic.award.GeneralAwardMsg;

/**
 * 奖励工具类
 */
public class AwardUtil {
    /**
     * 填充通用奖励信息
     */
    public static GeneralAwardMsg initGeneralAwardMsg(int commodityType, int awardImgId, int awardNum) {
        GeneralAwardMsg msg = new GeneralAwardMsg();
        msg.setCmdTp(commodityType);//商品类型
        msg.setAwdPct(MediaUtil.getMediaUrl(ImgUtil.loadAwardImg(awardImgId)));//奖励图片
        msg.setAwdAmt(awardNum);//奖励数量
        return msg;
    }

    /**
     * 填充通用奖励信息
     */
    public static GeneralAwardMsg initGeneralAwardMsg(int commodityType, String imgUrl, long awardNum) {
        GeneralAwardMsg msg = new GeneralAwardMsg();
        msg.setCmdTp(commodityType);//商品类型
        msg.setAwdPct(imgUrl);//奖励图片
        msg.setAwdAmt(awardNum);//奖励数量
        return msg;
    }
}
