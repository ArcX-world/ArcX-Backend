package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.LogUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;
import avatar.util.user.UserOnlineUtil;

/**
 * 设备房间信息数据接口
 */
public class ProductRoomDao {
    private static final ProductRoomDao instance = new ProductRoomDao();
    public static final ProductRoomDao getInstance(){
        return instance;
    }

    /**
     * 根据设备ID获取设备房间信息
     */
    public ProductRoomMsg loadByProductId(int productId) {
        //从缓存获取
        ProductRoomMsg msg = loadCache(productId);
        if(msg==null && productId>0){
            msg = ProductGamingUtil.initProductRoomMsg(productId);
            //设置设备的缓存信息
            setCache(productId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 获取设备房间信息
     */
    private ProductRoomMsg loadCache(int productId) {
        return (ProductRoomMsg) GameData.getCache().get(ProductPrefixMsg.PRODUCT_ROOM+"_"+productId);
    }

    /**
     * 设置设备房间信息
     */
    public void setCache(int productId, ProductRoomMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_ROOM+"_"+productId, msg);
    }

    /**
     * 删除设备上的玩家信息
     */
    public void delUser(int productId, int userId) {
        ProductRoomMsg msg = loadByProductId(productId);
        if (msg != null) {
            //游戏的玩家;
            if (msg.getGamingUserId() == userId) {
                LogUtil.getLogger().info("删除设备{}上游戏玩家{}的游戏信息----------", productId, userId);
                //设置设备缓存
                setCache(productId, ProductGamingUtil.initProductRoomMsg(productId));
                if(ProductUtil.isDollMachine(productId)) {
                    //初始化娃娃机缓存
                    ProductGamingUtil.initDollGamingMsg(productId);
                }
                //自研设备更新下机信息
                if(ProductUtil.isInnoProduct(productId)) {
                    ProductGamingUtil.updateInnoProductSettlementMsg(productId);
                }
                //初始化设备锁定缓存
                ProductGamingUtil.reInitProductAwardLockMsg(productId);
                //更新在线信息不是游戏玩家
                UserOnlineUtil.onlineMsgNoGaming(userId, productId);
                //更新上机玩家信息
                ProductGamingUserMsgDao.getInstance().setCache(productId, ProductGamingUtil.initProductGamingUserMsg(productId));
                //走游戏结束处理，做明细统计处理
                UserCostLogUtil.dealGamingResult(userId, productId);
                //添加操作日志
                UserOperateLogUtil.settlement(userId, productId);
            }
        }
    }
}
