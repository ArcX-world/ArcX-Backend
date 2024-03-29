package avatar.util.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.innoMsg.InnoReceiveProductOperateMsg;
import avatar.data.product.innoMsg.InnoReceiveStartGameMsg;
import avatar.entity.product.innoMsg.InnoProductWinPrizeMsgEntity;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.product.award.ProductAwardTypeEnum;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.innoMsg.InnoProductBreakTypeEnum;
import avatar.global.enumMsg.product.innoMsg.InnoProductOperateTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.product.innoMsg.InnoProductWinPrizeMsgDao;
import avatar.module.product.innoMsg.SelfProductAwardMsgDao;
import avatar.module.product.innoMsg.SelfSpecialAwardMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.innoMsg.InnoProductAwardRefreshTimeTask;
import avatar.task.innoMsg.InnoReturnCoinTask;
import avatar.task.innoMsg.SyncInnoEndGameTask;
import avatar.task.innoMsg.SyncInnoProductOperateTask;
import avatar.task.product.innoMsg.InnoProductDragonWinPrizeDealTask;
import avatar.task.product.innoMsg.InnoProductWinPrizeDealTask;
import avatar.task.product.innoMsg.InnoProductWinPrizeNormalDealTask;
import avatar.task.product.innoMsg.InnoSpecialAwardTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.product.*;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserBalanceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 推币机自研设备接收到返回信息处理工具类
 */
