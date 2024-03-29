package avatar.entity.nft;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="sell_gold_machine_msg" , comment = "售币机信息")
public class SellGoldMachineMsgEntity extends BaseEntity {
    public SellGoldMachineMsgEntity() {
        super(SellGoldMachineMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "nft_code" , comment = "NFT编号")
    private String nftCode;

    @Column(name = "nft_name" , comment = "售币机名称")
    private String nftName;

    @Column(name = "img_id" , comment = "售币机图片" )
    private int imgId;

    @Column(name = "lv" , comment = "等级" )
    private int lv;

    @Column(name = "exp_num" , comment = "当前经验" )
    private long expNum;

    @Column(name = "space_lv" , comment = "储币等级" )
    private int spaceLv;

    @Column(name = "income_lv" , comment = "入货等级" )
    private int incomeLv;

    @Column(name = "gold_num" , comment = "金币数量" )
    private long goldNum;

    @Column(name = "durability" , comment = "耐久度" )
    private long durability;

    @Column(name = "adv" , comment = "广告" )
    private long adv;

    @Column(name = "sale_commodity_type" , comment = "销售商品类型" )
    private int saleCommodityType;

    @Column(name = "sale_num" , comment = "销售价格" )
    private long saleNum;

    @Column(name = "operate_price" , comment = "经营价格(百万)" )
    private double operatePrice;

    @Column(name = "start_operate_time" , comment = "开始经营时间")
    private String startOperateTime;

    @Column(name = "sell_time" , comment = "售币次数" )
    private int sellTime;

    @Column(name = "status" , comment = "状态" )
    private int status;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNftCode() {
        return nftCode;
    }

    public void setNftCode(String nftCode) {
        this.nftCode = nftCode;
    }

    public String getNftName() {
        return nftName;
    }

    public void setNftName(String nftName) {
        this.nftName = nftName;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public long getExpNum() {
        return expNum;
    }

    public void setExpNum(long expNum) {
        this.expNum = expNum;
    }

    public int getSpaceLv() {
        return spaceLv;
    }

    public void setSpaceLv(int spaceLv) {
        this.spaceLv = spaceLv;
    }

    public int getIncomeLv() {
        return incomeLv;
    }

    public void setIncomeLv(int incomeLv) {
        this.incomeLv = incomeLv;
    }

    public long getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(long goldNum) {
        this.goldNum = goldNum;
    }

    public long getDurability() {
        return durability;
    }

    public void setDurability(long durability) {
        this.durability = durability;
    }

    public long getAdv() {
        return adv;
    }

    public void setAdv(long adv) {
        this.adv = adv;
    }

    public int getSaleCommodityType() {
        return saleCommodityType;
    }

    public void setSaleCommodityType(int saleCommodityType) {
        this.saleCommodityType = saleCommodityType;
    }

    public long getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(long saleNum) {
        this.saleNum = saleNum;
    }

    public double getOperatePrice() {
        return operatePrice;
    }

    public void setOperatePrice(double operatePrice) {
        this.operatePrice = operatePrice;
    }

    public String getStartOperateTime() {
        return startOperateTime;
    }

    public void setStartOperateTime(String startOperateTime) {
        this.startOperateTime = startOperateTime;
    }

    public int getSellTime() {
        return sellTime;
    }

    public void setSellTime(int sellTime) {
        this.sellTime = sellTime;
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
