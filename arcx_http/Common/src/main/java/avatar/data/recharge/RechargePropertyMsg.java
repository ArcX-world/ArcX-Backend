package avatar.data.recharge;

import java.util.List;

/**
 * 充值道具信息
 */
public class RechargePropertyMsg {
    //下次刷新时间
    private long rfTm;

    //刷新AXC价格
    private int rfAxcAmt;

    //道具列表
    List<RechargePropertyDetailMsg> ppyTbln;

    public long getRfTm() {
        return rfTm;
    }

    public void setRfTm(long rfTm) {
        this.rfTm = rfTm;
    }

    public int getRfAxcAmt() {
        return rfAxcAmt;
    }

    public void setRfAxcAmt(int rfAxcAmt) {
        this.rfAxcAmt = rfAxcAmt;
    }

    public List<RechargePropertyDetailMsg> getPpyTbln() {
        return ppyTbln;
    }

    public void setPpyTbln(List<RechargePropertyDetailMsg> ppyTbln) {
        this.ppyTbln = ppyTbln;
    }
}
