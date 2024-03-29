package avatar.entity.product.productType;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="product_second_level_type" , comment = "设备二级分类")
public class ProductSecondLevelTypeEntity extends BaseEntity {
    public ProductSecondLevelTypeEntity() {
        super(ProductSecondLevelTypeEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "product_type" , comment = "设备类型" )
    private int productType;

    @Column(name = "name" , comment = "二级分类名称" )
    private String name;

    @Column(name = "en_name" , comment = "二级分类英文名称" )
    private String enName;

    @Column(name = "is_inno_product" , comment = "是否自研设备" )
    private int isInnoProduct;

    @Column(name = "is_lottery_product" , comment = "是否彩票设备" )
    private int isLotteryProduct;

    @Column(name = "instruct_type" , comment = "指令类型" )
    private String instructType;

    @Column(name = "server_off_line_time" , comment = "服务端下机时间（秒）" )
    private int serverOffLineTime;

    @Column(name = "close_socket_off_time" , comment = "断socket下机时间（秒）" )
    private int closeSocketOffTime;

    @Column(name = "create_time" , comment = "创建时间")
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public int getIsInnoProduct() {
        return isInnoProduct;
    }

    public void setIsInnoProduct(int isInnoProduct) {
        this.isInnoProduct = isInnoProduct;
    }

    public int getIsLotteryProduct() {
        return isLotteryProduct;
    }

    public void setIsLotteryProduct(int isLotteryProduct) {
        this.isLotteryProduct = isLotteryProduct;
    }

    public String getInstructType() {
        return instructType;
    }

    public void setInstructType(String instructType) {
        this.instructType = instructType;
    }

    public int getServerOffLineTime() {
        return serverOffLineTime;
    }

    public void setServerOffLineTime(int serverOffLineTime) {
        this.serverOffLineTime = serverOffLineTime;
    }

    public int getCloseSocketOffTime() {
        return closeSocketOffTime;
    }

    public void setCloseSocketOffTime(int closeSocketOffTime) {
        this.closeSocketOffTime = closeSocketOffTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
