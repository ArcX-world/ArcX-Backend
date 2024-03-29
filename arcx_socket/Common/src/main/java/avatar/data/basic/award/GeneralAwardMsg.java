package avatar.data.basic.award;

/**
 * 奖励信息
 */
public class GeneralAwardMsg {
    //商品类型
    private int cmdTp;

    //商品ID
    private int cmdId;

    //奖励图片
    private String awdPct;

    //奖励数量
    private int awdAmt;

    public int getCmdTp() {
        return cmdTp;
    }

    public void setCmdTp(int cmdTp) {
        this.cmdTp = cmdTp;
    }

    public int getCmdId() {
        return cmdId;
    }

    public void setCmdId(int cmdId) {
        this.cmdId = cmdId;
    }

    public String getAwdPct() {
        return awdPct;
    }

    public void setAwdPct(String awdPct) {
        this.awdPct = awdPct;
    }

    public int getAwdAmt() {
        return awdAmt;
    }

    public void setAwdAmt(int awdAmt) {
        this.awdAmt = awdAmt;
    }
}
