package avatar.data.user.attribute;

/**
 * 玩家能量信息
 */
public class UserEnergyMsg {
    //当前进度数量
    private long cnAmt;

    //总进度数量
    private long ttAmt;

    //下次刷新时间
    private long lfTm;

    public long getCnAmt() {
        return cnAmt;
    }

    public void setCnAmt(long cnAmt) {
        this.cnAmt = cnAmt;
    }

    public long getTtAmt() {
        return ttAmt;
    }

    public void setTtAmt(long ttAmt) {
        this.ttAmt = ttAmt;
    }

    public long getLfTm() {
        return lfTm;
    }

    public void setLfTm(long lfTm) {
        this.lfTm = lfTm;
    }
}
