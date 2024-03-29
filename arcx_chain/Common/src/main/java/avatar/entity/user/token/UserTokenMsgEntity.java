package avatar.entity.user.token;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="user_token_msg" , comment = "玩家token信息")
public class UserTokenMsgEntity extends BaseEntity {
    public UserTokenMsgEntity() {
        super(UserTokenMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "access_token" , comment = "调用凭证" )
    private String accessToken;

    @Column(name = "access_out_time" , comment = "调用凭证过期时间" )
    private long accessOutTime;

    @Column(name = "refresh_token" , comment = "刷新凭证" )
    private String refreshToken;

    @Column(name = "refresh_out_time" , comment = "刷新凭证过期时间" )
    private long refreshOutTime;

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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getAccessOutTime() {
        return accessOutTime;
    }

    public void setAccessOutTime(long accessOutTime) {
        this.accessOutTime = accessOutTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getRefreshOutTime() {
        return refreshOutTime;
    }

    public void setRefreshOutTime(long refreshOutTime) {
        this.refreshOutTime = refreshOutTime;
    }
}
