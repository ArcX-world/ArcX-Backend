package avatar.data.activity.dragonTrain;

import avatar.data.basic.award.GeneralAwardMsg;

import java.util.List;

/**
 * 龙珠玛丽机中奖推送信息
 */
public class DragonTrainAwardPushMsg {
    //图标列表
    private List<String> icTbln;

    //中奖图标列表
    private List<DragonTrainAwardIndexMsg> icAwdTbln;

    //最终奖励信息
    private List<GeneralAwardMsg> awdTbln;

    public List<String> getIcTbln() {
        return icTbln;
    }

    public void setIcTbln(List<String> icTbln) {
        this.icTbln = icTbln;
    }

    public List<DragonTrainAwardIndexMsg> getIcAwdTbln() {
        return icAwdTbln;
    }

    public void setIcAwdTbln(List<DragonTrainAwardIndexMsg> icAwdTbln) {
        this.icAwdTbln = icAwdTbln;
    }

    public List<GeneralAwardMsg> getAwdTbln() {
        return awdTbln;
    }

    public void setAwdTbln(List<GeneralAwardMsg> awdTbln) {
        this.awdTbln = awdTbln;
    }
}
