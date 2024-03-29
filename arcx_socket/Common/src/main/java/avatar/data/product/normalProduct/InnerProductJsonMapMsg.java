package avatar.data.product.normalProduct;

import avatar.data.product.general.ResponseGeneralMsg;

import java.util.Map;

/**
 * 设备内部操作json map信息
 */
public class InnerProductJsonMapMsg {
    //内部操作协议号
    private int cmd;

    //当前通信的服务端ID
    private int hostId;

    //具体的内容参数
    private Map<String, Object> dataMap;

    //操作结果
    private int status;

    //服务器时间
    private String time;

    //设备ID
    private int productId;

    //设备玩家
    private int userId;

    //返回通用信息
    private ResponseGeneralMsg responseGeneralMsg;

    //设备信息通用参数
    private ProductGeneralParamsMsg productGeneralParamsMsg;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public ResponseGeneralMsg getResponseGeneralMsg() {
        return responseGeneralMsg;
    }

    public void setResponseGeneralMsg(ResponseGeneralMsg responseGeneralMsg) {
        this.responseGeneralMsg = responseGeneralMsg;
    }

    public ProductGeneralParamsMsg getProductGeneralParamsMsg() {
        return productGeneralParamsMsg;
    }

    public void setProductGeneralParamsMsg(ProductGeneralParamsMsg productGeneralParamsMsg) {
        this.productGeneralParamsMsg = productGeneralParamsMsg;
    }
}
