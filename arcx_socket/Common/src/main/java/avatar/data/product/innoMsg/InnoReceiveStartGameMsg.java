package avatar.data.product.innoMsg;

/**
 * 接收自研设备开始游戏信息
 */
public class InnoReceiveStartGameMsg {
    private int status;//状态

    private String alias;//设备号

    private int userId;//玩家ID

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
}
