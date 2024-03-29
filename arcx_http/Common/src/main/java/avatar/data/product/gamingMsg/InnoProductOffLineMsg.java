package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**
 * 自研设备下线信息
 */
public class InnoProductOffLineMsg implements Serializable {
    //设备ID
    private int productId;

    //倍率等级
    private int multi;

    //下机时间
    private long offLineTime;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getMulti() {
        return multi;
    }

    public void setMulti(int multi) {
        this.multi = multi;
    }

    public long getOffLineTime() {
        return offLineTime;
    }

    public void setOffLineTime(long offLineTime) {
        this.offLineTime = offLineTime;
    }
}
