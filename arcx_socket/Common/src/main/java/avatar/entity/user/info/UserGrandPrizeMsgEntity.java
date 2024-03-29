package avatar.entity.user.info;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="user_grand_prize_msg" , comment = "玩家设备大奖信息")
public class UserGrandPrizeMsgEntity extends BaseEntity {
    public UserGrandPrizeMsgEntity() {
        super(UserGrandPrizeMsgEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "user_id" , comment = "玩家ID")
    private int userId;

    @Column(name = "pile_tower" , comment = "炼金塔堆塔")
    private int pileTower;

    @Column(name = "dragon_ball" , comment = "龙珠")
    private int dragonBall;

    @Column(name = "room_rank_first" , comment = "房间排行榜第一名")
    private int roomRankFirst;

    @Column(name = "room_rank_second" , comment = "房间排行榜第二名")
    private int roomRankSecond;

    @Column(name = "room_rank_third" , comment = "房间排行榜第三名")
    private int roomRankThird;

    @Column(name = "free_game" , comment = "免费游戏")
    private int freeGame;

    @Column(name = "gem" , comment = "宝石游戏")
    private int gem;

    @Column(name = "prize_wheel_grand" , comment = "大奖转盘-grand奖池")
    private int prizeWheelGrand;

    @Column(name = "prize_wheel_major" , comment = "大奖转盘-major奖池")
    private int prizeWheelMajor;

    @Column(name = "prize_wheel_minor" , comment = "大奖转盘-grand奖池")
    private int prizeWheelMinor;

    @Column(name = "agypt_box" , comment = "埃及开箱子")
    private int agyptBox;

    @Column(name = "gossip" , comment = "八卦")
    private int gossip;

    @Column(name = "hero_battle" , comment = "三国战斗")
    private int heroBattle;

    @Column(name = "thunder" , comment = "闪电")
    private int thunder;

    @Column(name = "jackpot_kingkong" , comment = "金刚大奖转盘")
    private int jackpotKingkong;

    @Column(name = "jackpot_hero" , comment = "三国大奖转盘")
    private int jackpotHero;

    @Column(name = "whistle" , comment = "口哨")
    private int whistle;

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

    public int getPileTower() {
        return pileTower;
    }

    public void setPileTower(int pileTower) {
        this.pileTower = pileTower;
    }

    public int getDragonBall() {
        return dragonBall;
    }

    public void setDragonBall(int dragonBall) {
        this.dragonBall = dragonBall;
    }

    public int getRoomRankFirst() {
        return roomRankFirst;
    }

    public void setRoomRankFirst(int roomRankFirst) {
        this.roomRankFirst = roomRankFirst;
    }

    public int getRoomRankSecond() {
        return roomRankSecond;
    }

    public void setRoomRankSecond(int roomRankSecond) {
        this.roomRankSecond = roomRankSecond;
    }

    public int getRoomRankThird() {
        return roomRankThird;
    }

    public void setRoomRankThird(int roomRankThird) {
        this.roomRankThird = roomRankThird;
    }

    public int getFreeGame() {
        return freeGame;
    }

    public void setFreeGame(int freeGame) {
        this.freeGame = freeGame;
    }

    public int getGem() {
        return gem;
    }

    public void setGem(int gem) {
        this.gem = gem;
    }

    public int getPrizeWheelGrand() {
        return prizeWheelGrand;
    }

    public void setPrizeWheelGrand(int prizeWheelGrand) {
        this.prizeWheelGrand = prizeWheelGrand;
    }

    public int getPrizeWheelMajor() {
        return prizeWheelMajor;
    }

    public void setPrizeWheelMajor(int prizeWheelMajor) {
        this.prizeWheelMajor = prizeWheelMajor;
    }

    public int getPrizeWheelMinor() {
        return prizeWheelMinor;
    }

    public void setPrizeWheelMinor(int prizeWheelMinor) {
        this.prizeWheelMinor = prizeWheelMinor;
    }

    public int getAgyptBox() {
        return agyptBox;
    }

    public void setAgyptBox(int agyptBox) {
        this.agyptBox = agyptBox;
    }

    public int getGossip() {
        return gossip;
    }

    public void setGossip(int gossip) {
        this.gossip = gossip;
    }

    public int getHeroBattle() {
        return heroBattle;
    }

    public void setHeroBattle(int heroBattle) {
        this.heroBattle = heroBattle;
    }

    public int getThunder() {
        return thunder;
    }

    public void setThunder(int thunder) {
        this.thunder = thunder;
    }

    public int getJackpotKingkong() {
        return jackpotKingkong;
    }

    public void setJackpotKingkong(int jackpotKingkong) {
        this.jackpotKingkong = jackpotKingkong;
    }

    public int getJackpotHero() {
        return jackpotHero;
    }

    public void setJackpotHero(int jackpotHero) {
        this.jackpotHero = jackpotHero;
    }

    public int getWhistle() {
        return whistle;
    }

    public void setWhistle(int whistle) {
        this.whistle = whistle;
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
