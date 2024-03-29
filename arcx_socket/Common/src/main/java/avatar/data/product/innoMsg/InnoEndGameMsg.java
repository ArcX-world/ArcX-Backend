package avatar.data.product.innoMsg;

/**
 * 自研设备结束游戏信息
 */
public class InnoEndGameMsg {
    private int productId;//设备ID

    private String alias;//设备号

    private int userId;//玩家ID

    private String userName;//玩家昵称

    private String imgUrl;//玩家头像

    private int serverSideType;//服务端类型

    private long requestTime;//请求时间

    private int productMulti;//设备倍率等级

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getServerSideType() {
        return serverSideType;
    }

    public void setServerSideType(int serverSideType) {
        this.serverSideType = serverSideType;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public int getProductMulti() {
        return productMulti;
    }

    public void setProductMulti(int productMulti) {
        this.productMulti = productMulti;
    }
}
