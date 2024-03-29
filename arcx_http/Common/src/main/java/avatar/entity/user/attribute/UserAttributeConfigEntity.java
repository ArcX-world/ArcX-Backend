package avatar.entity.user.attribute;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="user_attribute_config" , comment = "玩家属性配置")
public class UserAttributeConfigEntity extends BaseEntity {
    public UserAttributeConfigEntity() {
        super(UserAttributeConfigEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "玩家id" )
    private int id;

    @Column(name = "lv" , comment = "等级" )
    private int lv;

    @Column(name = "lv_exp" , comment = "等级经验" )
    private long lvExp;

    @Column(name = "energy_max" , comment = "能量上限" )
    private long energyMax;

    @Column(name = "energy_axc" , comment = "能量升级axc" )
    private long energyAxc;

    @Column(name = "charging_second" , comment = "充能秒数" )
    private long chargingSecond;

    @Column(name = "charging_axc" , comment = "充能升级axc" )
    private long chargingAxc;

    @Column(name = "airdrop_coin" , comment = "每日空投游戏币(0.5h)" )
    private long airdropCoin;

    @Column(name = "airdrop_axc" , comment = "空投升级axc" )
    private long airdropAxc;

    @Column(name = "lucky_probability" , comment = "幸运概率" )
    private double luckyProbability;

    @Column(name = "lucky_axc" , comment = "幸运升级axc" )
    private long luckyAxc;

    @Column(name = "charm_addition" , comment = "魅力加成百分比" )
    private double charmAddition;

    @Column(name = "charm_axc" , comment = "幸运升级axc" )
    private long charmAxc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public long getLvExp() {
        return lvExp;
    }

    public void setLvExp(long lvExp) {
        this.lvExp = lvExp;
    }

    public long getEnergyMax() {
        return energyMax;
    }

    public void setEnergyMax(long energyMax) {
        this.energyMax = energyMax;
    }

    public long getEnergyAxc() {
        return energyAxc;
    }

    public void setEnergyAxc(long energyAxc) {
        this.energyAxc = energyAxc;
    }

    public long getChargingSecond() {
        return chargingSecond;
    }

    public void setChargingSecond(long chargingSecond) {
        this.chargingSecond = chargingSecond;
    }

    public long getChargingAxc() {
        return chargingAxc;
    }

    public void setChargingAxc(long chargingAxc) {
        this.chargingAxc = chargingAxc;
    }

    public long getAirdropCoin() {
        return airdropCoin;
    }

    public void setAirdropCoin(long airdropCoin) {
        this.airdropCoin = airdropCoin;
    }

    public long getAirdropAxc() {
        return airdropAxc;
    }

    public void setAirdropAxc(long airdropAxc) {
        this.airdropAxc = airdropAxc;
    }

    public double getLuckyProbability() {
        return luckyProbability;
    }

    public void setLuckyProbability(double luckyProbability) {
        this.luckyProbability = luckyProbability;
    }

    public long getLuckyAxc() {
        return luckyAxc;
    }

    public void setLuckyAxc(long luckyAxc) {
        this.luckyAxc = luckyAxc;
    }

    public double getCharmAddition() {
        return charmAddition;
    }

    public void setCharmAddition(double charmAddition) {
        this.charmAddition = charmAddition;
    }

    public long getCharmAxc() {
        return charmAxc;
    }

    public void setCharmAxc(long charmAxc) {
        this.charmAxc = charmAxc;
    }
}
