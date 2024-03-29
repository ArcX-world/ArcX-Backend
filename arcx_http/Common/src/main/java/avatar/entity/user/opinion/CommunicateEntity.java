package avatar.entity.user.opinion;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="communicate_msg" , comment = "联系信息")
public class CommunicateEntity extends BaseEntity {
    public CommunicateEntity() {
        super(CommunicateEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "email" , comment = "邮箱")
    private String email;

    @Column(name = "deal_back_user_id" , comment = "后台处理用户ID" )
    private int dealBackUserId;

    @Column(name = "deal_flag" , comment = "是否已处理" )
    private int dealFlag;

    @Column(name = "comment_msg" , comment = "备注")
    private String commentMsg;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDealBackUserId() {
        return dealBackUserId;
    }

    public void setDealBackUserId(int dealBackUserId) {
        this.dealBackUserId = dealBackUserId;
    }

    public int getDealFlag() {
        return dealFlag;
    }

    public void setDealFlag(int dealFlag) {
        this.dealFlag = dealFlag;
    }

    public String getCommentMsg() {
        return commentMsg;
    }

    public void setCommentMsg(String commentMsg) {
        this.commentMsg = commentMsg;
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
