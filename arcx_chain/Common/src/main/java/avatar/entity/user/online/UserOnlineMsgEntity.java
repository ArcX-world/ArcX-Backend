package avatar.entity.user.online;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="user_online_msg" , comment = "玩家在线信息")
public class UserOnlineMsgEntity extends BaseEntity {
    public UserOnlineMsgEntity() {
        super(UserOnlineMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "product_id" , comment = "设备ID" )
    private int productId;

    @Column(name = "is_online" , comment = "是否在线" )
    private int isOnline;

    @Column(name = "is_gaming" , comment = "是否游戏中" )
    private int isGaming;

    @Column(name = "is_charter" , comment = "是否包机" )
    private int isCharter;

    @Column(name = "ip" , comment = "玩家IP" )
    private String ip;

    @Column(name = "port" , comment = "端口" )
    private String port;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getIsGaming() {
        return isGaming;
    }

    public void setIsGaming(int isGaming) {
        this.isGaming = isGaming;
    }

    public int getIsCharter() {
        return isCharter;
    }

    public void setIsCharter(int isCharter) {
        this.isCharter = isCharter;
    }

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
