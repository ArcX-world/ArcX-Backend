package avatar.entity.basic.img;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="img_product_grand_prize_msg" , comment = "设备大奖图片信息")
public class ImgProductGrandPrizeMsgEntity extends BaseEntity {
    public ImgProductGrandPrizeMsgEntity() {
        super(ImgProductGrandPrizeMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "name" , comment = "图片名称")
    private String name;

    @Column(name = "en_concise_name" , comment = "英文简称")
    private String enConciseName;

    @Column(name = "img_url" , comment = "图片url")
    private String imgUrl;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnConciseName() {
        return enConciseName;
    }

    public void setEnConciseName(String enConciseName) {
        this.enConciseName = enConciseName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
