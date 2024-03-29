package avatar.data.crossServer;

import java.io.Serializable;

/**
 * 通用跨服玩家信息
 */
public class GeneralCrossServerUserMsg implements Serializable {
    //服务端类型
    private int serverSideType;

    //玩家id
    private int userId;

    //玩家昵称
    private String nickName;

    //玩家头像
    private String imgUrl;

    //国家信息
    private String nationCode;

    //国家全拼
    private String nationEn;

    //玩家等级
    private int userLevel;

    //vip等级
    private int vipLevel;

    //设备奖励信息
    private CrossServerSearchProductPrizeMsg productPrizeMsg;

    //创建时间
    private long createTime;

    public int getServerSideType() {
        return serverSideType;
    }

    public void setServerSideType(int serverSideType) {
        this.serverSideType = serverSideType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public String getNationEn() {
        return nationEn;
    }

    public void setNationEn(String nationEn) {
        this.nationEn = nationEn;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public CrossServerSearchProductPrizeMsg getProductPrizeMsg() {
        return productPrizeMsg;
    }

    public void setProductPrizeMsg(CrossServerSearchProductPrizeMsg productPrizeMsg) {
        this.productPrizeMsg = productPrizeMsg;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
