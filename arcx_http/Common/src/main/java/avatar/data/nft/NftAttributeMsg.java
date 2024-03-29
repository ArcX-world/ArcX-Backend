package avatar.data.nft;

/**
 * NFT属性信息
 */
public class NftAttributeMsg {
    //玩家属性类型
    private int atbTp;

    //等级
    private int lv;

    //下一等级
    private int nxLv;

    //是否可升级
    private int upFlg;

    //商品类型
    private int cmdTp;

    //商品数量
    private long csAmt;

    //当前等级数量
    private double lvAmt;

    //下一等级数量
    private double nxLvAmt;

    //当前进度分子
    private long sdNma;

    //当前进度分母
    private long sdDma;

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

    public int getNxLv() {
        return nxLv;
    }

    public void setNxLv(int nxLv) {
        this.nxLv = nxLv;
    }

    public int getUpFlg() {
        return upFlg;
    }

    public void setUpFlg(int upFlg) {
        this.upFlg = upFlg;
    }

    public int getCmdTp() {
        return cmdTp;
    }

    public void setCmdTp(int cmdTp) {
        this.cmdTp = cmdTp;
    }

    public long getCsAmt() {
        return csAmt;
    }

    public void setCsAmt(long csAmt) {
        this.csAmt = csAmt;
    }

    public double getLvAmt() {
        return lvAmt;
    }

    public void setLvAmt(double lvAmt) {
        this.lvAmt = lvAmt;
    }

    public double getNxLvAmt() {
        return nxLvAmt;
    }

    public void setNxLvAmt(double nxLvAmt) {
        this.nxLvAmt = nxLvAmt;
    }

    public long getSdNma() {
        return sdNma;
    }

    public void setSdNma(long sdNma) {
        this.sdNma = sdNma;
    }

    public long getSdDma() {
        return sdDma;
    }

    public void setSdDma(long sdDma) {
        this.sdDma = sdDma;
    }
}
