package avatar.util.basic.general;

import avatar.data.basic.award.GeneralAwardMsg;

/**
 * 奖励工具类
 */
public class AwardUtil {
    /**
     * 填充通用奖励信息
     */
    public static GeneralAwardMsg initGeneralAwardMsg(int awardType, int awardId, int awardImgId, int awardNum) {
        GeneralAwardMsg msg = new GeneralAwardMsg();
        msg.setCmdTp(awardType);//商品类型
        msg.setCmdId(awardId);//奖励ID
        msg.setAwdPct(MediaUtil.getMediaUrl(ImgUtil.loadAwardImg(awardImgId)));//奖励图片
        msg.setAwdAmt(awardNum);//奖励数量
        return msg;
    }

    /**
     * 填充通用奖励信息
     */
    public static GeneralAwardMsg initGeneralAwardMsg(int awardType, int awardId, String awardImgUrl, int awardNum) {
        GeneralAwardMsg msg = new GeneralAwardMsg();
        msg.setCmdTp(awardType);//商品类型
        msg.setCmdId(awardId);//奖励ID
        msg.setAwdPct(awardImgUrl);//奖励图片
        msg.setAwdAmt(awardNum);//奖励数量
        return msg;
    }
}
