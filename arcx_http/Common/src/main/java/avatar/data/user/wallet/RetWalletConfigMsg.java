package avatar.data.user.wallet;

/**
 * 钱包配置信息
 */
public class RetWalletConfigMsg {
    //SOL
    private double sol;

    //USDT
    private double usdt;

    //AXC
    private int axc;

    //ARCX
    private int arcx;

    public double getSol() {
        return sol;
    }

    public void setSol(double sol) {
        this.sol = sol;
    }

    public double getUsdt() {
        return usdt;
    }

    public void setUsdt(double usdt) {
        this.usdt = usdt;
    }

    public int getAxc() {
        return axc;
    }

    public void setAxc(int axc) {
        this.axc = axc;
    }

    public int getArcx() {
        return arcx;
    }

    public void setArcx(int arcx) {
        this.arcx = arcx;
    }
}
