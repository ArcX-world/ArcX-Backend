package avatar.data.crossServer;

import java.io.Serializable;

/**
 * 跨服查询设备奖励信息
 */
public class CrossServerSearchProductPrizeMsg implements Serializable {
    //炼金塔堆塔
    private int pileTower;

    //龙珠
    private int allDragon;

    //房间排行榜第一名
    private int roomRankFirst;

    //房间排行榜第二名
    private int roomRankSecond;

    //房间排行榜第三名
    private int roomRankThird;

    //免费游戏
    private int freeGame;

    //宝石游戏
    private int gem;

    //大奖转盘-minord奖池
    private int jackpotMinor;

    //大奖转盘-major奖池
    private int jackpotMajor;

    //大奖转盘-grand奖池
    private int jackpotGrand;

    //集卡数
    private int collectCard;

    //金字塔
    private int agyptBox;

    //八卦
    private int gossip;

    //三国战斗
    private int heroBattle;

    //闪电
    private int thunder;

    //金刚大奖转盘
    private int kingkongJackpot;

    //三国大奖转盘
    private int heroJackpot;

    //口哨
    private int whistle;

    public int getPileTower() {
        return pileTower;
    }

    public void setPileTower(int pileTower) {
        this.pileTower = pileTower;
    }

    public int getAllDragon() {
        return allDragon;
    }

    public void setAllDragon(int allDragon) {
        this.allDragon = allDragon;
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

    public int getJackpotMinor() {
        return jackpotMinor;
    }

    public void setJackpotMinor(int jackpotMinor) {
        this.jackpotMinor = jackpotMinor;
    }

    public int getJackpotMajor() {
        return jackpotMajor;
    }

    public void setJackpotMajor(int jackpotMajor) {
        this.jackpotMajor = jackpotMajor;
    }

    public int getJackpotGrand() {
        return jackpotGrand;
    }

    public void setJackpotGrand(int jackpotGrand) {
        this.jackpotGrand = jackpotGrand;
    }

    public int getCollectCard() {
        return collectCard;
    }

    public void setCollectCard(int collectCard) {
        this.collectCard = collectCard;
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

    public int getKingkongJackpot() {
        return kingkongJackpot;
    }

    public void setKingkongJackpot(int kingkongJackpot) {
        this.kingkongJackpot = kingkongJackpot;
    }

    public int getHeroJackpot() {
        return heroJackpot;
    }

    public void setHeroJackpot(int heroJackpot) {
        this.heroJackpot = heroJackpot;
    }

    public int getWhistle() {
        return whistle;
    }

    public void setWhistle(int whistle) {
        this.whistle = whistle;
    }
}
