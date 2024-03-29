package avatar.util.user;

import avatar.data.user.wallet.RetWalletConfigMsg;
import avatar.entity.user.thirdpart.Web3GameShiftAccountEntity;
import avatar.global.basicConfig.basic.WalletConfigMsg;
import avatar.global.enumMsg.basic.recharge.TokensTypeEnum;
import avatar.module.user.thirdpart.Web3GameShiftAccountDao;
import avatar.util.basic.CommodityUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.system.StrUtil;
import avatar.util.thirdpart.GameShiftUtil;
import avatar.util.thirdpart.SolanaUtil;

/**
 * 钱包工具类
 */
public class WalletUtil {

    /**
     * 处理提现
     */
    public static void walletWithdraw(int tokenType, int amount, int userId) {
        //查询账号信息
        Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(userId);
        //提现失败的返还处理
        boolean flag = false;
        if(tokenType==TokensTypeEnum.AXC.getCode()){
            //axc
            flag = SolanaUtil.transferAxcBalance(userId, amount, accountEntity.getAxcAccount());
        }else if(tokenType==TokensTypeEnum.USDT.getCode()){
            //usdt
            flag = SolanaUtil.transferUsdtBalance(userId, amount, accountEntity.getUsdtAccount());
        }
        if(!flag){
            //提现失败返还
            UserCostLogUtil.backTransferFail(userId, tokenType, amount);
        }
    }

    /**
     * 处理钱包充值
     */
    public static String walletRecharge(int tokenType, int amount, int userId) {
        //查询账号信息
        Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(userId);
        String txHash = "";//凭证
        if(tokenType==TokensTypeEnum.AXC.getCode()){
            //axc
            txHash = SolanaUtil.loadAxcTransferTransaction(userId, amount, 0, accountEntity.getWallet(),
                    accountEntity.getAxcAccount(),"");
        }else if(tokenType==TokensTypeEnum.USDT.getCode()){
            //usdt
            txHash = SolanaUtil.loadUsdtTransferTransaction(userId, amount, 0, accountEntity.getWallet(),
                    accountEntity.getUsdtAccount(),"");
        }
        return StrUtil.checkEmpty(txHash)? "": GameShiftUtil.signTransaction(userId, txHash);
    }

    /**
     * 代币转移
     */
    public static String transferTokens(int tokenType, double amount, int userId, String address) {
        //查询账号信息
        Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(userId);
        String txHash = "";//凭证
        int decimals = StrUtil.loadDecimals(amount);//精度
        int resultAmount = StrUtil.dealNumByDecimals(amount, decimals);//整数
        if(tokenType==TokensTypeEnum.AXC.getCode()){
            //axc
            txHash = SolanaUtil.loadAxcTransferTransaction(userId, resultAmount, decimals, accountEntity.getWallet(),
                    accountEntity.getAxcAccount(),address);
        }else if(tokenType==TokensTypeEnum.USDT.getCode()){
            //usdt
            txHash = SolanaUtil.loadUsdtTransferTransaction(userId, resultAmount, decimals, accountEntity.getWallet(),
                    accountEntity.getUsdtAccount(),address);
        }
        return StrUtil.checkEmpty(txHash)? "": GameShiftUtil.signTransaction(userId, txHash);
    }

    /**
     * 获取代币token
     */
    public static String loadTransferTokens(int tokenType, String address) {
        String accountToken = "";
        try {
            if (tokenType == TokensTypeEnum.AXC.getCode()) {
                //AXC
                accountToken = SolanaUtil.createAccount(address, SolanaUtil.axcMintPubkey()).getAta();
            } else if (tokenType == TokensTypeEnum.USDT.getCode()) {
                //USDT
                accountToken = SolanaUtil.createAccount(address, SolanaUtil.usdtMintPubkey()).getAta();
            }
        }catch(Exception e){
            ErrorDealUtil.printError(e);
        }
        return accountToken;
    }

    /**
     * 钱包最小信息
     */
    public static RetWalletConfigMsg minInfo() {
        RetWalletConfigMsg msg = new RetWalletConfigMsg();
        msg.setSol(WalletConfigMsg.minSolNum);//SOL
        msg.setUsdt(WalletConfigMsg.minUsdtNum);//USDT
        msg.setAxc(WalletConfigMsg.minAxcNum);//AXC
        msg.setArcx(WalletConfigMsg.minArcxNum);//ARCX
        return msg;
    }

    /**
     * 钱包上限信息
     */
    public static RetWalletConfigMsg maxIfo() {
        RetWalletConfigMsg msg = new RetWalletConfigMsg();
        msg.setSol(WalletConfigMsg.maxSolNum);//SOL
        msg.setUsdt(WalletConfigMsg.maxUsdtNum);//USDT
        msg.setAxc(WalletConfigMsg.maxAxcNum);//AXC
        msg.setArcx(WalletConfigMsg.maxArcxNum);//ARCX
        return msg;
    }

    /**
     * 钱包手续费信息
     */
    public static RetWalletConfigMsg feeIfo() {
        RetWalletConfigMsg msg = new RetWalletConfigMsg();
        msg.setSol(WalletConfigMsg.solFee);//SOL
        msg.setUsdt(WalletConfigMsg.usdtFee);//USDT
        msg.setAxc(WalletConfigMsg.axcFee);//AXC
        msg.setArcx(WalletConfigMsg.arcxFee);//ARCX
        return msg;
    }
}
