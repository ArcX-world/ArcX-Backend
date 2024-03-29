package avatar.data.user.balance;

/**
 * 玩家道具信息
 */
public class PropertyKnapsackMsg {
    //道具类型
    private int pptTp;

    //道具名称
    private String nm;

    //道具描述
    private String dsc;

    //道具图片
    private String pct;

    //道具数量
    private long ppyAmt;

    public int getPptTp() {
        return pptTp;
    }

    public void setPptTp(int pptTp) {
        this.pptTp = pptTp;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public String getPct() {
        return pct;
    }

    public void setPct(String pct) {
        this.pct = pct;
    }

    public long getPpyAmt() {
        return ppyAmt;
    }

    public void setPpyAmt(long ppyAmt) {
        this.ppyAmt = ppyAmt;
    }
}
