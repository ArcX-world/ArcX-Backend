package avatar.util.solana;

import avatar.global.enumMsg.system.TokensTypeEnum;
import avatar.global.linkMsg.http.SolanaHttpCmdName;
import avatar.global.lockMsg.LockMsg;
import avatar.module.solana.SolanaSignMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.HttpClientUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * solana请求工具类
 */
public class SolanaRequestUtil {
    /**
     * 是否重复请求订阅
     */
    public static boolean repeatDescribe(String signature){
        boolean flag = true;
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK,
                2000);
        try {
            if (lock.lock()) {
                flag = SolanaSignMsgDao.getInstance().loadDbBySign(signature);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return flag;
    }


    /**
     * 请求交易信息
     */
    public static void loadTransaction(String signature){
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("signature", signature);//签名
        String jsonMsg = JsonUtil.mapToJson(paramsMap);//json参数
        String responseMsg = HttpClientUtil.sendHttpPostJson(SolanaMsgUtil.domainName() +
                SolanaHttpCmdName.LOAD_TRANSACTION, jsonMsg);
        LogUtil.getLogger().info("请求{}的交易信息结果{}-------", signature, responseMsg);
        //余额变动处理
        balanceChange(responseMsg);
    }

    /**
     * 余额变动
     */
    private static void balanceChange(String jsonStr){
        long transferNum = 0;//转账金额
        String accountMsg = "";//转账账号
        String receiveAccountMsg = "";//收款账号
        try {
            if (jsonStr.contains("postBalances") && jsonStr.contains("preBalances") &&
                    jsonStr.contains("accountKeys")) {
                Map<String, Object> jsonMap = JsonUtil.strToMap(jsonStr);
                Map<String, Object> metaMap = (Map<String, Object>) jsonMap.get("meta");
                //获取转账前的余额
                JSONArray postBalancesArr = (JSONArray) metaMap.get("postBalances");
                //获取转账后的余额
                JSONArray preBalancesArr = (JSONArray) metaMap.get("preBalances");
                transferNum = Long.parseLong(postBalancesArr.get(1).toString())-Long.parseLong(preBalancesArr.get(1).toString());
                //转账账号
                Map<String, Object> transactionMap = (Map<String, Object>) jsonMap.get("transaction");
                Map<String, Object> messageMap = (Map<String, Object>) transactionMap.get("message");
                JSONArray accountArr = (JSONArray) messageMap.get("accountKeys");
                accountMsg = accountArr.getString(0);
                receiveAccountMsg = accountArr.getString(1);
            }
        }catch(Exception e){
            ErrorDealUtil.printError(e);
        }
        if(!StrUtil.checkEmpty(accountMsg)){
            double realNum = StrUtil.truncateNineDecimal(transferNum*1.0/1000000000);//实际转账数量
            int accountType = loadAccountType(receiveAccountMsg);//转账账号类型
            if(accountType==TokensTypeEnum.AXC.getCode()){
                //AXC单独处理
                realNum = transferNum;//实际转账数量
            }
            LogUtil.getLogger().info("收到{}类型账号{}转账的{}币--------", TokensTypeEnum.getNameByCode(accountType),
                    accountMsg, realNum);
            //处理对应余额
            SolanaUtil.dealUserBalance(accountType, accountMsg, realNum);
        }
    }

    /**
     * 获取账号类型
     */
    private static int loadAccountType(String receiveAccountMsg) {
        int accountType = 0;//账号类型
        if(receiveAccountMsg.equals(SolanaMsgUtil.solSubAcccount())){
            //solana
            accountType = TokensTypeEnum.SOLANA.getCode();
        }else if(receiveAccountMsg.equals(SolanaMsgUtil.axcSubAcccount())){
            //axc
            accountType = TokensTypeEnum.AXC.getCode();
        } else if(receiveAccountMsg.equals(SolanaMsgUtil.usdtSubAcccount())){
            //usdt
            accountType = TokensTypeEnum.USDT.getCode();
        }
        return accountType;
    }

}
