package avatar.util.thirdpart;

import avatar.data.thirdpart.WalletAccountMsg;
import avatar.data.thirdpart.Web3WalletMsg;
import avatar.entity.user.thirdpart.Web3AxcAccountEntity;
import avatar.global.basicConfig.basic.LocalSolanaConfigMsg;
import avatar.global.basicConfig.basic.OnlineSolanaConfigMsg;
import avatar.global.linkMsg.SolanaHttpCmdName;
import avatar.module.user.thirdpart.Web3AxcAccountDao;
import avatar.util.LogUtil;
import avatar.util.basic.CheckUtil;
import avatar.util.system.HttpClientUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * solana工具类
 */
public class SolanaUtil {
    /**
     * 获取axc mintPubkey
     */
    public static String axcMintPubkey(){
        if(CheckUtil.isTestEnv()){
            //测试缓存
            return LocalSolanaConfigMsg.axcMintPubkey;
        }else{
            //线上
            return OnlineSolanaConfigMsg.axcMintPubkey;
        }
    }

    /**
     * 获取usdt mintPubkey
     */
    public static String usdtMintPubkey(){
        if(CheckUtil.isTestEnv()){
            //测试缓存
            return LocalSolanaConfigMsg.usdtMintPubkey;
        }else{
            //线上
            return OnlineSolanaConfigMsg.usdtMintPubkey;
        }
    }

    /**
     * 获取转账token
     */
    private static String transferToken(){
        if(CheckUtil.isTestEnv()){
            //测试缓存
            return LocalSolanaConfigMsg.transferToken;
        }else{
            //线上
            return OnlineSolanaConfigMsg.transferToken;
        }
    }

    /**
     * 获取feePayer
     */
    private static String feePayer(){
        if(CheckUtil.isTestEnv()){
            //测试缓存
            return LocalSolanaConfigMsg.feePayer;
        }else{
            //线上
            return OnlineSolanaConfigMsg.feePayer;
        }
    }

    /**
     * 获取域名信息
     */
    private static String domainName(){
        if(CheckUtil.isTestEnv()){
            //测试缓存
            return LocalSolanaConfigMsg.domain;
        }else{
            //线上
            return OnlineSolanaConfigMsg.domain;
        }
    }

    /**
     * axc收款账号
     */
    private static String axcCompanyAccount(){
        if(CheckUtil.isTestEnv()){
            //测试缓存
            return LocalSolanaConfigMsg.axcCompantAccount;
        }else{
            //线上
            return OnlineSolanaConfigMsg.axcCompantAccount;
        }
    }

    /**
     * usdt收款账号
     */
    private static String usdtCompanyAccount(){
        if(CheckUtil.isTestEnv()){
            //测试缓存
            return LocalSolanaConfigMsg.usdtCompantAccount;
        }else{
            //线上
            return OnlineSolanaConfigMsg.usdtCompantAccount;
        }
    }

    /**
     * axc官方私钥
     */
    private static String companySk(){
        if(CheckUtil.isTestEnv()){
            //测试缓存
            return LocalSolanaConfigMsg.compantSk;
        }else{
            //线上
            return OnlineSolanaConfigMsg.compantSk;
        }
    }

    /**
     * 创建代币账号
     * 包含walletAccount则表示基于该钱包创建某代币
     * 格式{"data":{"account":{"pk":"A5kmeLHtKvmhQPGxxtBTaE5NpTHLTJhTPSHNyE2sFemZ","sk":"3YgoFme8bsFaJMgYGoEjrsn6LfLY9PdvmQ8vjXPiKNKMewrfmHvEEn8S6VosLbAPSnzFyYKD2LYYw5pCJXQb9F8o"},"ATA":"6RGdpqiavjy1eqN5mo5GWVFcgkDYu7SMWuevF9xxEzWL","err":{}}}
     */
    public static Web3WalletMsg createAccount(String walletAccount, String minPubkey){
        Web3WalletMsg walletMsg = new Web3WalletMsg();
        Map<String, String> paramsMap = new HashMap<>();
        if(!StrUtil.checkEmpty(walletAccount)){
            paramsMap.put("gameShiftWallet", walletAccount);
        }
        paramsMap.put("mintPubkey", minPubkey);//代币地址
        paramsMap.put("feePayer", feePayer());//主账号地址（扣费地址）
        String jsonMsg = JsonUtil.mapToJson(paramsMap);//json参数
        String responseMsg = HttpClientUtil.sendHttpPostJson(domainName()+
                SolanaHttpCmdName.CREATE_ARCX_ACCOUNT, jsonMsg);
        LogUtil.getLogger().info("创建代币账号结果{}---------", responseMsg);
        if(!StrUtil.checkEmpty(responseMsg) && responseMsg.contains("ATA")){
            if(responseMsg.contains("account")){
                //有公钥私钥的
                dealCreateAxcAccount(responseMsg, walletMsg);
                LogUtil.getLogger().info("创建代币账号获得的账号{}，公钥{}，私钥{}---------",
                        walletMsg.getAta(), walletMsg.getAccount().getPk(), walletMsg.getAccount().getSk());
            }else{
                //基于钱包创建
                walletMsg.setAta(dealCreateGameShiftAxcAccount(responseMsg));
                LogUtil.getLogger().info("基于钱包{}创建的代币账号{}---------",
                        walletAccount, walletMsg.getAta());
            }
        }else{
            LogUtil.getLogger().info("创建代币账号，包含参数异常--------");
        }
        return walletMsg;
    }

