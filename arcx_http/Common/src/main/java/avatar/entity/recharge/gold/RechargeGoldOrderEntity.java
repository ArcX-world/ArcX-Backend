package avatar.entity.recharge.gold;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="recharge_gold_order" , comment = "金币充值信息")
public class RechargeGoldOrderEntity extends BaseEntity {
    public RechargeGoldOrderEntity() {
        super(RechargeGoldOrderEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "commodity_id" , comment = "商品ID" )
    private int commodityId;

    @Column(name = "commodity_num" , comment = "商品数量" )
    private long commodityNum;

    @Column(name = "order_sn" , comment = "订单号" )
    private String orderSn;

    @Column(name = "recharge_id" , comment = "平台交易号" )
    private String rechargeId;

    @Column(name = "pay_type" , comment = "支付类型" )
    private int payType;

    @Column(name = "product_id" , comment = "设备ID" )
    private int productId;

    @Column(name = "price" , comment = "价格(USDT)" )
    private int price;

    @Column(name = "status" , comment = "状态" )
    private int status;

    @Column(name = "create_time" , comment = "创建时间" )
    private String createTime;

    @Column(name = "update_time" , comment = "更新时间" )
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public long getCommodityNum() {
        return commodityNum;
    }

    public void setCommodityNum(long commodityNum) {
        this.commodityNum = commodityNum;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(String rechargeId) {
        this.rechargeId = rechargeId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
