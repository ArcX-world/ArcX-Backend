package avatar.data.product.innoMsg;

/**
 * 自研设备结算窗口信息
 */
public class InnoSettlementWindowMsg {
    private String alias;//设备号

    private int userId;//玩家ID

    private int serverSideType;//服务端类型

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

}
