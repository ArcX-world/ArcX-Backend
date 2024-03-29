package avatar.util.product;

import avatar.data.product.gamingMsg.ProductCostCoinMsg;
import avatar.entity.product.innoMsg.InnoHighLevelCoinWeightEntity;
import avatar.entity.product.innoMsg.InnoProductCoinWeightEntity;
import avatar.entity.product.innoMsg.InnoProductSpecifyCoinWeightEntity;
import avatar.entity.product.innoNaPay.InnoNaPayCoinWeightEntity;
import avatar.entity.product.innoNaPay.InnoNaPaySpecifyCoinWeightEntity;
import avatar.entity.user.product.UserWeightNaInnoEntity;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.product.award.ProductAwardTypeEnum;
import avatar.global.enumMsg.product.info.ProductSecondTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductCostCoinMsgDao;
import avatar.module.product.innoMsg.*;
import avatar.module.product.innoNaPay.InnoNaPayCoinWeightDao;
import avatar.module.product.innoNaPay.InnoNaPaySpecifyCoinWeightDao;
import avatar.module.user.product.UserWeightNaInnoDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.TimeUtil;

import java.util.List;
import java.util.Map;

/**
 * 自研设备工具类
 */
public class InnoProductUtil {
    /**
     * 获取玩家游戏币权重等级
     */
    public static int userCoinWeight(int userId, int productId){
        int level = 1;//默认等级1
        long naNum = loadUserInnoProductNaNum(userId, productId);//总NA值
        int secondType = ProductUtil.loadSecondType(productId);//设备二级分类
        if(naNum>0){
            boolean payFlag = InnoNaPayUtil.isPay(userId);//是否付费
            //判定高等级NA
            int highLevel = loadHighNaLevel(naNum, secondType, payFlag);
            if(highLevel>0){
                level = highLevel;
            }else {
                if (InnoNaPayUtil.isPay(userId)) {
                    //付费NA
                    level = loadPayNaLevel(naNum, secondType);
                } else {
                    //普通NA
                    level = loadNormalNaLevel(naNum, secondType);
                }
            }
        }
        return level;
    }

    /**
     * 获取玩家自研设备NA值
     */
    public static long loadUserInnoProductNaNum(int userId, int productId){
        long basicNaNum = stackInnoWeightNa(userId);//玩家自研权重NA值
        //获取玩家在线分数NA
        long onlineNaNum = loadUserOnlineNa(productId);
        return basicNaNum + onlineNaNum;//总NA值
    }

    /**
     * 叠加自研权重NA
     */
    private static long stackInnoWeightNa(int userId) {
        long naNum = 0;//na值
        //查询玩家NA信息
        List<UserWeightNaInnoEntity> list = UserWeightNaInnoDao.getInstance().loadByUserId(userId);
        if(list.size()>0){
            for(UserWeightNaInnoEntity entity : list){
                naNum += entity.getNaNum();
            }
        }
        return naNum;
    }

    /**
     * 获取玩家在线分数权重NA值
     */
    private static long loadUserOnlineNa(int productId) {
        long naNum = 0;
        ProductCostCoinMsg productPushCoinMsg = ProductCostCoinMsgDao.getInstance().loadByProductId(productId);
        if(productPushCoinMsg!=null) {
            long plusScore = productPushCoinMsg.getSumAddCoin()-productPushCoinMsg.getSumCostCoin();//差值
            naNum = Math.max(plusScore, 0);
        }
        return naNum;
    }

    /**
     * 获取高等级NA等级
     */
    private static int loadHighNaLevel(long naNum, int secondType, boolean payFlag) {
        int retLevel = 0;
        //获取高等级列表
        List<InnoHighLevelCoinWeightEntity> list = InnoHighLevelCoinWeightDao.getInstance().
                loadByMsg(secondType, payFlag? YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());
        if(list.size()>0 && list.get(0).getNaNum()<=naNum){
            for(int i=0;i<list.size();i++){
                InnoHighLevelCoinWeightEntity entity = list.get(i);
                if(entity.getNaNum()>naNum){
                    retLevel = entity.getLevel();
                    break;
                }else if(i==(list.size()-1)){
                    retLevel = entity.getLevel()+1;
                }
            }
        }
        return retLevel;
    }

    /**
     * 获取付费NA等级
     */
    private static int loadPayNaLevel(long naNum, int secondType) {
        int level = loadPaySpecifyLevel(naNum, secondType);
        if (level == 0) {
            //查询通用权重等级
            level = loadPayGeneralLevel(naNum);
        }
        if (level == 0) {
            level = 1;
        }
        return level;
    }

