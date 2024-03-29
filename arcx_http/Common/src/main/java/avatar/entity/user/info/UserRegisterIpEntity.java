package avatar.entity.user.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="user_register_ip" , comment = "玩家注册ip")
public class UserRegisterIpEntity extends BaseEntity {
    public UserRegisterIpEntity() {
        super(UserRegisterIpEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "ip" , comment = "ip")
    private String ip;

    @Column(name = "register_version" , comment = "注册版本号")
    private String registerVersion;

    @Column(name = "login_way_type" , comment = "登录方式" )
    private int loginWayType;

    @Column(name = "login_platform" , comment = "登录平台" )
    private int loginPlatform;

    @Column(name = "last_ip" , comment = "当前登录IP")
    private String lastIp;

    @Column(name = "last_login_way" , comment = "当前登录方式" )
    private int lastLoginWay;

    @Column(name = "last_login_platform" , comment = "当前登录平台" )
    private int lastLoginPlatform;

    @Column(name = "last_version" , comment = "当前版本号")
    private String lastVersion;

    @Column(name = "create_time" , comment = "创建时间")
    private String createTime;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRegisterVersion() {
        return registerVersion;
    }

    public void setRegisterVersion(String registerVersion) {
        this.registerVersion = registerVersion;
    }

    public int getLoginWayType() {
        return loginWayType;
    }

    public void setLoginWayType(int loginWayType) {
        this.loginWayType = loginWayType;
    }

    public int getLoginPlatform() {
        return loginPlatform;
    }

    public void setLoginPlatform(int loginPlatform) {
        this.loginPlatform = loginPlatform;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public int getLastLoginWay() {
        return lastLoginWay;
    }

    public void setLastLoginWay(int lastLoginWay) {
        this.lastLoginWay = lastLoginWay;
    }

    public int getLastLoginPlatform() {
        return lastLoginPlatform;
    }

    public void setLastLoginPlatform(int lastLoginPlatform) {
        this.lastLoginPlatform = lastLoginPlatform;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
