package avatar.entity.nft;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="sell_gold_machine_up_config" , comment = "售币机升级配置")
public class SellGoldMachineUpConfigEntity extends BaseEntity {
    public SellGoldMachineUpConfigEntity() {
        super(SellGoldMachineUpConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "lv" , comment = "等级" )
    private int lv;

    @Column(name = "up_exp" , comment = "升级经验(分钟)" )
    private long upExp;

    @Column(name = "stored_max" , comment = "储币上限" )
    private long storedMax;

    @Column(name = "income_discount" , comment = "入货折扣" )
    private double incomeDiscount;

    @Column(name = "lv_axc" , comment = "等级AXC" )
    private long lvAxc;

    @Column(name = "lv_arcx" , comment = "等级ARCX" )
    private long lvArcx;

    @Column(name = "discount_axc" , comment = "折扣AXC" )
    private long discountAxc;

    @Column(name = "stored_axc" , comment = "储币AXC" )
    private long storedAxc;

    @Column(name = "create_time" , comment = "创建时间")
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public long getUpExp() {
        return upExp;
    }

    public void setUpExp(long upExp) {
        this.upExp = upExp;
    }

    public long getStoredMax() {
        return storedMax;
    }

    public void setStoredMax(long storedMax) {
        this.storedMax = storedMax;
    }

    public double getIncomeDiscount() {
        return incomeDiscount;
    }

    public void setIncomeDiscount(double incomeDiscount) {
        this.incomeDiscount = incomeDiscount;
    }

    public long getLvAxc() {
        return lvAxc;
    }

    public void setLvAxc(long lvAxc) {
        this.lvAxc = lvAxc;
    }

    public long getLvArcx() {
        return lvArcx;
    }

    public void setLvArcx(long lvArcx) {
        this.lvArcx = lvArcx;
    }

    public long getDiscountAxc() {
        return discountAxc;
    }

    public void setDiscountAxc(long discountAxc) {
        this.discountAxc = discountAxc;
    }

    public long getStoredAxc() {
        return storedAxc;
    }

    public void setStoredAxc(long storedAxc) {
        this.storedAxc = storedAxc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
