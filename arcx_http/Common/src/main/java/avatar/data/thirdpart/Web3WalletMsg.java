package avatar.data.thirdpart;

/**
 * solana钱包信息
 */
public class Web3WalletMsg {
    //账号信息
    private WalletAccountMsg account;

    //ACX账号
    private String ata;

    public WalletAccountMsg getAccount() {
        return account;
    }

    public void setAccount(WalletAccountMsg account) {
        this.account = account;
    }

    public String getAta() {
        return ata;
    }

    public void setAta(String ata) {
        this.ata = ata;
    }
}
