package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**
 * 设备游戏中玩家信息
 */
public class ProductGamingUserMsg implements Serializable {
    //服务端类型
    private int serverSideType;

    //设备ID
    private int productId;

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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
