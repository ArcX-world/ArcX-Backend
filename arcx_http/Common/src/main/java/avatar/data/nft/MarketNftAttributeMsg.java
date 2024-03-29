package avatar.data.nft;

/**
 * NFT属性信息
 */
public class MarketNftAttributeMsg {
    //玩家属性类型
    private int atbTp;

    //等级
    private int lv;

    //属性信息
    private String atbMsg;

    public int getAtbTp() {
        return atbTp;
    }

    public void setAtbTp(int atbTp) {
        this.atbTp = atbTp;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public String getAtbMsg() {
        return atbMsg;
    }

    public void setAtbMsg(String atbMsg) {
        this.atbMsg = atbMsg;
    }
}
