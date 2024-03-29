package avatar.entity.recharge.gold;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="recharge_gold_info" , comment = "充值金币信息")
public class RechargeGoldInfoEntity extends BaseEntity {
    public RechargeGoldInfoEntity() {
        super(RechargeGoldInfoEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "product_name" , comment = "商品名称" )
    private String productName;

    @Column(name = "price" , comment = "价格(USDT)" )
    private int price;

    @Column(name = "img_url" , comment = "商品图片")
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
