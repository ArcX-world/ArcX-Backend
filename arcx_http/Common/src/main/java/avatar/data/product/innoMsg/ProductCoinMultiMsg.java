package avatar.data.product.innoMsg;

import java.util.List;

/**
 * 设备投币倍率信息
 */
public class ProductCoinMultiMsg {
    //窗口图片
    private String wdwPct;

    //倍率列表
    private List<ProductCoinMultiLimitMsg> mulTbln;

    public String getWdwPct() {
        return wdwPct;
    }

    public void setWdwPct(String wdwPct) {
        this.wdwPct = wdwPct;
    }

    public List<ProductCoinMultiLimitMsg> getMulTbln() {
        return mulTbln;
    }

    public void setMulTbln(List<ProductCoinMultiLimitMsg> mulTbln) {
        this.mulTbln = mulTbln;
    }
}
