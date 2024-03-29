package avatar.data.product.normalProduct;

/**
 * 设备信息通用参数
 */
public class ProductGeneralParamsMsg {
    //玩家游戏次数
    private int gameTime;

    //玩家上机时间
    private long onProductTime;

    //最近一次刷新时间
    private long pushCoinOnTime;

    //设备操作
    private int operateState;

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
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

    public int getOperateState() {
        return operateState;
    }

    public void setOperateState(int operateState) {
        this.operateState = operateState;
    }

}
