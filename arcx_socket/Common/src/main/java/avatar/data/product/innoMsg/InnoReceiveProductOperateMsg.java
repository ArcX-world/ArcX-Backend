package avatar.data.product.innoMsg;

/**
 * 接收自研设备设备操作信息
 */
public class InnoReceiveProductOperateMsg {
    private int status;//状态

    private String alias;//设备号

    private int userId;//玩家ID

    private String userName;//玩家昵称

    private String imgUrl;//玩家头像

    private int serverSideType;//服务端类型

    private int innoProductOperateType;//自研设备操作类型

    private long onProductTime;//上机时间

    private int awardType;//设备中奖类型

    private int breakType;//设备故障类型

    private int coinNum;//获得币数量

    private int awardNum;//设备显示奖励游戏币

    private int isStart;//是否开始

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getInnoProductOperateType() {
        return innoProductOperateType;
    }

    public void setInnoProductOperateType(int innoProductOperateType) {
        this.innoProductOperateType = innoProductOperateType;
    }

    public long getOnProductTime() {
        return onProductTime;
    }

    public void setOnProductTime(long onProductTime) {
        this.onProductTime = onProductTime;
    }

    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public int getBreakType() {
        return breakType;
    }

    public void setBreakType(int breakType) {
        this.breakType = breakType;
    }

    public int getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(int coinNum) {
        this.coinNum = coinNum;
    }

    public int getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(int awardNum) {
        this.awardNum = awardNum;
    }

    public int getIsStart() {
        return isStart;
    }

    public void setIsStart(int isStart) {
        this.isStart = isStart;
    }
}
