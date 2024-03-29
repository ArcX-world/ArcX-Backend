package avatar.util.log;

import avatar.global.basicConfig.UserCostMsg;
import avatar.global.enumMsg.basic.CommodityTypeEnum;
import avatar.global.enumMsg.system.TokensTypeEnum;
import avatar.util.LogUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserUsdtUtil;

/**
 * 玩家消费工具类
 */
public class UserCostLogUtil {
    /**
     * 余额充值
     */
    public static void rechargeBalance(int userId, int tokenType, double costNum) {
        String costMsg = UserCostMsg.chainWalletRecharge;//链上钱包充值
        if(tokenType==TokensTypeEnum.AXC.getCode()){
            //axc
            int commodityType = CommodityTypeEnum.AXC.getCode();//AXC
            boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, (long)costNum);
            if(flag){
                UserOperateLogUtil.costBalance((long)costNum, userId, commodityType, costMsg);
            }else{
                LogUtil.getLogger().error("添加玩家{}链上钱包充值的{}{}失败-------", userId,
                        CommodityTypeEnum.getNameByCode(commodityType), costNum);
            }
        }else if(tokenType==TokensTypeEnum.USDT.getCode()){
            //USDT
            boolean flag = UserUsdtUtil.addUsdtBalance(userId, costNum);
            if(flag){
                UserOperateLogUtil.costUsdt(costNum, userId, costMsg);
            }else{
                LogUtil.getLogger().error("添加玩家{}链上钱包充值的USDT{}失败-------", userId, costNum);
            }
        }
    }
}
