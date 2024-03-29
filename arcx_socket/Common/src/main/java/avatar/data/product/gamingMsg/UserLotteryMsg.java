package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**
 * 玩家彩票信息
 */
public class UserLotteryMsg implements Serializable {
    //玩家ID
    private int userId;

    //设备二级分类
    private int secondLevelType;

    //彩票数
    private int lotteryNum;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSecondLevelType() {
        return secondLevelType;
    }

    public void setSecondLevelType(int secondLevelType) {
        this.secondLevelType = secondLevelType;
    }

    public int getLotteryNum() {
        return lotteryNum;
    }

    public void setLotteryNum(int lotteryNum) {
        this.lotteryNum = lotteryNum;
    }
}
