package avatar.entity.basic.agent;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="agent_area_msg" , comment = "代理区域信息")
public class AgentAreaMsgEntity extends BaseEntity {
    public AgentAreaMsgEntity() {
        super(AgentAreaMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "area_name" , comment = "区域名称" )
    private String areaName;

    @Column(name = "socket_ip" , comment = "socket ip" )
    private String socketIp;

    @Column(name = "socket_port" , comment = "socket端口" )
    private String socketPort;

    @Column(name = "http_domain" , comment = "http域名" )
    private String httpDomain;

    @Column(name = "create_time" , comment = "创建时间" )
    private String createTime;

    @Column(name = "update_time" , comment = "更新时间" )
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getSocketIp() {
        return socketIp;
    }

    public void setSocketIp(String socketIp) {
        this.socketIp = socketIp;
    }

    public String getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(String socketPort) {
        this.socketPort = socketPort;
    }

    public String getHttpDomain() {
        return httpDomain;
    }

    public void setHttpDomain(String httpDomain) {
        this.httpDomain = httpDomain;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
