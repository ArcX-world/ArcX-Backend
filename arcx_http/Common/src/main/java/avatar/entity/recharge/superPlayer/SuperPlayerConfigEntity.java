package avatar.entity.recharge.superPlayer;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="super_player_config" , comment = "超级玩家配置")
public class SuperPlayerConfigEntity extends BaseEntity {
    public SuperPlayerConfigEntity() {
        super(SuperPlayerConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "img_url" , comment = "展示图片" )
    private String imgUrl;

    @Column(name = "price" , comment = "价格(USDT)" )
    private int price;

    @Column(name = "effect_day" , comment = "有效天数" )
    private int effectDay;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(int effectDay) {
        this.effectDay = effectDay;
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
