package avatar.data.basic.award;

/**
 * 奖励信息
 */
public class GeneralAwardMsg {
    //商品类型
    private int cmdTp;

    //奖励图片
    private String awdPct;

    //奖励数量
    private long awdAmt;

    public int getCmdTp() {
        return cmdTp;
    }

    public void setCmdTp(int cmdTp) {
        this.cmdTp = cmdTp;
    }

    public String getAwdPct() {
        return awdPct;
    }

    public void setAwdPct(String awdPct) {
        this.awdPct = awdPct;
    }

    public long getAwdAmt() {
        return awdAmt;
    }

    public void setAwdAmt(long awdAmt) {
        this.awdAmt = awdAmt;
    }
}
