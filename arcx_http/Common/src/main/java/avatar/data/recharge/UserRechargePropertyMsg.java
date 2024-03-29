package avatar.data.recharge;

import java.io.Serializable;
import java.util.List;

/**
 * 玩家充值道具信息
 */
public class UserRechargePropertyMsg implements Serializable {
    //玩家ID
    private int userId;

    //刷新时间
    private long refreshTime;

    //道具列表
    private List<Integer> propertyList;

    //已购买的道具
    private List<Integer> buyList;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public List<Integer> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Integer> propertyList) {
        this.propertyList = propertyList;
    }

    public List<Integer> getBuyList() {
        return buyList;
    }

    public void setBuyList(List<Integer> buyList) {
        this.buyList = buyList;
    }
}
