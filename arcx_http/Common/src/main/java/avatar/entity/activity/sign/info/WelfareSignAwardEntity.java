package avatar.entity.activity.sign.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="welfare_sign_award" , comment = "福利签到奖励")
public class WelfareSignAwardEntity extends BaseEntity {
    public WelfareSignAwardEntity() {
        super(WelfareSignAwardEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "day" , comment = "天数" )
    private int day;

    @Column(name = "award_type" , comment = "奖励类型" )
    private int awardType;

    @Column(name = "award_id" , comment = "奖励ID" )
    private int awardId;

    @Column(name = "award_img_id" , comment = "奖励图片ID")
    private int awardImgId;

    @Column(name = "award_num" , comment = "奖励数量" )
    private int awardNum;

    @Column(name = "sequence" , comment = "排序" )
    private int sequence;

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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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

    public int getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(int awardNum) {
        this.awardNum = awardNum;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
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
