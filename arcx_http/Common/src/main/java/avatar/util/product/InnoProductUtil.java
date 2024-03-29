package avatar.util.product;

import avatar.data.product.innoMsg.ProductCoinMultiLimitMsg;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.product.innoMsg.InnoProductUnlockVersionDao;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 自研设备工具类
 */
public class InnoProductUtil {
    /**
     * 是否解锁版本
     */
    public static boolean isUnlockVersion(String version){
        boolean flag = false;
        if(!StrUtil.checkEmpty(version)) {
            List<String> list = InnoProductUnlockVersionDao.getInstance().loadMsg();
            if (list.size() > 0) {
                flag = list.contains(version);
            }
        }
        return flag;
    }

    /**
     * 倍率限制列表信息
     */
    public static List<ProductCoinMultiLimitMsg> productMultiLimitList(int userId, int productId,
            int secondType, List<Integer> multiList, boolean unlockFlag) {
        List<ProductCoinMultiLimitMsg> retList = new ArrayList<>();
        int lastMultiLevel = ProductGamingUtil.loadInnoLastMultiLevel(productId);//最近倍率等级
        for(int i=0;i<multiList.size();i++){
            int coinMulti = multiList.get(i);
            ProductCoinMultiLimitMsg msg = new ProductCoinMultiLimitMsg();
            msg.setMulAmt(coinMulti);//倍率
            int limitFlag = YesOrNoEnum.NO.getCode();//是否限制
            if(!unlockFlag){
                //倍率限制
                int multiLevel = multiList.size()-i;
                limitFlag = (lastMultiLevel>0 && multiLevel>lastMultiLevel)?
                        YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode();
            }
            msg.setLmFlg(limitFlag);//是否限制
            retList.add(msg);
        }
        return retList;
    }
}
