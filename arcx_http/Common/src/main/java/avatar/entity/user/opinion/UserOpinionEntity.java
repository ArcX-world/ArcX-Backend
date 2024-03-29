package avatar.entity.user.opinion;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="user_opinion" , comment = "用户帮助与反馈")
public class UserOpinionEntity extends BaseEntity {
    public UserOpinionEntity() {
        super(UserOpinionEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "user_id" , comment = "玩家id" )
    private int userId;

    @Column(name = "opinion" , comment = "意见")
    private String opinion;

    @Column(name = "img_url" , comment = "照片")
    private String imgUrl;

    @Column(name = "deal_opinion" , comment = "处理意见")
    private String dealOpinion;

    @Column(name = "deal_back_user_id" , comment = "处理的后台用户ID")
    private int dealBackUserId;

    @Column(name = "status" , comment = "状态" )
    private int status;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDealOpinion() {
        return dealOpinion;
    }

    public void setDealOpinion(String dealOpinion) {
        this.dealOpinion = dealOpinion;
    }

    public int getDealBackUserId() {
        return dealBackUserId;
    }

    public void setDealBackUserId(int dealBackUserId) {
        this.dealBackUserId = dealBackUserId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
