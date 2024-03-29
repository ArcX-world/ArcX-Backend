package avatar.entity.user.attribute;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="user_attribute_msg" , comment = "玩家属性信息")
public class UserAttributeMsgEntity extends BaseEntity {
    public UserAttributeMsgEntity() {
        super(UserAttributeMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "玩家id" )
    private int id;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "user_level" , comment = "玩家等级" )
    private int userLevel;

    @Column(name = "user_level_exp" , comment = "玩家等级经验" )
    private long userLevelExp;

    @Column(name = "energy_level" , comment = "能量等级" )
    private int energyLevel;

    @Column(name = "energy_num" , comment = "能量数量" )
    private long energyNum;

    @Column(name = "charging_level" , comment = "充能等级" )
    private int chargingLevel;

    @Column(name = "charging_time" , comment = "充能时间")
    private String chargingTime;

    @Column(name = "airdrop_level" , comment = "空投等级" )
    private int airdropLevel;

    @Column(name = "lucky_level" , comment = "幸运等级" )
    private int luckyLevel;

    @Column(name = "charm_level" , comment = "魅力等级" )
    private int charmLevel;

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

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public long getUserLevelExp() {
        return userLevelExp;
    }

    public void setUserLevelExp(long userLevelExp) {
        this.userLevelExp = userLevelExp;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public long getEnergyNum() {
        return energyNum;
    }

    public void setEnergyNum(long energyNum) {
        this.energyNum = energyNum;
    }

    public int getChargingLevel() {
        return chargingLevel;
    }

    public void setChargingLevel(int chargingLevel) {
        this.chargingLevel = chargingLevel;
    }

    public String getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(String chargingTime) {
        this.chargingTime = chargingTime;
    }

    public int getAirdropLevel() {
        return airdropLevel;
    }

    public void setAirdropLevel(int airdropLevel) {
        this.airdropLevel = airdropLevel;
    }

    public int getLuckyLevel() {
        return luckyLevel;
    }

    public void setLuckyLevel(int luckyLevel) {
        this.luckyLevel = luckyLevel;
    }

    public int getCharmLevel() {
        return charmLevel;
    }

    public void setCharmLevel(int charmLevel) {
        this.charmLevel = charmLevel;
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
