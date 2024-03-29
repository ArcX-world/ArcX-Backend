package avatar.data.recharge;

/**
 * 充值道具详情信息
 */
public class RechargePropertyDetailMsg {
    //商品ID
    private int cmdId;

    //道具数量
    private int ppyAmt;

    //道具名称
    private String nm;

    //道具描述
    private String dsc;

    //图片
    private String pct;

    //AXC价格
    private int axcAmt;

    //是否购买上限
    private int soFlg;

    public int getCmdId() {
        return cmdId;
    }

    public void setCmdId(int cmdId) {
        this.cmdId = cmdId;
    }

    public int getPpyAmt() {
        return ppyAmt;
    }

    public void setPpyAmt(int ppyAmt) {
        this.ppyAmt = ppyAmt;
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

    public int getAxcAmt() {
        return axcAmt;
    }

    public void setAxcAmt(int axcAmt) {
        this.axcAmt = axcAmt;
    }

    public int getSoFlg() {
        return soFlg;
    }

    public void setSoFlg(int soFlg) {
        this.soFlg = soFlg;
    }
}
