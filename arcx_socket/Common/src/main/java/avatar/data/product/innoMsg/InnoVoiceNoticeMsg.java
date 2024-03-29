package avatar.data.product.innoMsg;

/**
 * 自研设备声音通知信息
 */
public class InnoVoiceNoticeMsg {
    private String alias;//设备号

    private int userId;//玩家ID

    private int serverSideType;//服务端类型

    private int voiceType;//提示声音类型

    private int isStart;//是否开始播放音效

    private int isEndSwitch;//是否有开始结束

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

    public int getVoiceType() {
        return voiceType;
    }

    public void setVoiceType(int voiceType) {
        this.voiceType = voiceType;
    }

    public int getIsStart() {
        return isStart;
    }

    public void setIsStart(int isStart) {
        this.isStart = isStart;
    }

    public int getIsEndSwitch() {
        return isEndSwitch;
    }

    public void setIsEndSwitch(int isEndSwitch) {
        this.isEndSwitch = isEndSwitch;
    }
}
