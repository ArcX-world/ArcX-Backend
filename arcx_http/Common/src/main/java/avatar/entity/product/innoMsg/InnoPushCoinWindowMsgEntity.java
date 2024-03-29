package avatar.entity.product.innoMsg;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="inno_push_coin_window_msg" , comment = "自研投币倍率窗口")
public class InnoPushCoinWindowMsgEntity extends BaseEntity {
    public InnoPushCoinWindowMsgEntity() {
        super(InnoPushCoinWindowMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "second_type" , comment = "设备二级分类" )
    private int secondType;

    @Column(name = "img_url" , comment = "窗口图片")
    private String imgUrl;

    @Column(name = "award_lock_out_time" , comment = "中奖锁定超时时间" )
    private int awardLockOutTime;

    @Column(name = "multi_cooling_time" , comment = "倍率冷却时间" )
    private int multiCoolingTime;

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

    public int getSecondType() {
        return secondType;
    }

    public void setSecondType(int secondType) {
        this.secondType = secondType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getAwardLockOutTime() {
        return awardLockOutTime;
    }

    public void setAwardLockOutTime(int awardLockOutTime) {
        this.awardLockOutTime = awardLockOutTime;
    }

    public int getMultiCoolingTime() {
        return multiCoolingTime;
    }

    public void setMultiCoolingTime(int multiCoolingTime) {
        this.multiCoolingTime = multiCoolingTime;
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
