package avatar.entity.activity.dragonTrain.user;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="dragon_train_trigger_award_msg" , comment = "龙珠玛丽机触发中奖信息")
public class DragonTrainTriggerAwardMsgEntity extends BaseEntity {
    public DragonTrainTriggerAwardMsgEntity() {
        super(DragonTrainTriggerAwardMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "trigger_id" , comment = "触发ID" )
    private int triggerId;

    @Column(name = "commodity_type" , comment = "奖励类型" )
    private int commodityType;

    @Column(name = "gift_id" , comment = "奖励ID" )
    private int giftId;

    @Column(name = "result_award_num" , comment = "最终奖励值" )
    private int resultAwardNum;

    @Column(name = "award_min_num" , comment = "奖励最小值" )
    private int awardMinNum;

    @Column(name = "award_max_num" , comment = "奖励最大值" )
    private int awardMaxNum;

    @Column(name = "award_probability" , comment = "中奖几率" )
    private int awardProbability;

    @Column(name = "total_probability" , comment = "总几率" )
    private int totalProbability;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(int triggerId) {
        this.triggerId = triggerId;
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

    public int getResultAwardNum() {
        return resultAwardNum;
    }

    public void setResultAwardNum(int resultAwardNum) {
        this.resultAwardNum = resultAwardNum;
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
}
