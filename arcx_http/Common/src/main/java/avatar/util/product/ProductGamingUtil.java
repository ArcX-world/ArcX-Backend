package avatar.util.product;

import avatar.data.product.gamingMsg.InnoProductOffLineMsg;
import avatar.data.product.gamingMsg.ProductAwardLockMsg;
import avatar.entity.product.innoMsg.InnoPushCoinWindowMsgEntity;
import avatar.global.basicConfig.basic.ProductConfigMsg;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.product.info.ProductAwardLockDao;
import avatar.module.product.info.ProductSettlementMsgDao;
import avatar.module.product.innoMsg.InnoPushCoinMultiDao;
import avatar.module.product.innoMsg.InnoPushCoinWindowDao;
import avatar.util.system.TimeUtil;

import java.util.Collections;
import java.util.List;

/**
 * 设备游戏信息
 */
public class ProductGamingUtil {
    /**
     * 获取最近倍率
     */
    public static int loadInnoLastMultiLevel(int productId){
        int multiLevel = 0;
        long coolingTime = loadMultiCollingTime(productId);//设备冷却时间
        if(coolingTime>0) {
            //获取自研设备下机信息
            InnoProductOffLineMsg msg = ProductSettlementMsgDao.getInstance().loadByProductId(productId);
            if(msg!=null && (TimeUtil.getNowTime()-msg.getOffLineTime())<coolingTime){
                multiLevel = msg.getMulti();
            }
        }
        return multiLevel;
    }

    /**
     * 获取倍率冷却时间
     */
    private static long loadMultiCollingTime(int productId) {
        long collTime = 0;
        int secondType = ProductUtil.loadSecondType(productId);
        if(secondType>0){
            //查询倍率窗口
            InnoPushCoinWindowMsgEntity entity = InnoPushCoinWindowDao.getInstance().loadBySecondType(secondType);
            collTime = entity==null?0:entity.getMultiCoolingTime();
        }
        return collTime*1000;
    }

    /**
     * 填充自研设备最近下线信息
     */
    public static InnoProductOffLineMsg initInnoProductOffLineMsg(int productId) {
        InnoProductOffLineMsg msg = new InnoProductOffLineMsg();
        msg.setProductId(productId);//设备ID
        msg.setOffLineTime(0);//最近下线时间
        msg.setMulti(loadMultiLevel(productId));//倍率等级
        return msg;
    }

    /**
     * 查询投币倍率等级
     */
    public static int loadMultiLevel(int productId) {
        int multiLevel = 1;//倍率等级：默认1
        ProductAwardLockMsg msg = ProductAwardLockDao.getInstance().loadByProductId(productId);
        int coinMul = msg.getCoinMulti();//投币倍率
        if(coinMul>0){
            //查询倍率信息
            List<Integer> multiList = InnoPushCoinMultiDao.getInstance().loadBySecondType(
                    ProductUtil.loadSecondType(productId));
            if(multiList.size()>0){
                Collections.sort(multiList);
                for(int i=0;i<multiList.size();i++){
                    if(coinMul==multiList.get(i)){
                        multiLevel += i;
                        break;
                    }
                }
            }
        }
        //最大倍率处理
        multiLevel = Math.min(multiLevel, ProductConfigMsg.maxCoinMultiLevel);
        return multiLevel;
    }

    /**
     * 初始化设备中奖锁定信息
     */
    public static ProductAwardLockMsg initProductAwardLockMsg(int productId) {
        ProductAwardLockMsg msg = new ProductAwardLockMsg();
        msg.setProductId(productId);//设备ID
        msg.setCoinMulti(0);//投币倍率
        msg.setIsAwardLock(YesOrNoEnum.NO.getCode());//是否中奖锁定
        msg.setLockTime(0);//锁定时间
        return msg;
    }

}
