package avatar.data.user.attribute;

/**
 * 玩家属性信息
 */
public class UserAttributeMsg {
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
    private String lvAmt;

    //下一等级数量
    private String nxLvAmt;

    //下次刷新时间
    private long lfTm;

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

    public String getLvAmt() {
        return lvAmt;
    }

    public void setLvAmt(String lvAmt) {
        this.lvAmt = lvAmt;
    }

    public String getNxLvAmt() {
        return nxLvAmt;
    }

    public void setNxLvAmt(String nxLvAmt) {
        this.nxLvAmt = nxLvAmt;
    }

    public long getLfTm() {
        return lfTm;
    }

    public void setLfTm(long lfTm) {
        this.lfTm = lfTm;
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
