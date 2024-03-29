package avatar.data.product.gamingMsg;

/**
 * 娃娃机奖励的信息
 */
public class DollAwardCommodityMsg {
    //商品类型
    private int commodityType;

    //奖励ID
    private int awardId;

    //奖励数量
    private int awardNum;

    //奖励图片
    private int awardImgId;

    public int getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(int commodityType) {
        this.commodityType = commodityType;
    }

    public int getAwardId() {
        return awardId;
    }

    public void setAwardId(int awardId) {
        this.awardId = awardId;
    }

    public int getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(int awardNum) {
        this.awardNum = awardNum;
    }

    public int getAwardImgId() {
        return awardImgId;
    }

    public void setAwardImgId(int awardImgId) {
        this.awardImgId = awardImgId;
    }
}
