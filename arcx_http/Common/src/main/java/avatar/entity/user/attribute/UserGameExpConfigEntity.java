package avatar.entity.user.attribute;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="user_game_exp_config" , comment = "玩家游戏经验配置")
public class UserGameExpConfigEntity extends BaseEntity {
    public UserGameExpConfigEntity() {
        super(UserGameExpConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "玩家id" )
    private int id;

    @Column(name = "coin_num" , comment = "游戏币" )
    private long coinNum;

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

    public long getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(long coinNum) {
        this.coinNum = coinNum;
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
