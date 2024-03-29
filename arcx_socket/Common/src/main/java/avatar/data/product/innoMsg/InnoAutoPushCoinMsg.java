package avatar.data.product.innoMsg;

/**
 * 自研设备自动投币
 */
public class InnoAutoPushCoinMsg {
    private int productId;//设备ID

    private String alias;//设备号

    private int userId;//玩家ID

    private String userName;//玩家昵称

    private String imgUrl;//玩家头像

    private int serverSideType;//服务端类型

    private long requestTime;//请求时间

    private int isStart;//是否开始自动投币

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

    public int getIsStart() {
        return isStart;
    }

    public void setIsStart(int isStart) {
        this.isStart = isStart;
    }
}
