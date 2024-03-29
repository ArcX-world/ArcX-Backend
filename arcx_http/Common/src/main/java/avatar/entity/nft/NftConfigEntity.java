package avatar.entity.nft;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="nft_config" , comment = "NFT配置")
public class NftConfigEntity extends BaseEntity {
    public NftConfigEntity() {
        super(NftConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "store_show_img" , comment = "商店展示图")
    private String storeShowImg;

    @Column(name = "sale_tax" , comment = "交易费(%)" )
    private int saleTax;

    @Column(name = "operate_tax" , comment = "营业税(%)" )
    private int operateTax;

    @Column(name = "durability" , comment = "耐久度" )
    private long durability;

    @Column(name = "ad_weight" , comment = "广告权重系数" )
    private long adWeight;

    @Column(name = "sale_weight" , comment = "出售权重系数" )
    private long saleWeight;

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

    public String getStoreShowImg() {
        return storeShowImg;
    }

    public void setStoreShowImg(String storeShowImg) {
        this.storeShowImg = storeShowImg;
    }

    public int getSaleTax() {
        return saleTax;
    }

    public void setSaleTax(int saleTax) {
        this.saleTax = saleTax;
    }

    public int getOperateTax() {
        return operateTax;
    }

    public void setOperateTax(int operateTax) {
        this.operateTax = operateTax;
    }

    public long getDurability() {
        return durability;
    }

    public void setDurability(long durability) {
        this.durability = durability;
    }

    public long getAdWeight() {
        return adWeight;
    }

    public void setAdWeight(long adWeight) {
        this.adWeight = adWeight;
    }

    public long getSaleWeight() {
        return saleWeight;
    }

    public void setSaleWeight(long saleWeight) {
        this.saleWeight = saleWeight;
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
