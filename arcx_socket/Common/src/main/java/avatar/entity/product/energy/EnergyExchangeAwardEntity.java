package avatar.entity.product.energy;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="energy_exchange_award" , comment = "能量兑换奖励")
public class EnergyExchangeAwardEntity extends BaseEntity {
    public EnergyExchangeAwardEntity() {
        super(EnergyExchangeAwardEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "config_id" , comment = "配置ID" )
    private int configId;

    @Column(name = "award_type" , comment = "奖励类型" )
    private int awardType;

    @Column(name = "award_id" , comment = "奖励ID" )
    private int awardId;

    @Column(name = "award_img_id" , comment = "奖励图片" )
    private int awardImgId;

    @Column(name = "min_num" , comment = "奖励最小值" )
    private int minNum;

    @Column(name = "max_num" , comment = "奖励最大值" )
    private int maxNum;

    @Column(name = "award_probability" , comment = "中奖概率" )
    private int awardProbability;

    @Column(name = "total_probability" , comment = "总概率" )
    private int totalProbability;

    @Column(name = "max_time" , comment = "上限次数" )
    private long maxTime;

    @Column(name = "trigger_time" , comment = "已触发次数" )
    private long triggerTime;

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

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public int getAwardId() {
        return awardId;
    }

    public void setAwardId(int awardId) {
        this.awardId = awardId;
    }

    public int getAwardImgId() {
        return awardImgId;
    }

    public void setAwardImgId(int awardImgId) {
        this.awardImgId = awardImgId;
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

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public long getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(long triggerTime) {
        this.triggerTime = triggerTime;
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
