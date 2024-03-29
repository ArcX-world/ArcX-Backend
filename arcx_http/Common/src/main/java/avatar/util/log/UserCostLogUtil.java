package avatar.util.log;

import avatar.entity.nft.SellGoldMachineMsgEntity;
import avatar.entity.recharge.property.RechargePropertyMsgEntity;
import avatar.global.basicConfig.basic.UserCostMsg;
import avatar.global.basicConfig.basic.WalletConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.enumMsg.basic.nft.NftAttributeTypeEnum;
import avatar.global.enumMsg.basic.recharge.TokensTypeEnum;
import avatar.global.enumMsg.user.UserAttributeTypeEnum;
import avatar.util.LogUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.recharge.SuperPlayerUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserPropertyUtil;
import avatar.util.user.UserUsdtUtil;

/**
 * 玩家消费工具类
 */
public class UserCostLogUtil {
    /**
     * 添加福利签到日志
     */
    public static void welfareSign(int userId, int commodityType, int awardNum) {
        String costMsg = UserCostMsg.signBonus;
        //添加操作日志
        UserOperateLogUtil.costBalance(awardNum, userId, 0, commodityType, costMsg);
        //签到
        UserOperateLogUtil.sign(userId);
    }

    /**
     * 添加注册送币日志
     */
    public static void registerWelfare(int userId, int awardNum) {
        //添加操作日志
        UserOperateLogUtil.costBalance(awardNum, userId, 0, CommodityUtil.gold(), UserCostMsg.registerPresentGold);
    }

    /**
     * 扣除属性等级消耗
     */
    public static boolean costAttribute(int userId, int attributeType, long costAxc) {
        int commodityType = CommodityUtil.axc();
        boolean flag = UserBalanceUtil.costUserBalance(userId, commodityType, costAxc);
        if(flag && costAxc>0){
            String logMsg = UserCostMsg.upgradeAttribute+"【"+ UserAttributeTypeEnum.getNameByCode(attributeType) +"】";
            //添加日志
            UserOperateLogUtil.costBalance(costAxc*-1, userId, 0, commodityType, logMsg);
        }
        return flag;
    }

    /**
     * 扣除刷新商城道具
     */
    public static boolean refreshMallProperty(int userId, int costAxc) {
        int commodityType = CommodityUtil.axc();
        boolean flag = UserBalanceUtil.costUserBalance(userId, commodityType, costAxc);
        if(flag && costAxc>0){
            //添加操作日志
            UserOperateLogUtil.costBalance(costAxc*-1, userId, 0, commodityType, UserCostMsg.refreshMallProperty);
        }
        return flag;
    }

    /**
     * 扣除超级玩家消耗
     */
    public static boolean costSuperPlayer(int userId) {
        int price = SuperPlayerUtil.loadPrice();//价格
        boolean flag = UserUsdtUtil.costUsdtBalance(userId, price);
        if(flag && price>0){
            //添加日志
            UserOperateLogUtil.costUsdt(price*-1, userId, UserCostMsg.superPlayer);
        }
        return flag;
    }

