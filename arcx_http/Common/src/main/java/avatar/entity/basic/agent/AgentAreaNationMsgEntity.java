package avatar.entity.basic.agent;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="agent_area_nation_msg" , comment = "代理区域国家信息")
public class AgentAreaNationMsgEntity extends BaseEntity {
    public AgentAreaNationMsgEntity() {
        super(AgentAreaNationMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "area_id" , comment = "区域ID" )
    private int areaId;

    @Column(name = "nation_name" , comment = "国家名称" )
    private String nationName;

    @Column(name = "create_time" , comment = "创建时间" )
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
