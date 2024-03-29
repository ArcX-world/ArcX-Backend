package avatar.entity.activity.dragonTrain.user;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="dragon_train_ball_user_get_history" , comment = "龙珠玛丽机龙珠玩家获得历史")
public class DragonTrainBallUserGetHistoryEntity extends BaseEntity {
    public DragonTrainBallUserGetHistoryEntity() {
        super(DragonTrainBallUserGetHistoryEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "product_id" , comment = "设备ID" )
    private int productId;

    @Column(name = "current_num" , comment = "当前数量" )
    private int currentNum;

    @Column(name = "award_coin" , comment = "奖励游戏币数" )
    private int awardCoin;

    @Column(name = "is_trigger" , comment = "是否触发玛丽机" )
    private int isTrigger;

    @Column(name = "create_time" , comment = "创建时间" )
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public int getAwardCoin() {
        return awardCoin;
    }

    public void setAwardCoin(int awardCoin) {
        this.awardCoin = awardCoin;
    }

    public int getIsTrigger() {
        return isTrigger;
    }

    public void setIsTrigger(int isTrigger) {
        this.isTrigger = isTrigger;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