    /**
     * 处理创建的AXC账号
     */
    private static void dealCreateAxcAccount(String responseMsg, Web3WalletMsg walletMsg) {
        if(responseMsg.contains("data")){
            Map<String, Object> resonseMap = JsonUtil.strToMap(responseMsg);
            if(resonseMap!=null && resonseMap.size()>0 && resonseMap.containsKey("data")){
                Map<String, Object> dataMap = JsonUtil.strToMap(resonseMap.get("data").toString());
                if(dataMap!=null && dataMap.size()>0) {
                    //axc账号
                    if (dataMap.containsKey("ATA")) {
                        walletMsg.setAta(dataMap.get("ATA").toString());
                    } else {
                        LogUtil.getLogger().error("解析solana创建AXC账号的ATA信息错误--------");
                    }
                    //钱包账号信息
                    if (dataMap.containsKey("account")) {
                        WalletAccountMsg accountMsg = new WalletAccountMsg();//账号信息
                        Map<String, Object> accountMap = JsonUtil.strToMap(dataMap.get("account").toString());
                        //公钥
                        if(accountMap.containsKey("pk")){
                            accountMsg.setPk(accountMap.get("pk").toString());//公钥
                        }else {
                            LogUtil.getLogger().error("解析solana创建代币账号的PK钱包公钥信息错误--------");
                        }
                        //私钥
                        if(accountMap.containsKey("sk")){
                            accountMsg.setSk(accountMap.get("sk").toString());//私钥
                        }else {
                            LogUtil.getLogger().error("解析solana创建代币账号的SK钱包私钥信息错误--------");
                        }
                        walletMsg.setAccount(accountMsg);
                    } else {
                        LogUtil.getLogger().error("解析solana创建代币账号的ATA信息错误--------");
                    }
                }else{
                    LogUtil.getLogger().error("解析solana创建代币账号的data信息错误--------");
                }
            }else{
                LogUtil.getLogger().error("解析solana创建代币账号的data信息错误--------");
            }
        }else{
            LogUtil.getLogger().error("解析solana创建代币账号的data信息错误--------");
        }
    }

    /**
     * 获取代币账号余额
     * 格式{"context":{"apiVersion":"1.18.4","slot":286520048},"value":{"amount":"0","decimals":9,"uiAmount":0,"uiAmountString":"0"}}
     */
    public static double accountBalance(String tokenAccount){
        double amount = -1;//余额
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("tokenAccount", tokenAccount);//用户代币地址
        String jsonMsg = JsonUtil.mapToJson(paramsMap);//json参数
        String responseMsg = HttpClientUtil.sendHttpPostJson(domainName()+
                SolanaHttpCmdName.ARCX_ACCOUNT_BALANCE, jsonMsg);
//        LogUtil.getLogger().info("获取代币账号余额结果{}---------", responseMsg);
        if(!StrUtil.checkEmpty(responseMsg) && responseMsg.contains("amount")){
            amount = dealAxcAccountAmount(responseMsg);
        }
//        LogUtil.getLogger().info("代币账号{}的余额{}---------", tokenAccount, amount);
        return amount==-1?0:amount;
    }

