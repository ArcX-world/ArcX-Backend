package avatar.entity.basic.agent;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="agent_default_msg" , comment = "代理默认信息")
public class AgentDefaultMsgEntity extends BaseEntity {
    public AgentDefaultMsgEntity() {
        super(AgentDefaultMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "default_nation" , comment = "默认代理国家")
    private String defaultNation;

    @Column(name = "create_time" , comment = "创建时间")
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDefaultNation() {
        return defaultNation;
    }

    public void setDefaultNation(String defaultNation) {
        this.defaultNation = defaultNation;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
