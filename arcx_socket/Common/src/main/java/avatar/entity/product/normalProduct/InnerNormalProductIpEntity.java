package avatar.entity.product.normalProduct;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="inner_normal_product_ip" , comment = "普通设备内推IP")
public class InnerNormalProductIpEntity extends BaseEntity {
    public InnerNormalProductIpEntity() {
        super(InnerNormalProductIpEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "from_ip" , comment = "来源IP" )
    private String fromIp;

    @Column(name = "from_port" , comment = "来源端口" )
    private int fromPort;

    @Column(name = "to_ip" , comment = "接收IP" )
    private String toIp;

    @Column(name = "to_port" , comment = "接收端口" )
    private int toPort;

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

    public int getFromPort() {
        return fromPort;
    }

    public void setFromPort(int fromPort) {
        this.fromPort = fromPort;
    }

    public String getToIp() {
        return toIp;
    }

    public void setToIp(String toIp) {
        this.toIp = toIp;
    }

    public int getToPort() {
        return toPort;
    }

    public void setToPort(int toPort) {
        this.toPort = toPort;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