    /**
     * 处理账号AXC余额
     */
    private static double dealAxcAccountAmount(String responseMsg) {
        double retAmount = -1;
        if(responseMsg.contains("value")){
            Map<String, Object> dataMap = JsonUtil.strToMap(responseMsg);
            if(dataMap!=null && dataMap.size()>0 && dataMap.containsKey("value")){
                Map<String, Object> valueMap = JsonUtil.strToMap(dataMap.get("value").toString());
                if(valueMap!=null && valueMap.size()>0 && valueMap.containsKey("uiAmountString")){
                    retAmount = Double.parseDouble(valueMap.get("uiAmountString").toString());
                }else{
                    LogUtil.getLogger().error("解析solana的AXC余额的uiAmountString信息错误--------");
                }
//                int decimals = 0;//精度
//                if(valueMap!=null && valueMap.size()>0 && valueMap.containsKey("decimals")){
//                    decimals = Integer.parseInt(valueMap.get("decimals").toString());
//                }else{
//                    LogUtil.getLogger().error("解析solana的AXC余额的decimals信息错误--------");
//                }
//                String uiAmountString = "";//小数
//                if(valueMap!=null && valueMap.size()>0 && valueMap.containsKey("uiAmountString")){
//                    uiAmountString = valueMap.get("uiAmountString").toString();
//                }else{
//                    LogUtil.getLogger().error("解析solana的AXC余额的uiAmountString信息错误--------");
//                }
//                if(decimals>0 && !"0".equals(uiAmountString)){
//                    retAmount += (Double.parseDouble(uiAmountString));
//                    retAmount = StrUtil.truncateNmDecimal(retAmount, decimals);
//                }
            }else{
                LogUtil.getLogger().error("解析solana的AXC余额的value信息错误--------");
            }
        }else{
            LogUtil.getLogger().error("解析solana的AXC余额的value信息错误--------");
        }
        return retAmount;
    }

    /**
     * 扣除AXC代币
     */
    public static void costAxcBalance(int userId, int costNum){
        //查询代币信息
        Web3AxcAccountEntity entity = Web3AxcAccountDao.getInstance().loadByMsg(userId);
        if(entity!=null) {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("publicKey", entity.getAccountSk());//来源的钱包地址（私钥）
            paramsMap.put("tokenAccount1Pubket", entity.getAccountPk());//要扣费的代币地址（公钥）
            paramsMap.put("tokenAccount2Pubket", axcCompanyAccount());//收钱的代币地址（公钥）
            paramsMap.put("mintPubkey", transferToken());//代币地址
            paramsMap.put("amount", costNum+"");//转账额度
            paramsMap.put("decimals", "0");//转账精度
            String jsonMsg = JsonUtil.mapToJson(paramsMap);//json参数
            String responseMsg = HttpClientUtil.sendHttpPostJson(domainName() +
                    SolanaHttpCmdName.ARCX_TRANSFER, jsonMsg);
            LogUtil.getLogger().info("扣除玩家{}AXC代币账号结果{}---------", userId, responseMsg);
        }else{
            LogUtil.getLogger().error("扣除玩家{}AXC代币查询不到玩家的AXC代币账号信息------", userId);
        }
    }

    /**
     * 官方转账AXC代币到gameshift
     */
    public static boolean transferAxcBalance(int userId, int costNum, String account){
        boolean flag = false;
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("publicKey", companySk());//来源的钱包地址（私钥）
        paramsMap.put("tokenAccount1Pubket", axcCompanyAccount());//要扣费的代币地址（公钥）
        paramsMap.put("tokenAccount2Pubket", account);//收钱的代币地址（公钥）
        paramsMap.put("mintPubkey", axcMintPubkey());//代币地址
        paramsMap.put("amount", costNum+"");//转账额度
        paramsMap.put("decimals", "0");//转账精度
        String jsonMsg = JsonUtil.mapToJson(paramsMap);//json参数
        String responseMsg = HttpClientUtil.sendHttpPostJson(domainName() +
                SolanaHttpCmdName.ARCX_TRANSFER, jsonMsg);
        LogUtil.getLogger().info("官方转账AXC代币给玩家{}结果{}---------", userId, responseMsg);
        if(!StrUtil.checkEmpty(responseMsg) && responseMsg.contains("message")){
            flag = loadTransferResult(responseMsg);
        }
        return flag;
    }

    /**
     * 获取转账结果
     */
    private static boolean loadTransferResult(String responseMsg) {
        boolean flag = false;
        Map<String, Object> responseMap = JsonUtil.strToMap(responseMsg);
        if(responseMap.containsKey("message")){
            String result = responseMap.get("message").toString();
            flag = result.equalsIgnoreCase("success");
        }
        return flag;
    }