    /**
     * 添加超级玩家金币
     */
    public static void superPlayerGold(int userId, int awardNum) {
        int commodityType = CommodityUtil.gold();//金币
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, awardNum);
        if(flag){
            //添加操作日志
            UserOperateLogUtil.costBalance(awardNum, userId, 0, commodityType, UserCostMsg.superPlayer);
        }else{
            LogUtil.getLogger().error("玩家{}开通超级玩家添加{}金币失败-------", userId, awardNum);
        }
    }

    /**
     * 添加超级玩家道具
     */
    public static void superPlayerProperty(int userId, int awardId, int awardNum) {
        boolean flag = UserPropertyUtil.addUserProperty(userId, awardId, awardNum);
        if(flag){
            //添加操作日志
            UserOperateLogUtil.costProperty(awardNum, userId, awardId, UserCostMsg.superPlayer);
        }else{
            LogUtil.getLogger().error("玩家{}开通超级玩家添加{}道具{}失败-------", userId, awardNum,
                    PropertyTypeEnum.getNameByCode(awardId));
        }
    }

    /**
     * 扣除充值金币消耗
     */
    public static boolean costOfficialRechargeGold(int userId, int price) {
        boolean flag = UserUsdtUtil.costUsdtBalance(userId, price);
        if(flag && price>0){
            //添加日志
            UserOperateLogUtil.costUsdt(price*-1, userId, UserCostMsg.officialRechargeCoin);
        }
        return flag;
    }

    /**
     * 添加官方充值金币
     */
    public static void officialRechargeGold(int userId, long awardNum) {
        int commodityType = CommodityUtil.gold();//金币
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, awardNum);
        if(flag){
            //添加操作日志
            UserOperateLogUtil.costBalance(awardNum, userId, 0, commodityType, UserCostMsg.officialRechargeCoin);
        }else{
            LogUtil.getLogger().error("玩家{}官方充值的{}金币失败-------", userId, awardNum);
        }
    }

    /**
     * 扣除充值道具消耗
     */
    public static boolean costRechargeProperty(int userId, int price) {
        int commodityType = CommodityUtil.axc();//AXC
        boolean flag = UserBalanceUtil.costUserBalance(userId, commodityType, price);
        if(flag && price>0){
            //添加日志
            UserOperateLogUtil.costBalance(price*-1, userId, 0, commodityType, UserCostMsg.buyProperty);
        }
        return flag;
    }

    /**
     * 充值道具奖励
     */
    public static void rechargePropertyAward(int userId, RechargePropertyMsgEntity entity) {
        int awardId = entity.getPropertyType();//道具类型
        int awardNum = entity.getNum();//奖励数量
        boolean flag = UserPropertyUtil.addUserProperty(userId, awardId, awardNum);
        if(flag){
            //添加操作日志
            UserOperateLogUtil.costProperty(awardNum, userId, awardId, UserCostMsg.buyProperty);
        }else{
            LogUtil.getLogger().error("玩家{}充值的{}道具{}失败-------", userId, awardNum,
                    PropertyTypeEnum.getNameByCode(awardId));
        }
    }

    /**
     * 扣除提现上链
     */
    public static boolean costWalletWithdraw(int userId, int tokenType, int amount) {
        String costMsg = UserCostMsg.withdrawChain;//提现上链
        boolean flag = false;
        if(tokenType==TokensTypeEnum.AXC.getCode()){
            //AXC
            if(UserBalanceUtil.getUserBalance(userId, CommodityUtil.axc())>=(amount+WalletConfigMsg.axcFee)){
                flag = UserBalanceUtil.costUserBalance(userId, CommodityUtil.axc(), amount);
                if(flag){
                    //添加日志
                    UserOperateLogUtil.costBalance(amount*-1, userId, 0, CommodityUtil.axc(), costMsg);
                }
                //扣除手续费
                flag = UserBalanceUtil.costUserBalance(userId, CommodityUtil.axc(), WalletConfigMsg.axcFee);
                if(flag){
                    //添加日志
                    UserOperateLogUtil.costBalance(WalletConfigMsg.axcFee*-1, userId, 0, CommodityUtil.axc(),
                            UserCostMsg.withdrawChainFee);
                }
            }
        }else if(tokenType== TokensTypeEnum.USDT.getCode()){
            //USDT
            if(UserUsdtUtil.usdtBalance(userId)>=(amount+WalletConfigMsg.usdtFee)){
                flag = UserUsdtUtil.costUsdtBalance(userId, amount);
                if(flag){
                    //添加日志
                    UserOperateLogUtil.costUsdt(amount*-1, userId, costMsg);
                }
                //扣除手续费
                flag = UserUsdtUtil.costUsdtBalance(userId,  WalletConfigMsg.usdtFee);
                if(flag){
                    //添加日志
                    UserOperateLogUtil.costUsdt(WalletConfigMsg.usdtFee*-1, userId, UserCostMsg.withdrawChainFee);
                }
            }
        }
        return flag;
    }

    /**
     * 扣除兑换NFT售币机金币USDT
     */
    public static boolean costNftGoldUsdt(int userId, String nftCode, long usdtAmount) {
        boolean flag = UserUsdtUtil.costUsdtBalance(userId, usdtAmount);
        if(flag){
            UserOperateLogUtil.costUsdt(usdtAmount*-1, userId, UserCostMsg.nftMachineBuy.replace("aa", nftCode));
        }else{
            LogUtil.getLogger().error("扣除玩家{}兑换NFT售币机金币的USDT{}失败-------", userId, usdtAmount);
        }
        return flag;
    }

    /**
     * 添加玩家获得的金币
     */
    public static void addNftGold(int userId, String nftCode, long coinNum) {
        int commodityType = CommodityUtil.gold();//金币
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, coinNum);
        if(flag){
            UserOperateLogUtil.costBalance(coinNum, userId, 0, commodityType,
                    UserCostMsg.nftMachineBuy.replace("aa", nftCode));
        }else{
            LogUtil.getLogger().error("添加玩家{}兑换NFT售币机金币的金币{}失败-------", userId, coinNum);
        }
    }

    /**
     * 添加NFT兑币机赚的USDT
     */
    public static void addNftGoldEarn(int userId, String nftCode, double usdtAmount) {
        boolean flag = UserUsdtUtil.addUsdtBalance(userId, usdtAmount);
        if(flag){
            UserOperateLogUtil.costUsdt(usdtAmount, userId, UserCostMsg.nftMachineBuy.replace("aa", nftCode));
        }else{
            LogUtil.getLogger().error("添加玩家{}兑换NFT售币机金币赚的USDT{}失败-------", userId, usdtAmount);
        }
    }

    /**
     * 扣除NFT属性消耗
     */
    public static boolean costNftAttribute(int userId, int attributeType, String nftCode, long costAxc) {
        int commodityType = CommodityUtil.axc();
        boolean flag = UserBalanceUtil.costUserBalance(userId, commodityType, costAxc);
        if(flag && costAxc>0){
            String logMsg = UserCostMsg.upgradeNftAttribute+"【"+
                    nftCode + "-" + NftAttributeTypeEnum.getNameByCode(attributeType) +"】";
            //添加日志
            UserOperateLogUtil.costBalance(costAxc*-1, userId, 0, commodityType, logMsg);
        }
        return flag;
    }

    /**
     * 扣除售币机增加储币值的消耗
     */
    public static boolean costSellGoldMachineAddCoin(int userId, double usdtAmount, String nftCode) {
        boolean flag = UserUsdtUtil.costUsdtBalance(userId, usdtAmount);
        if(flag){
            UserOperateLogUtil.costUsdt(usdtAmount*-1, userId,
                    UserCostMsg.sellCoinMachineAddCoin.replace("aa", nftCode));
        }else{
            LogUtil.getLogger().error("扣除玩家{}售币机增加储币值的消耗的USDT{}失败-------", userId, usdtAmount);
        }
        return flag;
    }

    /**
     * 扣除维修售币机耐久度的消耗
     */
    public static boolean costNftRepairDurability(int userId, String nftCode, long costNum) {
        int commodityType = CommodityUtil.axc();
        boolean flag = UserBalanceUtil.costUserBalance(userId, commodityType, costNum);
        if(flag){
            UserOperateLogUtil.costBalance(costNum*-1, userId, 0, commodityType,
                    UserCostMsg.sellCoinMachineRepairDurability.replace("aa", nftCode));
        }else{
            LogUtil.getLogger().error("扣除玩家{}维修售币机耐久度的消耗的AXC{}失败-------", userId, costNum);
        }
        return flag;
    }

    /**
     * 扣除购买NFT的消耗
     */
    public static boolean costBuyNft(int userId, SellGoldMachineMsgEntity entity) {
        int commodityType = entity.getSaleCommodityType();//商品类型
        long costAmount = entity.getSaleNum();//扣除余额
        boolean flag;
        if(commodityType==CommodityUtil.usdt()){
            flag = UserUsdtUtil.costUsdtBalance(userId, costAmount);
            if(flag){
                UserOperateLogUtil.costUsdt(costAmount*-1, userId,
                        UserCostMsg.buyNft.replace("aa", entity.getNftCode()));
            }else{
                LogUtil.getLogger().error("扣除玩家{}购买NFT的消耗的USDT{}失败-------", userId, costAmount);
            }
        }else{
            flag = UserBalanceUtil.costUserBalance(userId, commodityType, costAmount);
            if(flag){
                UserOperateLogUtil.costBalance(costAmount*-1, userId, 0, commodityType,
                        UserCostMsg.buyNft.replace("aa", entity.getNftCode()));
            }else{
                LogUtil.getLogger().error("扣除玩家{}购买NFT的消耗{}{}失败-------", userId,
                        CommodityTypeEnum.getNameByCode(commodityType), costAmount);
            }
        }
        return flag;
    }

    /**
     * 添加出售NFT营收
     */
    public static void addSaleNftEarn(int userId, int commodityType, double saleNum, String nftCode) {
        boolean flag;
        if(commodityType==CommodityUtil.usdt()){
            flag = UserUsdtUtil.addUsdtBalance(userId, saleNum);
            if(flag){
                UserOperateLogUtil.costUsdt(saleNum, userId,
                        UserCostMsg.saleNft.replace("aa", nftCode));
            }else{
                LogUtil.getLogger().error("添加玩家{}购买NFT的获得的USDT{}失败-------", userId, saleNum);
            }
        }else{
            long resultNum = (long) saleNum;//实际获得
            flag = UserBalanceUtil.addUserBalance(userId, commodityType, resultNum);
            if(flag){
                UserOperateLogUtil.costBalance(resultNum, userId, 0, commodityType,
                        UserCostMsg.saleNft.replace("aa", nftCode));
            }else{
                LogUtil.getLogger().error("扣除玩家{}购买NFT的获得的{}{}失败-------", userId,
                        CommodityTypeEnum.getNameByCode(commodityType), resultNum);
            }
        }
    }

    /**
     * 提现失败返还
     */
    public static void backTransferFail(int userId, int tokenType, int amount) {
        String costMsg = UserCostMsg.withdrawChainFailRet;//提现上链失败返还
        if(tokenType==TokensTypeEnum.AXC.getCode()){
            //axc
            int commodityType = CommodityUtil.axc();//AXC
            int totalAmount = amount+WalletConfigMsg.axcFee;//提现额度+费用
            boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, totalAmount);
            if(flag){
                UserOperateLogUtil.costBalance(totalAmount, userId, 0, commodityType, costMsg);
            }else{
                LogUtil.getLogger().error("添加玩家{}提现上链失败返还的{}{}失败-------", userId,
                        CommodityTypeEnum.getNameByCode(commodityType), totalAmount);
            }
        }else if(tokenType==TokensTypeEnum.USDT.getCode()){
            //USDT
            double totalNum = amount+WalletConfigMsg.usdtFee;
            boolean flag = UserUsdtUtil.addUsdtBalance(userId, totalNum);
            if(flag){
                UserOperateLogUtil.costUsdt(totalNum, userId, costMsg);
            }else{
                LogUtil.getLogger().error("添加玩家{}提现上链失败返还的USDT{}失败-------", userId, totalNum);
            }
        }
    }
}
