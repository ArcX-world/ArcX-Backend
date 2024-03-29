package avatar.service.user;

import avatar.entity.user.thirdpart.Web3GameShiftAccountEntity;
import avatar.global.basicConfig.basic.WalletConfigMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.user.thirdpart.Web3GameShiftAccountDao;
import avatar.net.session.Session;
import avatar.util.basic.CommodityUtil;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.UserCheckParamsUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.thirdpart.GameShiftUtil;
import avatar.util.thirdpart.SolanaUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserUsdtUtil;
import avatar.util.user.WalletUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 钱包接口实现类
 */
public class WalletService {
    /**
     * 钱包开销
     */
    public static void walletSpending(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            //UDST代币
            dataMap.put("usdt", UserUsdtUtil.usdtBalance(userId));
            //AXC代币
            dataMap.put("axc", UserBalanceUtil.getUserBalance(userId, CommodityUtil.axc()));
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 链上钱包
     */
    public static void chainWallet(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = UserCheckParamsUtil.chainWallet(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            //查询账号信息
            Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(userId);
            dataMap.put("wlAds", accountEntity.getWallet());//钱包地址
            //UDST代币
            dataMap.put("usdt", SolanaUtil.accountBalance(accountEntity.getUsdtAccount()));
            //AXC代币
            dataMap.put("axc", SolanaUtil.accountBalance(accountEntity.getAxcAccount()));
            //solana代币
            dataMap.put("sol", GameShiftUtil.getBalance(userId));
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 钱包提现
     */
    public static void walletWithdraw(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = UserCheckParamsUtil.walletWithdraw(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            int tokenType = ParamsUtil.intParmasNotNull(map, "tkTp");//代币类型
            int amount = ParamsUtil.intParmasNotNull(map, "amt");//转移代币数
            //扣掉对应的币
            boolean flag = UserCostLogUtil.costWalletWithdraw(userId, tokenType, amount);
            if(flag) {
                //处理提现
                WalletUtil.walletWithdraw(tokenType, amount, userId);
            }else{
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
            }
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 钱包充值
     */
    public static void walletRecharge(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = UserCheckParamsUtil.walletRecharge(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            int tokenType = ParamsUtil.intParmasNotNull(map, "tkTp");//代币类型
            int amount = ParamsUtil.intParmasNotNull(map, "amt");//转入代币数
            dataMap.put("url", WalletUtil.walletRecharge(tokenType, amount, userId));//确认地址
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 代币转移
     */
    public static void transferTokens(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = UserCheckParamsUtil.transferTokens(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            int tokenType = ParamsUtil.intParmasNotNull(map, "tkTp");//代币类型
            double amount = StrUtil.truncateNmDecimal(ParamsUtil.doubleParmasNotNull(map, "amt"), 9);//转移代币数
            String address = ParamsUtil.stringParmasNotNull(map, "ads");//转移的代币地址
            String tokenAccount = WalletUtil.loadTransferTokens(tokenType, address);//获取代币token
            if(!StrUtil.checkEmpty(tokenAccount)){
                dataMap.put("url", WalletUtil.transferTokens(tokenType, amount, userId, tokenAccount));//确认地址
            }else{
                status = ClientCode.FAIL.getCode();//失败
            }
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 钱包配置
     */
    public static void walletConfig(Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        dataMap.put("minIfo", WalletUtil.minInfo());//最小信息
        dataMap.put("maxIfo", WalletUtil.maxIfo());//上限信息
        dataMap.put("feeIfo", WalletUtil.feeIfo());//手续费信息
        dataMap.put("exFee", WalletConfigMsg.extraFee);//外部费用
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), dataMap);
    }
}
