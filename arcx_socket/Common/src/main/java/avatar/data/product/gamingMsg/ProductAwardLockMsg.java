package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**
 * 设备中奖锁定信息
 */
public class ProductAwardLockMsg implements Serializable {
    //设备ID
    private int productId;

    //投币倍率
    private int coinMulti;

    //是否中奖锁定
    private int isAwardLock;

    //锁定时间
    private long lockTime;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCoinMulti() {
        return coinMulti;
    }

    public void setCoinMulti(int coinMulti) {
        this.coinMulti = coinMulti;
    }

    public int getIsAwardLock() {
        return isAwardLock;
    }

    public void setIsAwardLock(int isAwardLock) {
        this.isAwardLock = isAwardLock;
    }

    public long getLockTime() {
        return lockTime;
    }

    public void setLockTime(long lockTime) {
        this.lockTime = lockTime;
    }
}
