package avatar.data.user.balance;

import java.io.Serializable;

/**
 * 玩家在线得分信息
 */
public class UserOnlineScoreMsg implements Serializable {
    //玩家ID
    private int userId;

    //商品类型
    private int commodityType;

    //增加数量
    private long addNum;

    //扣除数量
    private long costNum;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(int commodityType) {
        this.commodityType = commodityType;
    }

    public long getAddNum() {
        return addNum;
    }

    public void setAddNum(long addNum) {
        this.addNum = addNum;
    }

    public long getCostNum() {
        return costNum;
    }

    public void setCostNum(long costNum) {
        this.costNum = costNum;
    }
}
