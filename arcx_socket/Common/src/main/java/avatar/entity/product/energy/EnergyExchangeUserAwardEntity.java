package avatar.entity.product.energy;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="energy_exchange_award" , comment = "能量兑换奖励")
public class EnergyExchangeUserAwardEntity extends BaseEntity {
    public EnergyExchangeUserAwardEntity() {
        super(EnergyExchangeUserAwardEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "history_id" , comment = "历史ID" )
    private long historyId;

    @Column(name = "award_type" , comment = "奖励类型" )
    private int awardType;

    @Column(name = "award_id" , comment = "奖励ID" )
    private int awardId;

    @Column(name = "min_num" , comment = "奖励最小值" )
    private int minNum;

    @Column(name = "max_num" , comment = "奖励最大值" )
    private int maxNum;

    @Column(name = "award_probability" , comment = "中奖概率" )
    private int awardProbability;

    @Column(name = "total_probability" , comment = "总概率" )
    private int totalProbability;

    @Column(name = "award_num" , comment = "奖励数量" )
    private int awardNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
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

    public int getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(int awardNum) {
        this.awardNum = awardNum;
    }
}