    /**
     * 官方转账USDT代币到gameshift
     */
    public static boolean transferUsdtBalance(int userId, int costNum, String account){
        boolean flag = false;
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("publicKey", companySk());//来源的钱包地址（私钥）
        paramsMap.put("tokenAccount1Pubket", usdtCompanyAccount());//要扣费的代币地址（公钥）
        paramsMap.put("tokenAccount2Pubket", account);//收钱的代币地址（公钥）
        paramsMap.put("mintPubkey", usdtMintPubkey());//代币地址
        paramsMap.put("amount", costNum+"");//转账额度
        paramsMap.put("decimals", "0");//转账精度
        String jsonMsg = JsonUtil.mapToJson(paramsMap);//json参数
        String responseMsg = HttpClientUtil.sendHttpPostJson(domainName() +
                SolanaHttpCmdName.ARCX_TRANSFER, jsonMsg);
        LogUtil.getLogger().info("官方转账USDT代币给玩家{}结果{}---------", userId, responseMsg);
        if(!StrUtil.checkEmpty(responseMsg) && responseMsg.contains("message")){
            flag = loadTransferResult(responseMsg);
        }
        return flag;
    }

    /**
     * 处理创建gameShift-代币账号
     */
    private static String dealCreateGameShiftAxcAccount(String responseMsg) {
        String ata = "";
        if(responseMsg.contains("data")){
            Map<String, Object> resonseMap = JsonUtil.strToMap(responseMsg);
            if(resonseMap!=null && resonseMap.size()>0 && resonseMap.containsKey("data")){
                Map<String, Object> dataMap = JsonUtil.strToMap(resonseMap.get("data").toString());
                if(dataMap!=null && dataMap.size()>0) {
                    //axc账号
                    if (dataMap.containsKey("ATA")) {
                        ata = dataMap.get("ATA").toString();
                    } else {
                        LogUtil.getLogger().error("解析solana创建gameShift代币账号的ATA信息错误--------");
                    }
                }else{
                    LogUtil.getLogger().error("解析solana创建gameShift代币账号的data信息错误--------");
                }
            }else{
                LogUtil.getLogger().error("解析solana创建gameShift代币账号的data信息错误--------");
            }
        }else{
            LogUtil.getLogger().error("解析solana创建gameShift代币账号的data信息错误--------");
        }
        return ata;
    }

    /**
     * 获取axc转账交易凭证
     */
    public static String loadAxcTransferTransaction(int userId, int costNum, int decimal, String wallet,
            String account, String toAccount){
        String txHash = "";
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("publicKey", wallet);//来源的钱包地址（gameshift钱包地址）
        paramsMap.put("tokenAccountXPubkey", account);//转账出去的公钥（from）
        paramsMap.put("tokenAccountYPubkey", StrUtil.checkEmpty(toAccount)?axcCompanyAccount():toAccount);//收款的公钥（to）
        paramsMap.put("mintPubkey", axcMintPubkey());//代币地址
        paramsMap.put("amount", costNum+"");//转账额度
        paramsMap.put("decimals", decimal+"");//转账精度
        String jsonMsg = JsonUtil.mapToJson(paramsMap);//json参数
        String responseMsg = HttpClientUtil.sendHttpPostJson(domainName() +
                SolanaHttpCmdName.ARCX_TRANSFER_TRANSACTION, jsonMsg);
        LogUtil.getLogger().info("玩家{}获取AXC转账交易凭证结果{}---------", userId, responseMsg);
        if(responseMsg!=null && responseMsg.contains("txhash")){
            Map<String, Object> jsonMap = JsonUtil.strToMap(responseMsg);
            if(jsonMap.containsKey("txhash")){
                txHash = jsonMap.get("txhash").toString();
            }
        }
        return txHash;
    }

    /**
     * 获取usdt转账交易凭证
     */
    public static String loadUsdtTransferTransaction(int userId, int costNum, int decimal, String wallet,
            String account, String toAccount){
        String txHash = "";
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("publicKey", wallet);//来源的钱包地址（gameshift钱包地址）
        paramsMap.put("tokenAccountXPubkey", account);//转账出去的公钥（from）
        paramsMap.put("tokenAccountYPubkey", StrUtil.checkEmpty(toAccount)?usdtCompanyAccount():toAccount);//收款的公钥（to）
        paramsMap.put("mintPubkey", usdtMintPubkey());//代币地址
        paramsMap.put("amount", costNum+"");//转账额度
        paramsMap.put("decimals", decimal+"");//转账精度
        String jsonMsg = JsonUtil.mapToJson(paramsMap);//json参数
        String responseMsg = HttpClientUtil.sendHttpPostJson(domainName() +
                SolanaHttpCmdName.ARCX_TRANSFER_TRANSACTION, jsonMsg);
        LogUtil.getLogger().info("玩家{}获取USDT转账交易凭证结果{}---------", userId, responseMsg);
        if(responseMsg!=null && responseMsg.contains("txhash")){
            Map<String, Object> jsonMap = JsonUtil.strToMap(responseMsg);
            if(jsonMap.containsKey("txhash")){
                txHash = jsonMap.get("txhash").toString();
            }
        }
        return txHash;
    }
}
