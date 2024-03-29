package avatar.entity.product.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="product_info" , comment = "设备信息")
public class ProductInfoEntity extends BaseEntity {
    public ProductInfoEntity() {
        super(ProductInfoEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "product_name" , comment = "设备名称" )
    private String productName;

    @Column(name = "img_product_id" , comment = "设备图标ID" )
    private int imgProductId;

    @Column(name = "live_type" , comment = "播流类型" )
    private int liveType;

    @Column(name = "live_url" , comment = "视频链接" )
    private String liveUrl;

    @Column(name = "web_live_url" , comment = "web播流" )
    private String webLiveUrl;

    @Column(name = "sequence" , comment = "排序" )
    private int sequence;

    @Column(name = "second_sequence" , comment = "二级排序" )
    private int secondSequence;

    @Column(name = "alias" , comment = "设备号" )
    private String alias;

    @Column(name = "product_type" , comment = "设备类型" )
    private int productType;

    @Column(name = "second_type" , comment = "设备二级分类" )
    private int secondType;

    @Column(name = "cost_commodity_type" , comment = "消耗商品类型" )
    private int costCommodityType;

    @Column(name = "cost" , comment = "一次消耗的币" )
    private int cost;

    @Column(name = "descr" , comment = "描述" )
    private String descr;

    @Column(name = "img_product_detail_id" , comment = "设备详情图片ID" )
    private int imgProductDetailId;

    @Column(name = "img_product_present_id" , comment = "设备礼品图片ID" )
    private int imgProductPresentId;

    @Column(name = "ip" , comment = "服务器IP" )
    private String ip;

    @Column(name = "port" , comment = "服务器端口" )
    private String port;

    @Column(name = "default_music" , comment = "是否开启默认音乐" )
    private int defaultMusic;

    @Column(name = "commodity_type" , comment = "奖励类型" )
    private int commodityType;

    @Column(name = "award_id" , comment = "奖励ID" )
    private int awardId;

    @Column(name = "award_min_num" , comment = "奖励最小值" )
    private int awardMinNum;

    @Column(name = "award_max_num" , comment = "奖励最大值" )
    private int awardMaxNum;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getImgProductId() {
        return imgProductId;
    }

    public void setImgProductId(int imgProductId) {
        this.imgProductId = imgProductId;
    }

    public int getLiveType() {
        return liveType;
    }

    public void setLiveType(int liveType) {
        this.liveType = liveType;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getWebLiveUrl() {
        return webLiveUrl;
    }

    public void setWebLiveUrl(String webLiveUrl) {
        this.webLiveUrl = webLiveUrl;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSecondSequence() {
        return secondSequence;
    }

    public void setSecondSequence(int secondSequence) {
        this.secondSequence = secondSequence;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getSecondType() {
        return secondType;
    }

    public void setSecondType(int secondType) {
        this.secondType = secondType;
    }

    public int getCostCommodityType() {
        return costCommodityType;
    }

    public void setCostCommodityType(int costCommodityType) {
        this.costCommodityType = costCommodityType;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public int getImgProductDetailId() {
        return imgProductDetailId;
    }

    public void setImgProductDetailId(int imgProductDetailId) {
        this.imgProductDetailId = imgProductDetailId;
    }

    public int getImgProductPresentId() {
        return imgProductPresentId;
    }

    public void setImgProductPresentId(int imgProductPresentId) {
        this.imgProductPresentId = imgProductPresentId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getDefaultMusic() {
        return defaultMusic;
    }

    public void setDefaultMusic(int defaultMusic) {
        this.defaultMusic = defaultMusic;
    }

    public int getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(int commodityType) {
        this.commodityType = commodityType;
    }

    public int getAwardId() {
        return awardId;
    }

    public void setAwardId(int awardId) {
        this.awardId = awardId;
    }

    public int getAwardMinNum() {
        return awardMinNum;
    }

    public void setAwardMinNum(int awardMinNum) {
        this.awardMinNum = awardMinNum;
    }

    public int getAwardMaxNum() {
        return awardMaxNum;
    }

    public void setAwardMaxNum(int awardMaxNum) {
        this.awardMaxNum = awardMaxNum;
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
