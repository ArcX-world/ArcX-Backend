package avatar.data.user.attribute;

import java.io.Serializable;

/**
 * 玩家在线经验信息
 */
public class UserOnlineExpMsg implements Serializable {
    //玩家ID
    private int userId;

    //游戏币数
    private long coinNum;

    //经验数
    private long expNum;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(long coinNum) {
        this.coinNum = coinNum;
    }

    public long getExpNum() {
        return expNum;
    }

    public void setExpNum(long expNum) {
        this.expNum = expNum;
    }
}
