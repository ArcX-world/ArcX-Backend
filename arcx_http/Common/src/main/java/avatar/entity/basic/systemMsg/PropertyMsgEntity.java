package avatar.entity.basic.systemMsg;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="property_msg" , comment = "道具信息")
public class PropertyMsgEntity extends BaseEntity {
    public PropertyMsgEntity() {
        super(PropertyMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "property_type" , comment = "奖励类型" )
    private int propertyType;

    @Column(name = "property_use_type" , comment = "使用类型" )
    private int propertyUseType;

    @Column(name = "num" , comment = "类型数量" )
    private int num;

    @Column(name = "name" , comment = "道具名称")
    private String name;

    @Column(name = "desc" , comment = "描述")
    private String desc;

    @Column(name = "img_url" , comment = "道具图片")
    private String imgUrl;

    @Column(name = "sequence" , comment = "排序" )
    private int sequence;

    @Column(name = "active_flag" , comment = "是否激活" )
    private int activeFlag;

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

    public int getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(int propertyType) {
        this.propertyType = propertyType;
    }

    public int getPropertyUseType() {
        return propertyUseType;
    }

    public void setPropertyUseType(int propertyUseType) {
        this.propertyUseType = propertyUseType;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(int activeFlag) {
        this.activeFlag = activeFlag;
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
