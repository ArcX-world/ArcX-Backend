package avatar.data.product.innoNaPay;

import java.io.Serializable;

/**
 * 自研付费NA玩家状态信息
 */
public class InnoNaPayUserStatusMsg implements Serializable {
    //玩家ID
    private int userId;

    //是否付费
    private boolean payFlag;

    //刷新时间
    private long refreshTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isPayFlag() {
        return payFlag;
    }

    public void setPayFlag(boolean payFlag) {
        this.payFlag = payFlag;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }
}
