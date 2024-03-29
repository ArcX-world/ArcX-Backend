package avatar.data.product.gamingMsg;

/**
 * 彩票信息
 */
public class LotteryMsg {
    //添加彩票数
    private int addLotteryNum;

    //当前彩票数
    private int num;

    //彩票数上限
    private int maxNum;

    //添加的游戏币数
    private int addCoin;

    public int getAddLotteryNum() {
        return addLotteryNum;
    }

    public void setAddLotteryNum(int addLotteryNum) {
        this.addLotteryNum = addLotteryNum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getAddCoin() {
        return addCoin;
    }

    public void setAddCoin(int addCoin) {
        this.addCoin = addCoin;
    }
}
