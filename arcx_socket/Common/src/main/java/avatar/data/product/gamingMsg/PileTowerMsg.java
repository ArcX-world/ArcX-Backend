package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**
 * 堆塔信息
 */
public class PileTowerMsg implements Serializable {
    //设备ID
    private int productId;

    //堆塔连续指令次数
    private int tillTime;

    //堆塔时间
    private long pileTime;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getTillTime() {
        return tillTime;
    }

    public void setTillTime(int tillTime) {
        this.tillTime = tillTime;
    }

    public long getPileTime() {
        return pileTime;
    }

    public void setPileTime(long pileTime) {
        this.pileTime = pileTime;
    }
}
