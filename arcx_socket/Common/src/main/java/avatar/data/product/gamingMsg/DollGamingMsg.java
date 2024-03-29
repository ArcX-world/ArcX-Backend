package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**
 * 娃娃机游戏信息
 */
public class DollGamingMsg implements Serializable {
    //设备ID
    private int productId;

    //游戏次数
    private int time;

    //是否设备初始化
    private boolean isInitalization;

    //是否已经操作
    private boolean isCatch;

    //游戏环节
    private int gamingState;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isInitalization() {
        return isInitalization;
    }

    public void setInitalization(boolean initalization) {
        isInitalization = initalization;
    }

    public boolean isCatch() {
        return isCatch;
    }

    public void setCatch(boolean aCatch) {
        isCatch = aCatch;
    }

    public int getGamingState() {
        return gamingState;
    }

    public void setGamingState(int gamingState) {
        this.gamingState = gamingState;
    }
}
