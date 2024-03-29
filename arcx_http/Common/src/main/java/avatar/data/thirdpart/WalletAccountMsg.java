package avatar.data.thirdpart;

/**
 * solana钱包信息
 */
public class WalletAccountMsg {
    //公钥
    private String pk;

    //私钥
    private String sk;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }
}
