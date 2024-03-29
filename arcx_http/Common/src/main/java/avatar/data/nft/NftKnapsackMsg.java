package avatar.data.nft;

import java.util.List;

/**
 * NFT背包信息
 */
public class NftKnapsackMsg {
    //NFT编号
    private String nftCd;

    //名称
    private String nm;

    //图片
    private String pct;

    //NFT类型
    private int nftTp;

    //状态
    private int stat;

    //属性列表
    private List<ConciseNftAttributeMsg> atbTbln;

    public String getNftCd() {
        return nftCd;
    }

    public void setNftCd(String nftCd) {
        this.nftCd = nftCd;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getPct() {
        return pct;
    }

    public void setPct(String pct) {
        this.pct = pct;
    }

    public int getNftTp() {
        return nftTp;
    }

    public void setNftTp(int nftTp) {
        this.nftTp = nftTp;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public List<ConciseNftAttributeMsg> getAtbTbln() {
        return atbTbln;
    }

    public void setAtbTbln(List<ConciseNftAttributeMsg> atbTbln) {
        this.atbTbln = atbTbln;
    }
}
