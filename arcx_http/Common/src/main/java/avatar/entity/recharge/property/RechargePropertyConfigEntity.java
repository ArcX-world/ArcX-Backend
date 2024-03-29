package avatar.entity.recharge.property;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="recharge_property_config" , comment = "道具充值配置")
public class RechargePropertyConfigEntity extends BaseEntity {
    public RechargePropertyConfigEntity() {
        super(RechargePropertyConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "show_num" , comment = "展示数量" )
    private int showNum;

    @Column(name = "refresh_price" , comment = "价格" )
    private int refreshPrice;

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

    public int getShowNum() {
        return showNum;
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
    }

    public int getRefreshPrice() {
        return refreshPrice;
    }

    public void setRefreshPrice(int refreshPrice) {
        this.refreshPrice = refreshPrice;
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
