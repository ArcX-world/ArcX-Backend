package avatar.entity.activity.dragonTrain.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="dragon_train_wheel_icon_msg" , comment = "龙珠玛丽机转轮图标信息")
public class DragonTrainWheelIconMsgEntity extends BaseEntity {
    public DragonTrainWheelIconMsgEntity() {
        super(DragonTrainWheelIconMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "award_img_id" , comment = "奖励图标ID" )
    private int awardImgId;

    @Column(name = "commodity_type" , comment = "奖励类型" )
    private int commodityType;

    @Column(name = "gift_id" , comment = "奖励ID" )
    private int giftId;

    @Column(name = "award_min_num" , comment = "中奖最小值" )
    private int awardMinNum;

    @Column(name = "award_max_num" , comment = "中奖最大值" )
    private int awardMaxNum;

    @Column(name = "award_probability" , comment = "中奖几率" )
    private int awardProbability;

    @Column(name = "total_probability" , comment = "总几率" )
    private int totalProbability;

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

    public int getAwardImgId() {
        return awardImgId;
    }

    public void setAwardImgId(int awardImgId) {
        this.awardImgId = awardImgId;
    }

    public int getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(int commodityType) {
        this.commodityType = commodityType;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
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

    public int getAwardProbability() {
        return awardProbability;
    }

    public void setAwardProbability(int awardProbability) {
        this.awardProbability = awardProbability;
    }

    public int getTotalProbability() {
        return totalProbability;
    }

    public void setTotalProbability(int totalProbability) {
        this.totalProbability = totalProbability;
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
