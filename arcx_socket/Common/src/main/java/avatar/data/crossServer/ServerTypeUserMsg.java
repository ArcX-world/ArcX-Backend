package avatar.data.crossServer;

/**
 * 服务端玩家信息
 */
public class ServerTypeUserMsg {
    //玩家ID
    private int plyId;

    //玩家名称
    private String plyNm;

    //玩家头像
    private String plyPct;

    //服务端信息
    private int svTp;

    public int getPlyId() {
        return plyId;
    }

    public void setPlyId(int plyId) {
        this.plyId = plyId;
    }

    public String getPlyNm() {
        return plyNm;
    }

    public void setPlyNm(String plyNm) {
        this.plyNm = plyNm;
    }

    public String getPlyPct() {
        return plyPct;
    }

    public void setPlyPct(String plyPct) {
        this.plyPct = plyPct;
    }

    public int getSvTp() {
        return svTp;
    }

    public void setSvTp(int svTp) {
        this.svTp = svTp;
    }
}
