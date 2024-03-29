package avatar.util.user;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductStatusEnum;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.user.info.UserInfoDao;
import avatar.util.LogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.TimeUtil;

/**
 * 封禁工具类
 */
public class ForbidUtil {
    /**
     * 检测禁入设备
     */
    public static int checkForbidProduct(int userId, int productId) {
        int status = ClientCode.SUCCESS.getCode();//成功
        boolean flag = true;
        //玩家状态
        if(flag && userId>0){
            UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
            if(userInfoEntity!=null && userInfoEntity.getStatus()!= UserStatusEnum.NORMAL.getCode()){//查询玩家封禁提示
                flag = false;
                LogUtil.getLogger().info("玩家{}禁入设备{}，禁入类型：玩家状态异常------", userId, productId);
            }
        }
        //判断是否设备正常
        if(flag && productId>0){
            ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
            if(productInfoEntity!=null &&
                    productInfoEntity.getStatus()!= ProductStatusEnum.NORMAL.getCode()){
                flag = false;
                LogUtil.getLogger().info("玩家{}禁入设备{}，禁入类型：设备异常------", userId, productId);
            }
        }
        if(!flag){
            status = ClientCode.SYSTEM_ERROR.getCode();//系统错误
        }
        return status;
    }

    /**
     * 检测游戏中禁入设备
     */
    public static int checkGamingForbidProduct(int userId, int productId, boolean unlockFlag) {
        int status = ClientCode.SUCCESS.getCode();//成功
        boolean flag = true;
        //玩家状态
        if(userId>0){
            UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
            if(userInfoEntity!=null && userInfoEntity.getStatus()!= UserStatusEnum.NORMAL.getCode()){//查询玩家封禁提示
                flag = false;
                LogUtil.getLogger().info("玩家{}禁入设备{}，禁入类型：玩家状态异常------", userId, productId);
            }
        }
        //判断是否在其他设备上
        if(flag && productId>0){
            int pId = UserOnlineUtil.loadGamingProduct(userId);
            if (pId>0 && productId!=pId) {
                flag = false;
                LogUtil.getLogger().info("玩家{}禁入设备{}，禁入类型：在其他设备{}上------", userId, productId, pId);
            }
        }
        //判断是否设备正常
        if(flag && productId>0){
            ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
            if(productInfoEntity!=null &&
                    productInfoEntity.getStatus()!= ProductStatusEnum.NORMAL.getCode()){
                flag = false;
                LogUtil.getLogger().info("玩家{}禁入设备{}，禁入类型：设备异常------", userId, productId);
            }
        }
        if(!flag){
            status = ClientCode.SYSTEM_ERROR.getCode();//系统错误
        }
        return status;
    }
}
