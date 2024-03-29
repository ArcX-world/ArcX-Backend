package avatar.service.innoMsg;

import avatar.data.product.innoMsg.InnoDragonMsg;
import avatar.data.product.innoMsg.InnoGetCoinMsg;
import avatar.data.product.innoMsg.InnoProductAwardMsg;
import avatar.data.product.innoMsg.InnoProductMsg;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.info.ProductAliasDao;
import avatar.service.jedis.RedisLock;
import avatar.task.product.general.RefreshProductMsgTask;
import avatar.task.product.innoMsg.InnoVoiceNoticeTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;
import com.alibaba.fastjson.JSONObject;

/**
 * 自研设备接口实现类
 */
public class InnoProductService {
    /**
     * 订阅的设备信息
     */
    public static void describeProductMsg(InnoProductMsg innoProductMsg) {
        String alias = innoProductMsg.getAlias();//设备号
        int productId = ProductAliasDao.getInstance().loadByAlias(alias);
        if(productId>0){
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                    2000);
            try {
                if (lock.lock()) {
                    //更新设备信息
                    ProductGamingUtil.updateGamingUserMsg(productId, innoProductMsg);
                    //刷新房间信息
                    SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }finally {
                lock.unlock();
            }
        }
    }

    /**
     * 订阅的获得币信息
     */
    public static void describeGetCoinMsg(InnoGetCoinMsg innoGetCoinMsg) {
        int productId = ProductAliasDao.getInstance().loadByAlias(innoGetCoinMsg.getAlias());
        if(productId>0) {
            int cost = ProductUtil.productCost(productId);//设备花费
            if(cost>0) {
                //推币机
                getCoin(innoGetCoinMsg, productId, cost);
            }
        }
    }

    /**
     * 推币机获得币
     */
    private static void getCoin(InnoGetCoinMsg innoGetCoinMsg, int productId, int cost) {
        int userId = innoGetCoinMsg.getUserId();//玩家ID
        int serverSideType = innoGetCoinMsg.getServerSideType();//服务端类型
        int coinNum = innoGetCoinMsg.getRetNum();//游戏币个数
    }

    /**
     * 订阅的设备中奖信息处理
     */
    public static void describProductAwardMsg(InnoProductAwardMsg innoProductAwardMsg) {
        int productId = ProductAliasDao.getInstance().loadByAlias(innoProductAwardMsg.getAlias());
    }

    /**
     * 设备声音通知
     */
    public static void voiceNotice(JSONObject jsonMap) {
        String alias = InnoParamsUtil.loadStringParams(jsonMap, "alias");
        if(!StrUtil.checkEmpty(alias)) {
            int productId = ProductAliasDao.getInstance().loadByAlias(alias);//设备ID
            if(productId>0) {
                SchedulerSample.delayed(1,
                        new InnoVoiceNoticeTask(jsonMap, productId));
            }
        }else{
            LogUtil.getLogger().error("接收到声音推送信息，但是解析不到对应的设备号---------");
        }
    }

    /**
     * 龙珠订阅处理
     */
    public static void describeDragonMsg(InnoDragonMsg innoDragonMsg) {
        int productId = ProductAliasDao.getInstance().loadByAlias(innoDragonMsg.getAlias());
        if(productId>0) {
            //todo 龙珠处理
        }
    }
}
