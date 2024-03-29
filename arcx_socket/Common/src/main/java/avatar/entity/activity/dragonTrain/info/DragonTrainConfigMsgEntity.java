package avatar.entity.activity.dragonTrain.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="dragon_train_config_msg" , comment = "龙珠玛丽机配置信息")
public class DragonTrainConfigMsgEntity extends BaseEntity {
    public DragonTrainConfigMsgEntity() {
        super(DragonTrainConfigMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "award_num" , comment = "中奖数量" )
    private int awardNum;

    @Column(name = "again_time" , comment = "again上限次数" )
    private int againTime;

    @Column(name = "is_repeat_award" , comment = "是否可中同类型奖品" )
    private int isRepeatAward;

    @Column(name = "award_img_id" , comment = "奖励图片" )
    private int awardImgId;

    @Column(name = "dragon_min_num" , comment = "龙珠奖励最小游戏币" )
    private int dragonMinNum;

    @Column(name = "dragon_max_num" , comment = "龙珠奖励最大游戏币" )
    private int dragonMaxNum;

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

    public int getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(int awardNum) {
        this.awardNum = awardNum;
    }

    public int getAgainTime() {
        return againTime;
    }

    public void setAgainTime(int againTime) {
        this.againTime = againTime;
    }

    public int getIsRepeatAward() {
        return isRepeatAward;
    }

    public void setIsRepeatAward(int isRepeatAward) {
        this.isRepeatAward = isRepeatAward;
    }

    public int getAwardImgId() {
        return awardImgId;
    }

    public void setAwardImgId(int awardImgId) {
        this.awardImgId = awardImgId;
    }

    public int getDragonMinNum() {
        return dragonMinNum;
    }

    public void setDragonMinNum(int dragonMinNum) {
        this.dragonMinNum = dragonMinNum;
    }

    public int getDragonMaxNum() {
        return dragonMaxNum;
    }

    public void setDragonMaxNum(int dragonMaxNum) {
        this.dragonMaxNum = dragonMaxNum;
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
