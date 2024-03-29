package avatar.data.product.normalProduct;

/**
 * 请求通用信息
 */
public class RequestGeneralMsg {
    //服务端类型
    private int serverSideType;

    //设备号
    private String alias;

    //玩家ID
    private int userId;

    //玩家昵称
    private String userName;

    //玩家头像
    private String imgUrl;

    public int getServerSideType() {
        return serverSideType;
    }

    public void setServerSideType(int serverSideType) {
        this.serverSideType = serverSideType;
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
}