public class CoinPusherInnoProductOperateUtil {
    /**
     * 开始游戏
     */
    public static void startGame(InnoReceiveStartGameMsg startGameMsg, int productId) {
        int status = ClientCode.SUCCESS.getCode();//成功
        int userId = startGameMsg.getUserId();//玩家ID
        //更新玩家投币倍率
        int userStartGameMulti = ProductGamingUtil.loadUserStartGameMulti(userId);
        if(userStartGameMulti>0){
            ProductGamingUtil.updateProductAwardLockMultiMsg(productId, userStartGameMulti);
        }
        //查询设备价格
        int cost = ProductUtil.productCost(productId);
        boolean flag = UserBalanceUtil.costUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode(), cost);
        if(flag){
            int preCoinWeight = InnoProductUtil.userCoinWeight(userId, productId);//处理之前的权重等级
            //初始化中奖信息
            initSelfProductAward(productId);
            //初始化币值缓存
            ProductGamingUtil.initProductCostCoinMsg(productId);
            //添加扣除的币值缓存
            ProductGamingUtil.addCostCoin(productId, cost);
            //开始游戏初始化信息
            InnerStartGameUtil.startInitMsg(productId, userId,
                    InnoParamsUtil.initResponseGeneralMsg(ProductUtil.loadProductAlias(productId), userId));
            //添加下机定时器
            InnerOffLineUtil.offLineTask(userId, productId);
            //推送投币操作
            pushCoinOperate(userId, productId);
            //做权重处理
            SyncInnoUtil.dealCoinWeight(preCoinWeight, userId, productId);
        }
        //推送客户端
        InnoSendWebsocketUtil.sendWebsocketMsg(userId,
                ProductOperationEnum.START_GAME.getCode(), status, 0,
                productId);
        if(ParamsUtil.isSuccess(status)){
            //添加操作日志
            UserOperateLogUtil.startGame(userId, productId);
        }else{
            //推送自研设备服务器退出设备通知
            SchedulerSample.delayed(5, new SyncInnoEndGameTask(
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnoParamsUtil.initInnoEndGameMsg(productId, ProductUtil.loadProductAlias(productId), userId)));
        }
    }

    /**
     * 初始化自研设备中奖
     */
    private static void initSelfProductAward(int productId) {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELF_PRODUCT_AWARD_LOCK + "_" + productId,
                2000);
        try {
            if (lock.lock()) {
                //自研设备中奖信息
                SelfProductAwardMsgDao.getInstance().setCache(productId, new ArrayList<>());
                //自研特殊中奖信息
                SelfSpecialAwardMsgDao.getInstance().setCache(productId, new HashMap<>());
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 投币操作
     */
    private static void pushCoinOperate(int userId, int productId) {
        //查询设备缓存信息
        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        SchedulerSample.delayed(5, new SyncInnoProductOperateTask(
                ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                InnoParamsUtil.initInnoProductOperateMsg(productId, ProductUtil.loadProductAlias(productId), userId,
                        productRoomMsg.getOnProductTime(), InnoProductOperateTypeEnum.PUSH_COIN.getCode(), false)));
    }

    /**
     * 设备操作
     */
    public static void productOperate(InnoReceiveProductOperateMsg productOperateMsg, int productId) {
        int operateType = productOperateMsg.getInnoProductOperateType();//操作类型
        if(operateType== InnoProductOperateTypeEnum.PUSH_COIN.getCode()){
            //投币
            pushCoin(productId);
        }else if(operateType== InnoProductOperateTypeEnum.GET_COIN.getCode()){
            //获得币
            getCoin(productOperateMsg, productId);
        }else if(operateType== InnoProductOperateTypeEnum.WIN_PRIZE.getCode()){
            //中奖
            winPrize(productOperateMsg, productId);
        }else if(operateType== InnoProductOperateTypeEnum.BREAK_DOWN.getCode()){
            //故障
            breakDown(productOperateMsg, productId);
        }
    }

    /**
     * 投币
     */
    private static void pushCoin(int productId) {
        LogUtil.getLogger().info("接收到自研设备（安卓板）设备{}的投币返回-------", productId);
    }

    /**
     * 获得币
     */
    private static void getCoin(InnoReceiveProductOperateMsg productOperateMsg, int productId) {
        int coinNum = productOperateMsg.getCoinNum();//获得币数量
        LogUtil.getLogger().info("接收到自研设备（安卓板）设备{}的获得币返回，数量{}-------", productId, coinNum);
        if(coinNum>0){
            if(ProductUtil.isOutStartGameTime(productId)) {
                //做获得币后续处理
                CoinPusherInnerReceiveDealUtil.getCoinDeal(productOperateMsg.getUserId(), productId, coinNum);
            }else{
                LogUtil.getLogger().error("自研设备{}获得币的时间未超出获得币间隔时间{}秒，不做得币处理-----", productId,
                        ProductConfigMsg.startGameGetCoinTime);
            }
        }
    }

    /**
     * 中奖
     */
    private static void winPrize(InnoReceiveProductOperateMsg productOperateMsg, int productId) {
        int productAwardType = productOperateMsg.getAwardType();//中奖类型
        LogUtil.getLogger().info("接收到自研设备（安卓板）设备{}的设备中奖返回，中奖类型{}-------", productId,
                ProductAwardTypeEnum.getNameByCode(productAwardType));
        int userId = productOperateMsg.getUserId();//玩家ID
        int awardNum = productOperateMsg.getAwardNum();//设备显示奖励游戏币
        int isStart = productOperateMsg.getIsStart();//是否开始中奖
        if(productAwardType>0) {
            if(StrUtil.strToList(ProductConfigMsg.innoNoProcessAward).contains(productAwardType)){
                //无中奖流程处理
                noProcessAwardDeal(userId, productId, awardNum, isStart, productAwardType);
            }if(StrUtil.strToList(ProductConfigMsg.innoJustDataAward).contains(productAwardType)){
                //只记录流程处理
                justDataAwardDeal(userId, productId, awardNum, isStart, productAwardType);
            }else{
                //中奖处理
                winPrizeDeal(userId, productId, productAwardType, awardNum, isStart);
            }
        }else{
            LogUtil.getLogger().error("自研设备{}玩家{}中奖，中奖类型{}，转换失败，查询不到对应的设备类型，后续处理失败--------",
                    productOperateMsg.getAlias(), userId, productAwardType);
        }
    }

    /**
     * 无中奖流程处理
     */
    private static void noProcessAwardDeal(int userId, int productId, int awardNum, int isStart, int productAwardType) {
        SchedulerSample.delayed(100, new InnoProductWinPrizeDealTask(
                productAwardType, productId, userId, awardNum, isStart));
    }

    /**
     * 只记录流程处理
     */
    private static void justDataAwardDeal(int userId, int productId, int awardNum, int isStart, int productAwardType) {
        //处理中奖信息
        dealWinPrizeMsg(userId, productId, productAwardType, awardNum, isStart);
        //通知中奖
        SchedulerSample.delayed(100, new InnoProductWinPrizeDealTask(
                productAwardType, productId, userId, awardNum, isStart));
    }

    /**
     * 处理中奖信息
     */
    private static long dealWinPrizeMsg(int userId, int productId, int productAwardType, int awardNum, int isStart) {
        long awardId = 0;//中奖信息ID
        boolean updateFlag = false;//是否更新
        if(isStart== YesOrNoEnum.NO.getCode()){
            //结束中奖
            InnoProductWinPrizeMsgEntity entity = InnoProductWinPrizeMsgDao.getInstance().loadMsg(userId,
                    productId, productAwardType);
            if(entity!=null && entity.getIsStart()== YesOrNoEnum.YES.getCode()){
                //更新数据
                entity.setAwardNum(awardNum);//奖励数量
                entity.setIsStart(isStart);//是否中奖中
                InnoProductWinPrizeMsgDao.getInstance().update(entity);
                updateFlag = true;
                awardId = entity.getId();//中奖信息ID
            }
        }
        if(!updateFlag) {
            //添加数据
            awardId = InnoProductWinPrizeMsgDao.getInstance().insert(ProductUtil.initInnoProductWinPrizeMsgEntity(
                    userId, productId, productAwardType, awardNum, isStart));
        }
        return awardId;
    }

    /**
     * 中奖处理
     */
    private static void winPrizeDeal(int userId, int productId, int productAwardType, int awardNum, int isStart) {
        //处理中奖信息
        long awardId = dealWinPrizeMsg(userId, productId, productAwardType, awardNum, isStart);
        //处理自研设备中奖刷新定时器
        dealSelfProductAwardTask(userId, productId, isStart, awardId);
        //特殊奖项处理
        if(StrUtil.strToList(ProductConfigMsg.innoSpecialAwardDeal).contains(productAwardType)) {
            SchedulerSample.delayed(5, new InnoSpecialAwardTask(
                    userId, productId, productAwardType, isStart));
        }
        if((productAwardType== ProductAwardTypeEnum.DRAGON_BALL.getCode() || isStart== YesOrNoEnum.YES.getCode())
                && productAwardType!=ProductAwardTypeEnum.AVERAGE_AWARD.getCode()) {
            //自研设备奖励通用处理
            SchedulerSample.delayed(100, new InnoProductWinPrizeNormalDealTask(
                    productId, userId, productAwardType));
        }
        if(productAwardType==ProductAwardTypeEnum.DRAGON_BALL.getCode()){
            //龙珠
            SchedulerSample.delayed(100, new InnoProductDragonWinPrizeDealTask(
                    productId, userId));
        }else{
            //其他自研奖励
            SchedulerSample.delayed(100, new InnoProductWinPrizeDealTask(
                    productAwardType, productId, userId, awardNum, isStart));
        }
    }

    /**
     * 处理自研设备中奖定时器
     */
    private static void dealSelfProductAwardTask(int userId, int productId, int isStart, long awardId) {
        if(isStart== YesOrNoEnum.YES.getCode()) {
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELF_PRODUCT_AWARD_LOCK + "_" + productId,
                    2000);
            try {
                if (lock.lock()) {
                    //查询中奖列表
                    List<Long> list = SelfProductAwardMsgDao.getInstance().loadByProductId(productId);
                    boolean openTaskFlag = list.size()==0;//是否开启定时器的标志
                    if(!list.contains(awardId)){
                        list.add(awardId);
                        //设置缓存
                        SelfProductAwardMsgDao.getInstance().setCache(productId, list);
                    }
                    if(openTaskFlag){
                        SchedulerSample.delayed(10, new InnoProductAwardRefreshTimeTask(productId, userId));
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 故障
     */
    private static void breakDown(InnoReceiveProductOperateMsg productOperateMsg, int productId) {
        int breakType = productOperateMsg.getBreakType();//故障类型
        LogUtil.getLogger().info("接收到自研设备（安卓板）设备{}的设备故障返回，故障类型{}-------", productId,
                InnoProductBreakTypeEnum.getNameByCode(breakType));
        if(breakType== InnoProductBreakTypeEnum.PUSH_COIN_ERROR.getCode()){
            //投币故障，返还游戏币
            SchedulerSample.delayed(5, new InnoReturnCoinTask(productId, productOperateMsg.getUserId(),
                    productOperateMsg.getOnProductTime()));
        }
        //添加维护信息
        ProductUtil.addRepairMsg(productId, breakType);
    }
}
