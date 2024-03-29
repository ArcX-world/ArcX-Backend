package avatar.entity.user.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="user_default_head_img" , comment = "玩家默认头像信息")
public class UserDefaultHeadImgEntity extends BaseEntity {
    public UserDefaultHeadImgEntity() {
        super(UserDefaultHeadImgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "玩家id" )
    private int id;

    @Column(name = "img_url" , comment = "头像")
    private String imgUrl;

    @Column(name = "is_cross_platform" , comment = "是否跨平台")
    private int isCrossPlatform;

    @Column(name = "create_time" , comment = "创建时间")
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getIsCrossPlatform() {
        return isCrossPlatform;
    }

    public void setIsCrossPlatform(int isCrossPlatform) {
        this.isCrossPlatform = isCrossPlatform;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
