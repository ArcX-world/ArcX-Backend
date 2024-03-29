package avatar.entity.log;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="user_operate_log" , comment = "玩家操作日志")
public class UserOperateLogEntity extends BaseEntity {
    public UserOperateLogEntity() {
        super(UserOperateLogEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private long id;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "operate_type" , comment = "操作类型" )
    private int operateType;

    @Column(name = "operate_log" , comment = "操作日志")
    private String operateLog;

    @Column(name = "operate_ip" , comment = "操作IP")
    private String operateIp;

    @Column(name = "create_time" , comment = "创建时间")
    private String createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public String getOperateLog() {
        return operateLog;
    }

    public void setOperateLog(String operateLog) {
        this.operateLog = operateLog;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