    /**
     * 获取付费NA通用权重等级
     */
    private static int loadPayGeneralLevel(long naNum) {
        int level = 0;
        //查询自研设备等级信息
        List<InnoNaPayCoinWeightEntity> list = InnoNaPayCoinWeightDao.getInstance().loadMsg();
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                InnoNaPayCoinWeightEntity entity = list.get(i);
                if(entity.getNaNum()>naNum){
                    level = entity.getLevel();
                    break;
                }else if(i==(list.size()-1)){
                    level = entity.getLevel()+1;
                }
            }
        }
        return level;
    }

    /**
     * 获取付费NA指定设备二级分类等级
     */
    private static int loadPaySpecifyLevel(long naNum, int secondType) {
        int level = 0;
        //查询自研指定设备等级信息
        List<InnoNaPaySpecifyCoinWeightEntity> list = InnoNaPaySpecifyCoinWeightDao.getInstance().
                loadBySecondType(secondType);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                InnoNaPaySpecifyCoinWeightEntity entity = list.get(i);
                if(entity.getNaNum()>naNum){
                    level = entity.getLevel();
                    break;
                }else if(i==(list.size()-1)){
                    level = entity.getLevel()+1;
                }
            }
        }
        return level;
    }

    /**
     * 获取普通NA等级
     */
    private static int loadNormalNaLevel(long naNum, int secondType) {
        //指定分类等级
        int level = loadSpecifyLevel(naNum, secondType);
        if (level == 0) {
            //查询通用权重等级
            level = loadGeneralLevel(naNum);
        }
        if (level == 0) {
            level = 1;
        }
        return level;
    }

    /**
     * 获取通用权重等级
     */
    private static int loadGeneralLevel(long naNum) {
        int level = 0;
        //查询自研设备等级信息
        List<InnoProductCoinWeightEntity> list = InnoProductCoinWeightDao.getInstance().loadMsg();
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                InnoProductCoinWeightEntity entity = list.get(i);
                if(entity.getCoin()>naNum){
                    level = entity.getLevel();
                    break;
                }else if(i==(list.size()-1)){
                    level = entity.getLevel()+1;
                }
            }
        }
        return level;
    }

    /**
     * 获取指定设备二级分类等级
     */
    private static int loadSpecifyLevel(long naNum, int secondType) {
        int level = 0;
        //查询自研指定设备等级信息
        List<InnoProductSpecifyCoinWeightEntity> list = InnoProductSpecifyCoinWeightDao.getInstance().
                loadBySecondType(secondType);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                InnoProductSpecifyCoinWeightEntity entity = list.get(i);
                if(entity.getCoin()>naNum){
                    level = entity.getLevel();
                    break;
                }else if(i==(list.size()-1)){
                    level = entity.getLevel()+1;
                }
            }
        }
        return level;
    }

    /**
     * 是否自研免费投币环节
     */
    public static boolean isInnoFreeCoin(int productId){
        boolean flag = false;
        //获取设备二级分类
        int secondType = ProductUtil.loadSecondType(productId);
        if(secondType== ProductSecondTypeEnum.AGYPT.getCode()){
            //自研埃及
            flag = isInnoSpecialAwardLink(productId, ProductAwardTypeEnum.AGYPT_OPEN_BOX.getCode());
        }else if(secondType==ProductSecondTypeEnum.CLOWN_CIRCUS.getCode()){
            //小丑马戏团口哨
            flag = isInnoSpecialAwardLink(productId, ProductAwardTypeEnum.WHISTLE.getCode());
        }else if(secondType==ProductSecondTypeEnum.PIRATE.getCode()){
            //海盗开炮
            flag = isInnoSpecialAwardLink(productId, ProductAwardTypeEnum.PIRATE_CANNON.getCode());
        }
        return flag;
    }

    /**
     * 是否自研设备特殊环节中
     */
    private static boolean isInnoSpecialAwardLink(int productId, int awardType){
        boolean flag = false;
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.INNO_SPECIAL_AWARD_LOCK + "_" + productId,
                2000);
        try {
            if (lock.lock()) {
                Map<Integer, Long> awardTypeMap = SelfSpecialAwardMsgDao.getInstance().loadByProductId(productId);
                if(awardTypeMap.containsKey(awardType)){
                    long time = awardTypeMap.get(awardType);
                    if((TimeUtil.getNowTime()-time)<innoSpecialLinkTillTime(awardType)*1000){
                        flag = true;
                    }else{
                        //删除缓存
                        awardTypeMap.remove(awardType);
                        SelfSpecialAwardMsgDao.getInstance().setCache(productId, awardTypeMap);
                    }
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
        return flag;
    }

    /**
     * 自研特殊环节持续时间
     */
    private static long innoSpecialLinkTillTime(int awardType){
        long time = 0;
        if(awardType==ProductAwardTypeEnum.AGYPT_OPEN_BOX.getCode()){
            //埃及开箱子
            time = ProductConfigMsg.agyptOpenBoxTillTime;
        }else if(awardType==ProductAwardTypeEnum.WHISTLE.getCode()){
            //小丑口哨
            time = ProductConfigMsg.clownCircusFerruleTillTime;
        }else if(awardType==ProductAwardTypeEnum.PIRATE_CANNON.getCode()){
            //海盗开炮
            time = ProductConfigMsg.pirateCannonTillTime;
        }
        return time;
    }

    /**
     * 是否倍率等级限制
     */
    public static boolean isCoinMultiLowerLimit(int userId, int coinMulti, int productId) {
        int lastMultiLevel = ProductGamingUtil.loadInnoLastMultiLevel(productId);//最近倍率等级
        boolean flag = lastMultiLevel>0 && coinMulti>ProductGamingUtil.loadCoinMulti(productId, lastMultiLevel);
        if(flag) {
            LogUtil.getLogger().info("玩家{}选择的倍率{}超出最近下机倍率等级{}------", userId, coinMulti, lastMultiLevel);
        }
        return flag;
    }

    /**
     * 是否解锁版本
     */
    public static boolean isUnlockVersion(String version){
        boolean flag = false;
        List<String> list = InnoProductUnlockVersionDao.getInstance().loadMsg();
        if(list.size()>0){
            flag = list.contains(version);
        }
        return flag;
    }

}
