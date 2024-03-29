package avatar.entity.product.innoMsg;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="inno_product_win_prize_msg" , comment = "自研设备获奖信息")
public class InnoProductWinPrizeMsgEntity extends BaseEntity {
    public InnoProductWinPrizeMsgEntity() {
        super(InnoProductWinPrizeMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private long id;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "product_id" , comment = "设备ID" )
    private int productId;

    @Column(name = "award_type" , comment = "奖励类型" )
    private int awardType;

    @Column(name = "award_num" , comment = "设备显示奖励游戏币" )
    private int awardNum;

    @Column(name = "is_start" , comment = "是否中奖中" )
    private int isStart;

    @Column(name = "create_time" , comment = "创建时间")
    private String createTime;

    @Column(name = "update_time" , comment = "更新时间")
    private String updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public int getAwardNum() {
        return awardNum;
    }

    public void setAwardNum(int awardNum) {
        this.awardNum = awardNum;
    }

    public int getIsStart() {
        return isStart;
    }

    public void setIsStart(int isStart) {
        this.isStart = isStart;
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
