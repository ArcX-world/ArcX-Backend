package avatar.data.product.innoMsg;

/**
 * 自研设备中奖得分倍数信息
 */
public class InnoAwardScoreMultiMsg {
    private String alias;//设备号

    private int userId;//玩家ID

    private int serverSideType;//服务端类型

    private int awardMulti;//中奖得分倍数

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getServerSideType() {
        return serverSideType;
    }

    public void setServerSideType(int serverSideType) {
        this.serverSideType = serverSideType;
    }

    public int getAwardMulti() {
        return awardMulti;
    }

    public void setAwardMulti(int awardMulti) {
        this.awardMulti = awardMulti;
    }
}
