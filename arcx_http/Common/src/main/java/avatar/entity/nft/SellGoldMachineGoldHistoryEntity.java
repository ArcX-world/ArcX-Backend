package avatar.entity.nft;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="sell_gold_machine_gold_history" , comment = "售币机售币记录")
public class SellGoldMachineGoldHistoryEntity extends BaseEntity {
    public SellGoldMachineGoldHistoryEntity() {
        super(SellGoldMachineGoldHistoryEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "nft_code" , comment = "NFT编号")
    private String nftCode;

    @Column(name = "operate_price" , comment = "经营价格(百万)" )
    private double operatePrice;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "buy_user_id" , comment = "购币玩家ID" )
    private int buyUserId;

    @Column(name = "gold_num" , comment = "购币数量" )
    private long goldNum;

    @Column(name = "balance_num" , comment = "剩余数量" )
    private long balanceNum;

    @Column(name = "usdt_num" , comment = "USDT价格" )
    private double usdtNum;

    @Column(name = "tax" , comment = "营业税(%)" )
    private int tax;

    @Column(name = "tax_num" , comment = "费率" )
    private double taxNum;

    @Column(name = "real_earn" , comment = "实际收入" )
    private double realEarn;

    @Column(name = "create_time" , comment = "创建时间")
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNftCode() {
        return nftCode;
    }

    public void setNftCode(String nftCode) {
        this.nftCode = nftCode;
    }

    public double getOperatePrice() {
        return operatePrice;
    }

    public void setOperatePrice(double operatePrice) {
        this.operatePrice = operatePrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBuyUserId() {
        return buyUserId;
    }

    public void setBuyUserId(int buyUserId) {
        this.buyUserId = buyUserId;
    }

    public long getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(long goldNum) {
        this.goldNum = goldNum;
    }

    public long getBalanceNum() {
        return balanceNum;
    }

    public void setBalanceNum(long balanceNum) {
        this.balanceNum = balanceNum;
    }

    public double getUsdtNum() {
        return usdtNum;
    }

    public void setUsdtNum(double usdtNum) {
        this.usdtNum = usdtNum;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public double getTaxNum() {
        return taxNum;
    }

    public void setTaxNum(double taxNum) {
        this.taxNum = taxNum;
    }

    public double getRealEarn() {
        return realEarn;
    }

    public void setRealEarn(double realEarn) {
        this.realEarn = realEarn;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
