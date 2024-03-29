package avatar.data.product.innoMsg;

/**
 * 自研设备设备操作信息
 */
public class InnoProductOperateMsg {
    private int productId;//设备ID

    private String alias;//设备号

    private int userId;//玩家ID

    private String userName;//玩家昵称

    private String imgUrl;//玩家头像

    private int serverSideType;//服务端类型

    private long requestTime;//请求时间

    private long onProductTime;//上机时间

    private int innoProductOperateType;//自研设备操作类型

    private int productCost;//设备币值

    private int agyptOpenBox;//埃及开箱子

    private int clownCircusFerrule;//小丑动物套圈

    private int pirateCannon;//海盗开炮

    private int coinLevelWeight;//权重等级

    private int payFlag;//是否付费

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

    public long getOnProductTime() {
        return onProductTime;
    }

    public void setOnProductTime(long onProductTime) {
        this.onProductTime = onProductTime;
    }

    public int getInnoProductOperateType() {
        return innoProductOperateType;
    }

    public void setInnoProductOperateType(int innoProductOperateType) {
        this.innoProductOperateType = innoProductOperateType;
    }

    public int getProductCost() {
        return productCost;
    }

    public void setProductCost(int productCost) {
        this.productCost = productCost;
    }

    public int getAgyptOpenBox() {
        return agyptOpenBox;
    }

    public void setAgyptOpenBox(int agyptOpenBox) {
        this.agyptOpenBox = agyptOpenBox;
    }

    public int getClownCircusFerrule() {
        return clownCircusFerrule;
    }

    public void setClownCircusFerrule(int clownCircusFerrule) {
        this.clownCircusFerrule = clownCircusFerrule;
    }

    public int getPirateCannon() {
        return pirateCannon;
    }

    public void setPirateCannon(int pirateCannon) {
        this.pirateCannon = pirateCannon;
    }

    public int getCoinLevelWeight() {
        return coinLevelWeight;
    }

    public void setCoinLevelWeight(int coinLevelWeight) {
        this.coinLevelWeight = coinLevelWeight;
    }

    public int getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(int payFlag) {
        this.payFlag = payFlag;
    }
}
