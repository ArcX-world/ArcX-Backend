package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**
 * 设备房间信息
 */
public class ProductRoomMsg implements Serializable {
    //设备ID
    private int productId;

    //上机的玩家ID
    private int gamingUserId;

    //上机时间
    private long onProductTime;

    //设备刷新时间
    private long pushCoinOnTime;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getGamingUserId() {
        return gamingUserId;
    }

    public void setGamingUserId(int gamingUserId) {
        this.gamingUserId = gamingUserId;
    }

    public long getOnProductTime() {
        return onProductTime;
    }

    public void setOnProductTime(long onProductTime) {
        this.onProductTime = onProductTime;
    }

    public long getPushCoinOnTime() {
        return pushCoinOnTime;
    }

    public void setPushCoinOnTime(long pushCoinOnTime) {
        this.pushCoinOnTime = pushCoinOnTime;
    }

}
