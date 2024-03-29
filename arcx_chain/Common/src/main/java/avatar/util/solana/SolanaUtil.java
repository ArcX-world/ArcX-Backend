package avatar.util.solana;

import avatar.entity.solana.SolanaSignMsgEntity;
import avatar.global.enumMsg.system.TokensTypeEnum;
import avatar.module.user.thirdpart.Web3GameShiftAccountDao;
import avatar.util.LogUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.system.TimeUtil;

/**
 * solana工具类
 */
public class SolanaUtil {
    /**
     * 填充solana签名信息
     */
    public static SolanaSignMsgEntity initSolanaSignMsgEntity(String signature) {
        SolanaSignMsgEntity entity = new SolanaSignMsgEntity();
        entity.setSignature(signature);//签名
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }

    /**
     * 处理玩家余额
     */
    public static void dealUserBalance(int accountType, String accountMsg, double realNum) {
        if(realNum>=1) {
            if (accountType == TokensTypeEnum.AXC.getCode() || accountType == TokensTypeEnum.USDT.getCode()) {
                //仅处理AXC和USDT
                int userId = Web3GameShiftAccountDao.getInstance().loadDbUser(accountType, accountMsg);
                UserCostLogUtil.rechargeBalance(userId, accountType, realNum);
            }
        }else{
            LogUtil.getLogger().error("{}类型账号{}转账的{}币低于1，不处理--------", TokensTypeEnum.getNameByCode(accountType),
                    accountMsg, realNum);
        }
    }
}
