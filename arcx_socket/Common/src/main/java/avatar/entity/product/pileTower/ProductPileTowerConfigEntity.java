package avatar.entity.product.pileTower;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="product_pile_tower_config" , comment = "设备炼金塔堆塔配置")
public class ProductPileTowerConfigEntity extends BaseEntity {
    public ProductPileTowerConfigEntity() {
        super(ProductPileTowerConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "min_num" , comment = "最小值" )
    private int minNum;

    @Column(name = "max_num" , comment = "最大值" )
    private int maxNum;

    @Column(name = "interval_time" , comment = "间隔时间（秒）" )
    private int intervalTime;

    @Column(name = "award_img_id" , comment = "奖励图片ID" )
    private int awardImgId;

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

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getAwardImgId() {
        return awardImgId;
    }

    public void setAwardImgId(int awardImgId) {
        this.awardImgId = awardImgId;
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
