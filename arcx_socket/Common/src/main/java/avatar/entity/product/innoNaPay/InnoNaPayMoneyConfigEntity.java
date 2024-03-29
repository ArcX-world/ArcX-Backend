package avatar.entity.product.innoNaPay;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="inno_na_pay_money_config" , comment = "自研设备付费NA金额配置")
public class InnoNaPayMoneyConfigEntity extends BaseEntity {
    public InnoNaPayMoneyConfigEntity() {
        super(InnoNaPayMoneyConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "time_range" , comment = "时间范围（天）" )
    private int timeRange;

    @Column(name = "money" , comment = "累计金额" )
    private double money;

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

    public int getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(int timeRange) {
        this.timeRange = timeRange;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
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
