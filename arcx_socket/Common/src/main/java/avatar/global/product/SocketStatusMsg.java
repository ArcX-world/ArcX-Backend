package avatar.global.product;

import java.io.Serializable;

/**
 * 设备socket状态信息
 */
public class SocketStatusMsg implements Serializable {
    //是否正常
    private boolean isNormal;

    //重连时间
    private long time;

    public boolean isNormal() {
        return isNormal;
    }

    public void setNormal(boolean normal) {
        isNormal = normal;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
