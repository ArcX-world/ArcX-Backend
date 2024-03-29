package avatar.util.product;

import avatar.data.product.gamingMsg.*;
import avatar.data.product.general.ResponseGeneralMsg;
import avatar.data.product.innoMsg.InnoProductMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.product.innoMsg.InnoPushCoinWindowMsgEntity;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.product.info.GamingStateEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.crossServer.ServerSideUserMsgDao;
import avatar.module.product.gaming.*;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.product.innoMsg.InnoPushCoinWindowDao;
import avatar.module.product.productType.LotteryCoinPercentDao;
import avatar.module.user.product.UserLotteryMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.product.general.AddUserGameExpTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserNoticePushUtil;

import java.util.Collections;
import java.util.List;

/**
 * 设备游戏信息
 */
public class ProductGamingUtil {
    /**
     * 初始化设备房间信息
     */
    public static ProductRoomMsg initProductRoomMsg(int productId) {
        LogUtil.getLogger().info("初始化设备{}的游戏信息----------", productId);
        ProductRoomMsg msg = new ProductRoomMsg();//设备信息
        msg.setProductId(productId);//设备ID
        msg.setGamingUserId(0);//上机的玩家ID
        msg.setOnProductTime(0);//上机时间
        msg.setPushCoinOnTime(0);//设备刷新时间
        return msg;
    }

    /**
     * 初始化娃娃机信息
     */
    public static DollGamingMsg initDollGamingMsg(int productId) {
        DollGamingMsg msg = new DollGamingMsg();
        msg.setProductId(productId);//设备ID
        msg.setTime(0);//游戏回合
        msg.setInitalization(false);//是否初始化
        msg.setCatch(false);//是否已经操作
        msg.setGamingState(GamingStateEnum.NO_PROPLE.getCode());//游戏环节：无人上机
        //更新缓存
        DollGamingMsgDao.getInstance().setCache(productId, msg);
        return msg;
    }

    /**
     * 更新自研设备下机信息
     */
    public static void updateInnoProductSettlementMsg(int productId) {
        InnoProductOffLineMsg msg = ProductSettlementMsgDao.getInstance().loadByProductId(productId);
        msg.setOffLineTime(TimeUtil.getNowTime());//下线时间
        msg.setMulti(loadMultiLevel(productId));//倍率等级
        ProductSettlementMsgDao.getInstance().setCache(productId, msg);
    }

    /**
     * 更新自研设备下机信息
     */
    private static void updateInnoProductSettlementMsg(int productId, int productMulti) {
        InnoProductOffLineMsg msg = ProductSettlementMsgDao.getInstance().loadByProductId(productId);
        msg.setOffLineTime(TimeUtil.getNowTime());//下线时间
        msg.setMulti(productMulti);//倍率
        ProductSettlementMsgDao.getInstance().setCache(productId, msg);
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
        msg.setIsAwardLock(YesOrNoEnum.NO.getCode());//是否中奖锁定
        msg.setLockTime(0);//锁定时间
        return msg;
    }

    /**
     * 重新初始化设备中奖锁定信息
     */
    public static void reInitProductAwardLockMsg(int productId) {
        ProductAwardLockMsg msg = ProductAwardLockDao.getInstance().loadByProductId(productId);
        msg.setProductId(productId);//设备ID
        msg.setIsAwardLock(YesOrNoEnum.NO.getCode());//是否中奖锁定
        msg.setLockTime(0);//锁定时间
        ProductAwardLockDao.getInstance().setCache(productId,msg);
    }

    /**
     * 初始化设备游戏中玩家信息
     */
    public static ProductGamingUserMsg initProductGamingUserMsg(int productId) {
        ProductGamingUserMsg msg = new ProductGamingUserMsg();
        msg.setServerSideType(0);//服务端类型
        msg.setProductId(productId);//设备ID
        msg.setUserId(0);//玩家ID
        msg.setUserName("");//玩家昵称
        msg.setImgUrl("");//玩家头像
        return msg;
    }

    /**
     * 填充设备消费游戏币信息
     */
    public static ProductCostCoinMsg initProductCostCoinMsg(int productId) {
        ProductCostCoinMsg msg = new ProductCostCoinMsg();
        msg.setProductId(productId);//设备ID
        msg.setSumAddCoin(0);//累计增加币值
        msg.setSumCostCoin(0);//累计扣除币值
        return msg;
    }

