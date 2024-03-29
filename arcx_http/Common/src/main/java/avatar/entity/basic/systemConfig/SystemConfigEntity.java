package avatar.entity.basic.systemConfig;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="system_config" , comment = "系统配置信息")
public class SystemConfigEntity extends BaseEntity {
    public SystemConfigEntity() {
        super(SystemConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "server_type" , comment = "服务端类型" )
    private int serverType;

    @Column(name = "media_prefix" , comment = "存储桶路径前缀" )
    private String mediaPrefix;

    @Column(name = "register_coin" , comment = "登录送币" )
    private int registerCoin;

    @Column(name = "system_maintain" , comment = "是否系统维护" )
    private int systemMaintain;

    @Column(name = "server_img" , comment = "客服头像" )
    private String serverImg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public String getMediaPrefix() {
        return mediaPrefix;
    }

    public void setMediaPrefix(String mediaPrefix) {
        this.mediaPrefix = mediaPrefix;
    }

    public int getRegisterCoin() {
        return registerCoin;
    }

    public void setRegisterCoin(int registerCoin) {
        this.registerCoin = registerCoin;
    }

    public int getSystemMaintain() {
        return systemMaintain;
    }

    public void setSystemMaintain(int systemMaintain) {
        this.systemMaintain = systemMaintain;
    }

    public String getServerImg() {
        return serverImg;
    }

    public void setServerImg(String serverImg) {
        this.serverImg = serverImg;
    }
}
