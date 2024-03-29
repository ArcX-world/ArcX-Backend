package avatar.entity.product.innoNaPay;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="inno_na_pay_specify_coin_weight" , comment = "自研设备付费NA指定权重")
public class InnoNaPaySpecifyCoinWeightEntity extends BaseEntity {
    public InnoNaPaySpecifyCoinWeightEntity() {
        super(InnoNaPaySpecifyCoinWeightEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "second_type" , comment = "设备二级分类" )
    private int secondType;

    @Column(name = "level" , comment = "权重等级" )
    private int level;

    @Column(name = "na_num" , comment = "na值" )
    private int naNum;

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

    public int getSecondType() {
        return secondType;
    }

    public void setSecondType(int secondType) {
        this.secondType = secondType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNaNum() {
        return naNum;
    }

    public void setNaNum(int naNum) {
        this.naNum = naNum;
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
