package avatar.data.product.gamingMsg;

import java.io.Serializable;

/**
 * 设备消费信息
 */
public class ProductCostCoinMsg implements Serializable {
    //设备ID
    private int productId;

    //累计增加币值
    private long sumAddCoin;

    //累计扣除币值
    private long sumCostCoin;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public long getSumAddCoin() {
        return sumAddCoin;
    }

    public void setSumAddCoin(long sumAddCoin) {
        this.sumAddCoin = sumAddCoin;
    }

    public long getSumCostCoin() {
        return sumCostCoin;
    }

    public void setSumCostCoin(long sumCostCoin) {
        this.sumCostCoin = sumCostCoin;
    }
}
