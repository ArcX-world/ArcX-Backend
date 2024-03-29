package avatar.data.activity.welfare;

import avatar.data.basic.award.GeneralAwardMsg;

import java.util.List;

/**
 * 福利签到奖励信息
 */
public class WelfareSignAwardMsg {
    //奖励列表
    private List<GeneralAwardMsg> earnArr;

    //是否已经签到
    private int snFlag;

    public List<GeneralAwardMsg> getEarnArr() {
        return earnArr;
    }

    public void setEarnArr(List<GeneralAwardMsg> earnArr) {
        this.earnArr = earnArr;
    }

    public int getSnFlag() {
        return snFlag;
    }

    public void setSnFlag(int snFlag) {
        this.snFlag = snFlag;
    }
}
