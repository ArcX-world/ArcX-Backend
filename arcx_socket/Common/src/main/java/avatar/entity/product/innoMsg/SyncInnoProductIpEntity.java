package avatar.entity.product.innoMsg;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="sync_inno_product_ip" , comment = "自研设备内推IP")
public class SyncInnoProductIpEntity extends BaseEntity {
    public SyncInnoProductIpEntity() {
        super(SyncInnoProductIpEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "from_ip" , comment = "来源IP" )
    private String fromIp;

    @Column(name = "from_port" , comment = "来源端口" )
    private String fromPort;

    @Column(name = "to_ip" , comment = "接收IP" )
    private String toIp;

    @Column(name = "to_port" , comment = "接收端口" )
    private String toPort;

    @Column(name = "user_id" , comment = "唯一ID" )
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public String getFromPort() {
        return fromPort;
    }

    public void setFromPort(String fromPort) {
        this.fromPort = fromPort;
    }

    public String getToIp() {
        return toIp;
    }

    public void setToIp(String toIp) {
        this.toIp = toIp;
    }

    public String getToPort() {
        return toPort;
    }

    public void setToPort(String toPort) {
        this.toPort = toPort;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
