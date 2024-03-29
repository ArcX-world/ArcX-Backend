package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**
 * 玩家开始游戏投币倍率信息
 */
public class UserStartGameMultiMsg implements Serializable {
    //玩家ID
    private int userId;

    //倍率
    private int coinMulti;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCoinMulti() {
        return coinMulti;
    }

    public void setCoinMulti(int coinMulti) {
        this.coinMulti = coinMulti;
    }
}
