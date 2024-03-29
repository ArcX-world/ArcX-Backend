package avatar.data.activity.dragonTrain;

/**
 * 龙珠玛丽机中奖位置信息
 */
public class DragonTrainAwardIndexMsg {
    //奖励类型
    private int cmdTp;

    //中奖图标位置
    private int awdInx;

    public int getCmdTp() {
        return cmdTp;
    }

    public void setCmdTp(int cmdTp) {
        this.cmdTp = cmdTp;
    }

    public int getAwdInx() {
        return awdInx;
    }

    public void setAwdInx(int awdInx) {
        this.awdInx = awdInx;
    }
}
