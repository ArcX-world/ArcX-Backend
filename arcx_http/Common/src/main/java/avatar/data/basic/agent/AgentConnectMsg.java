package avatar.data.basic.agent;

import java.io.Serializable;

/**
 * 代理连接信息
 */
public class AgentConnectMsg implements Serializable {
    //ip地址
    private String ip;

    //端口号
    private String port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
