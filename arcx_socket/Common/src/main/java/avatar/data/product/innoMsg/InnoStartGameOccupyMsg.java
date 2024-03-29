package avatar.data.product.innoMsg;

/**
 * 接收自研设备开始游戏占用中校验操作信息
 */
public class InnoStartGameOccupyMsg {

    private String alias;//设备号

    private int userId;//玩家ID

    private String userName;//玩家昵称

    private String imgUrl;//玩家头像

    private int serverSideType;//服务端类型

    private long onProductTime;//上机时间

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

    public long getOnProductTime() {
        return onProductTime;
    }

    public void setOnProductTime(long onProductTime) {
        this.onProductTime = onProductTime;
    }
}
