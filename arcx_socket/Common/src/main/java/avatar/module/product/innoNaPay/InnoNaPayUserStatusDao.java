package avatar.module.product.innoNaPay;

import avatar.data.product.innoNaPay.InnoNaPayUserStatusMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.InnoNaPayUtil;

/**
 * 玩家自研支付状态数据接口
 */
public class InnoNaPayUserStatusDao {
    private static final InnoNaPayUserStatusDao instance = new InnoNaPayUserStatusDao();
    public static final InnoNaPayUserStatusDao getInstance() {
        return instance;
    }

    /**
     * 查询信息
     */
    public InnoNaPayUserStatusMsg loadMsg(int userId) {
        //从缓存获取
        InnoNaPayUserStatusMsg msg = loadCache(userId);
        if (msg == null) {
            msg = InnoNaPayUtil.initInnoNaPayUserStatusMsg(userId);
            //设置缓存
            setCache(userId, msg);
        }
        //处理返回信息
        return InnoNaPayUtil.dealInnoNaPayUserStatusMsg(msg);
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private InnoNaPayUserStatusMsg loadCache(int userId) {
        return (InnoNaPayUserStatusMsg) GameData.getCache().get(ProductPrefixMsg.INNO_NA_PAY_USER_STATUS + "_" + userId);
    }

    /**
     * 设置缓存
     */
    public void setCache(int userId, InnoNaPayUserStatusMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.INNO_NA_PAY_USER_STATUS + "_" + userId, msg);
    }

    /**
     * 删除缓存
     */
    public void removeCache(int userId) {
        GameData.getCache().removeCache(ProductPrefixMsg.INNO_NA_PAY_USER_STATUS + "_" + userId);
    }

}
