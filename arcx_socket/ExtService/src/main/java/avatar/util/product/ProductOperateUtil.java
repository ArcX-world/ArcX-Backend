package avatar.util.product;

import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.service.product.ProductSocketOperateService;
import avatar.task.product.general.RefreshProductMsgTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserOnlineUtil;

import java.util.List;

/**
 * 设备操作工具类
 */
public class ProductOperateUtil {
    /**
     * 从机器下线
     */
    public static void offLineProduct(int userId, int productId) {
        //修改设备玩家信息
        ProductRoomDao.getInstance().delUser(productId, userId);
        //处理在线信息
        if(UserOnlineUtil.isOnline(userId)) {
            //更新在线信息不是游戏玩家
            UserOnlineUtil.onlineMsgNoGaming(userId, productId);
        }else{
            //玩家不在线，删除在线信息
            UserOnlineMsgDao.getInstance().delete(userId, productId);
        }
        //刷新房间信息
        SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
    }

    /**
     * 处理踢出设备
     */
    public static void kickOut(int userId, int productId) {
        //查询在线信息
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        if(list.size()>0){
            list.forEach(entity->{
                if(entity!=null && entity.getProductId()==productId && entity.getIsGaming()== YesOrNoEnum.YES.getCode()){
                    kickOutUser(userId, productId);
                }
            });
        }
    }

    /**
     * 踢出玩家
     */
    private static void kickOutUser(int userId, int productId) {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                //查询设备信息
                int productType = ProductUtil.loadProductType(productId);//设备类型
                if (productType == ProductTypeEnum.PUSH_COIN_MACHINE.getCode()) {
                    //推币机
                    int operate = ProductOperationEnum.OFF_LINE.getCode();//操作类型：结算
                    LogUtil.getLogger().info("服务器主动踢出推币机设备{}玩家{}----", productId, userId);
                    //设备操作
                    ProductSocketOperateService.coinPusherOperate(productId, operate, userId);
                } else {
                    LogUtil.getLogger().info("服务器主动踢出设备{}玩家{}，设备类型为{}，不处理----", productId, userId, productType);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