    /**
     * 去掉扣除币值
     */
    public static long costCostCoin(int productId, int cost){
        long oriSumCostCoin = 0;//初始总扣除数量
        RedisLock pushCoinLock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_PUSH_COIN_LOCK+"_"+productId,
                2000);
        try {
            if (pushCoinLock.lock()) {
                ProductCostCoinMsg msg = ProductCostCoinMsgDao.getInstance().loadByProductId(productId);
                if (msg != null) {
                    oriSumCostCoin = msg.getSumCostCoin();
                    long sumCostCoin = Math.max(0, (msg.getSumCostCoin() - cost));
                    msg.setSumCostCoin(sumCostCoin);
                    ProductCostCoinMsgDao.getInstance().setCache(productId, msg);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            pushCoinLock.unlock();
        }
        return oriSumCostCoin;
    }

    /**
     * 添加扣除币值
     */
    public static void addCostCoin(int productId, int cost){
        if(cost>0) {
            RedisLock pushCoinLock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_PUSH_COIN_LOCK + "_" + productId,
                    2000);
            try {
                if (pushCoinLock.lock()) {
                    ProductCostCoinMsg msg = ProductCostCoinMsgDao.getInstance().loadByProductId(productId);
                    if (msg != null) {
                        msg.setSumCostCoin(msg.getSumCostCoin() + cost);
                        ProductCostCoinMsgDao.getInstance().setCache(productId, msg);
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                pushCoinLock.unlock();
            }
        }
    }

    /**
     * 游戏中玩家
     */
    public static int loadGamingUserId(int productId) {
        int userId = 0;
        //游戏信息
        ProductGamingUserMsg gamingUserMsg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
        if(gamingUserMsg!=null && gamingUserMsg.getUserId()>0 &&
                CrossServerMsgUtil.isArcxServer(gamingUserMsg.getServerSideType())){
            userId = gamingUserMsg.getUserId();
        }
        return userId;
    }

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
     * 根据等级获取倍率
     */
    public static int loadCoinMulti(int productId, int multiLevel) {
        int coinMulti = 0;
        List<Integer> multiList = InnoPushCoinMultiDao.getInstance().loadBySecondType(
                ProductUtil.loadSecondType(productId));//倍率列表
        if(multiList.size()>0){
            if(multiLevel>multiList.size()){
                //超出等级了
                coinMulti = multiList.get(0);
            }else{
                for(int i=0;i<multiList.size();i++){
                    if((multiList.size()-i)==multiLevel){
                        coinMulti = multiList.get(i);
                        break;
                    }
                }
            }
        }
        return coinMulti;
    }

    /**
     * 是否设备中奖锁定
     */
    public static boolean isProductAwardLock(int productId){
        boolean flag = false;
        //获取锁定信息
        ProductAwardLockMsg msg = ProductAwardLockDao.getInstance().loadByProductId(productId);
        if(msg.getIsAwardLock()==YesOrNoEnum.YES.getCode()){
            //判断是否超时
            //查询窗口信息
            InnoPushCoinWindowMsgEntity windowMsgEntity = InnoPushCoinWindowDao.getInstance().
                    loadBySecondType(ProductUtil.loadSecondType(productId));
            long outTime = windowMsgEntity==null?0:windowMsgEntity.getAwardLockOutTime()*1000;//超时时间
            if((TimeUtil.getNowTime()-msg.getLockTime())>=outTime){
                LogUtil.getLogger().error("自研设备{}中奖锁定了，但是超出了设定的超时时间{}秒，自动解锁---------",
                        productId, outTime/1000);
                //更新锁定信息
                updateProductAwardLockMsg(productId, YesOrNoEnum.NO.getCode());
            }else{
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 更新中奖锁信息
     */
    public static void updateProductAwardLockMsg(int productId, int isStart) {
        ProductAwardLockMsg msg = ProductAwardLockDao.getInstance().loadByProductId(productId);
        msg.setIsAwardLock(isStart);//是否锁定
        if(isStart==YesOrNoEnum.YES.getCode()){
            msg.setLockTime(TimeUtil.getNowTime());//锁定时间
        }else{
            msg.setLockTime(0);//锁定时间
        }
        ProductAwardLockDao.getInstance().setCache(productId, msg);
    }

    /**
     * 获取玩家开始游戏倍率
     */
    public static int loadUserStartGameMulti(int userId){
        return UserStartGameMultiMsgDao.getInstance().loadByUserId(userId).getCoinMulti();
    }

    /**
     * 填充玩家开始游戏倍率信息
     */
    public static UserStartGameMultiMsg initUserStartGameMultiMsg(int userId) {
        UserStartGameMultiMsg msg = new UserStartGameMultiMsg();
        msg.setUserId(userId);//玩家ID
        msg.setCoinMulti(0);//倍率
        return msg;
    }

    /**
     * 更新中奖锁倍率
     */
    public static void updateProductAwardLockMultiMsg(int productId, int coinMulti) {
        if(coinMulti>0) {
            ProductAwardLockMsg msg = ProductAwardLockDao.getInstance().loadByProductId(productId);
            msg.setCoinMulti(coinMulti);//投币倍率
            ProductAwardLockDao.getInstance().setCache(productId, msg);
        }
    }

    /**
     * 设置游戏中玩家信息
     */
    public static void updateGamingUserMsg(int productId, InnoProductMsg innoProductMsg) {
        boolean emptyFlag = false;//无人上机设备
        //查询设备游戏中玩家信息
        ProductGamingUserMsg msg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
        int productMulti = innoProductMsg==null?0:innoProductMsg.getProductMulti();//设备倍率
        if(innoProductMsg==null){
            //重置
            msg.setServerSideType(0);//服务端类型
            msg.setUserName("");//玩家昵称
            msg.setImgUrl("");//玩家头像
            emptyFlag = true;
        }else {
            //更新
            int serverSideType = innoProductMsg.getServerSideType();//服务端类型
            msg.setServerSideType(serverSideType);//服务端类型
            msg.setUserId(innoProductMsg.getUserId());//玩家ID
            msg.setUserName(innoProductMsg.getUserName());//玩家昵称
            msg.setImgUrl(CrossServerMsgUtil.loadCrossUserImg(serverSideType, innoProductMsg.getImgUrl()));//玩家头像
        }
        ProductGamingUserMsgDao.getInstance().setCache(productId, msg);
        //设置服务端玩家信息
        if(msg.getUserId()>0 && !CrossServerMsgUtil.isArcxServer(msg.getServerSideType())){
            ServerSideUserMsgDao.getInstance().setCache(msg.getUserId(), msg.getServerSideType(), msg);
        }
        if(productMulti>0){
            //更改设备倍率
            ProductGamingUtil.updateInnoProductSettlementMsg(productId, productMulti);
        }
        //清空一下设备缓存
        if(emptyFlag) {
            initProductCache(productId);
        }
    }

    /**
     * 清空设备缓存信息
     */
    private static void initProductCache(int productId) {
        //查询设备缓存信息
        ProductRoomMsg msg = ProductRoomDao.getInstance().loadByProductId(productId);
        if(msg!=null && msg.getGamingUserId()>0){
            ProductRoomDao.getInstance().delUser(productId, msg.getGamingUserId());
        }
    }

    /**
     * 设置游戏中玩家信息
     */
    public static void updateGamingUserMsg(int productId, ResponseGeneralMsg responseGeneralMsg) {
        boolean emptyFlag = false;//是否无人上机设备
        //查询设备游戏中玩家信息
        ProductGamingUserMsg msg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
        if(responseGeneralMsg==null){
            //重置
            msg.setServerSideType(0);//服务端类型
            msg.setUserId(0);//玩家ID
            msg.setUserName("");//玩家昵称
            msg.setImgUrl("");//玩家头像
            emptyFlag = true;
        }else {
            //更新
            int serverSideType = responseGeneralMsg.getServerSideType();//服务端类型
            msg.setServerSideType(serverSideType);//服务端类型
            msg.setUserId(responseGeneralMsg.getUserId());//玩家ID
            msg.setUserName(responseGeneralMsg.getUserName());//玩家昵称
            msg.setImgUrl(CrossServerMsgUtil.loadCrossUserImg(
                    responseGeneralMsg.getServerSideType(), responseGeneralMsg.getImgUrl()));//玩家头像
        }
        ProductGamingUserMsgDao.getInstance().setCache(productId, msg);
        //设置服务端玩家信息
        if(msg.getUserId()>0 && !CrossServerMsgUtil.isArcxServer(msg.getServerSideType())){
            ServerSideUserMsgDao.getInstance().setCache(msg.getUserId(), msg.getServerSideType(), msg);
        }
        //清空一下设备缓存
        if(emptyFlag) {
            initProductCache(productId);
        }
    }

    /**
     * 获得游戏币
     */
    public static void addEarnCoin(int userId, int productId, long result) {
        //获取设备锁
        RedisLock pushCoinLock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_COST_COIN_LOCK+"_"+productId,
                2000);
        try {
            if (pushCoinLock.lock()) {
                //添加设备获得币信息
                ProductCostCoinMsg costCoinMsg = ProductCostCoinMsgDao.getInstance().loadByProductId(productId);
                if (costCoinMsg != null) {
                    costCoinMsg.setSumAddCoin(costCoinMsg.getSumAddCoin() + result);
                    ProductCostCoinMsgDao.getInstance().setCache(productId, costCoinMsg);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            pushCoinLock.unlock();
        }
        //添加玩家余额
        UserBalanceUtil.addUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode(), result);
        //添加玩家游戏经验
        SchedulerSample.delayed(10, new AddUserGameExpTask(userId, result));
    }

    /**
     * 填充堆塔信息
     */
    public static PileTowerMsg initPileTowerMsg(int productId) {
        PileTowerMsg msg = new PileTowerMsg();
        msg.setProductId(productId);//设备ID
        msg.setTillTime(0);//堆塔连续次数
        msg.setPileTime(0);//堆塔时间
        return msg;
    }

    /**
     * 填充玩家彩票信息
     */
    public static UserLotteryMsg initUserLotteryMsg(int userId, int secondLevelType) {
        UserLotteryMsg msg = new UserLotteryMsg();
        msg.setUserId(userId);//玩家ID
        msg.setSecondLevelType(secondLevelType);//设备二级分类
        msg.setLotteryNum(0);//彩票数
        return msg;
    }

    /**
     * 添加推下来的彩票
     */
    public static void addLotteryResult(int userId, int result, int productId) {
        int addCoinNum = 0;//添加游戏币数
        int cost = ProductUtil.productCost(productId);//设备花费
        int secondLevelType = ProductUtil.loadSecondType(productId);//设备二级分类
        //获取玩家余额锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_COST_DEAL_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                UserLotteryMsg lotteryMsgEntity = UserLotteryMsgDao.getInstance().loadByMsg(userId, secondLevelType);
                if(lotteryMsgEntity!=null){
                    //处理彩票兑比
                    addCoinNum = dealUserLotteryPercent(result, secondLevelType, lotteryMsgEntity);
                }else{
                    LogUtil.getLogger().info("获取玩家{}彩票分类{}的彩票信息失败--------", userId, secondLevelType);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        int addScore = addCoinNum*cost;//获得彩票数
        if(addCoinNum>0) {
            //添加获得的币
            addEarnCoin(userId, productId, addScore);
        }
        //推送socket通知
        UserNoticePushUtil.pushLotteryNotice(productId, secondLevelType, userId, result, addScore);
    }

    /**
     * 处理玩家彩票兑比
     */
    private static int dealUserLotteryPercent(int lotteryNum, int secondLevelType, UserLotteryMsg lotteryMsg) {
        int addCoinNum = 0;//添加的游戏币
        int num = lotteryMsg.getLotteryNum() + lotteryNum;//彩票数
        //查询彩票兑比
        int percent = LotteryCoinPercentDao.getInstance().loadBySecondLevelType(secondLevelType);
        percent = percent > 0 ? percent : ProductConfigMsg.lotteryCoinExchange;
        if (num >= percent) {
            addCoinNum = num / percent;
        }
        num = num % percent;//彩票进度
        lotteryMsg.setLotteryNum(num);//彩票数
        UserLotteryMsgDao.getInstance().setCache(lotteryMsg.getUserId(), secondLevelType, lotteryMsg);
        return addCoinNum;
    }

    /**
     * 刷新投币缓存时间
     */
    public static void flushPushCoinTime(int productId) {
        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        roomMsg.setPushCoinOnTime(TimeUtil.getNowTime());//上机时间
        ProductRoomDao.getInstance().setCache(roomMsg.getProductId(), roomMsg);
    }

    /**
     * 获取添加道具
     */
    public static DollAwardCommodityMsg loadDollAwardMsg(int productId){
        DollAwardCommodityMsg awardMsg = null;
        //查询设备信息
        ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        int commodityType = productInfoEntity.getCommodityType();//商品类型
        int awardId = productInfoEntity.getAwardId();//奖励ID
        int awardNum = StrUtil.loadInterValNum(productInfoEntity.getAwardMinNum(), productInfoEntity.getAwardMaxNum());//商品数
        if(awardNum>0){
            awardMsg = new DollAwardCommodityMsg();
            awardMsg.setCommodityType(commodityType);//商品类型
            awardMsg.setAwardId(awardId);//奖励ID
            awardMsg.setAwardImgId(productInfoEntity.getImgProductPresentId());//奖励图片
            awardMsg.setAwardNum(awardNum);//奖励数量
        }
        return awardMsg;
    }

    /**
     * 更新玩家开始游戏倍率
     */
    public static void updateUserStartGameMultiMsg(int userId, int coinMulti) {
        if(coinMulti>0) {
            UserStartGameMultiMsg msg = UserStartGameMultiMsgDao.getInstance().loadByUserId(userId);
            msg.setCoinMulti(coinMulti);//投币倍率
            UserStartGameMultiMsgDao.getInstance().setCache(userId, msg);
        }
    }

}
