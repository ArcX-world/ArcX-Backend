package avatar.entity.user.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="user_info" , comment = "用户信息表")
public class UserInfoEntity extends BaseEntity {
    public UserInfoEntity() {
        super(UserInfoEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "玩家id" )
    private int id;

    @Column(name = "nick_name" , comment = "玩家昵称")
    private String nickName;

    @Column(name = "img_url" , comment = "头像地址")
    private String imgUrl;

    @Column(name = "ip" , comment = "ip")
    private String ip;

    @Column(name = "login_way" , comment = "最近登录方式")
    private int loginWay;

    @Column(name = "mobile_platform_type" , comment = "手机平台类型")
    private int mobilePlatformType;

    @Column(name = "sex" , comment = "性别")
    private int sex;

    @Column(name = "email" , comment = "邮箱")
    private String email;

    @Column(name = "password" , comment = "密码")
    private String password;

    @Column(name = "status" , comment = "状态")
    private int status;

    @Column(name = "create_time" , comment = "创建时间")
    private String createTime;

    @Column(name = "update_time" , comment = "更新时间")
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getLoginWay() {
        return loginWay;
    }

    public void setLoginWay(int loginWay) {
        this.loginWay = loginWay;
    }

    public int getMobilePlatformType() {
        return mobilePlatformType;
    }

    public void setMobilePlatformType(int mobilePlatformType) {
        this.mobilePlatformType = mobilePlatformType;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
