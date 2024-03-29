package avatar.util.thirdpart;

import avatar.data.thirdpart.Web3WalletMsg;
import avatar.entity.user.thirdpart.Web3GameShiftAccountEntity;
import avatar.global.basicConfig.basic.GameShiftConfigMsg;
import avatar.module.user.thirdpart.Web3GameShiftAccountDao;
import avatar.util.LogUtil;
import avatar.util.basic.CheckUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import com.squareup.okhttp.*;

import java.util.Map;

/**
 * gameShift工具类
 */
public class GameShiftUtil {
    /**
     * 获取请求域名
     */
    private static String loadDomain(){
        if(CheckUtil.isTestEnv()){
            return GameShiftConfigMsg.localHttpName;
        }else{
            return GameShiftConfigMsg.onlineHttpName;
        }
    }

    /**
     * 注册账号
     */
    private static boolean register(int userId, String email){
        boolean flag = false;
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"referenceId\":\""+userId+"\",\"email\":\""+email+"\"}");
            Request request = new Request.Builder()
                    .url(loadDomain()+"v2/users")
                    .post(body)
                    .addHeader("accept", "application/json")
                    .addHeader("x-api-key", GameShiftConfigMsg.xApiKey)
                    .addHeader("content-type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            int code = response.code();
            if(code==200 || code==201){
                flag = true;
            }
            LogUtil.getLogger().error("玩家{}注册gameShift账号，返回{}，结果{}---------", userId, response.body().string(),
                    flag?"成功":"失败");
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return flag;
    }

    /**
     * 获取主钱包地址
     */
    private static String loadWalletAddress(int userId){
        String address = "";
        try {
            OkHttpClient client = new OkHttpClient();
            String domain = loadDomain()+"users/"+userId+"/wallet-address";
            Request request = new Request.Builder()
                    .url(domain)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("x-api-key", GameShiftConfigMsg.xApiKey)
                    .build();

            Response response = client.newCall(request).execute();
            if(response.code()==200) {
                ResponseBody responseBody = response.body();
                address = responseBody.string();
                LogUtil.getLogger().error("玩家{}通过gameshift获取钱包地址，返回码{}，返回内容{}---------", userId,
                        response.code(), address);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return address;
    }

    /**
     * 获取余额
     */
    public static double getBalance(int userId){
        double balance = 0;//余额
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.gameshift.dev/v2/users/"+userId+"/balances")
                    .get()
                    .addHeader("x-api-key", GameShiftConfigMsg.xApiKey)
                    .build();

            Response response = client.newCall(request).execute();
            int code = response.code();
            String jsonStr = response.body().string();//返回结果
//            LogUtil.getLogger().error("玩家{}获取solana余额，结果{}---------", userId, jsonStr);
            if(code==200){
                //处理余额返回
                balance = dealBalanceResponse(jsonStr);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return balance;
    }

    /**
     * 余额返回处理
     */
    private static double dealBalanceResponse(String jsonStr) {
        double balance = 0;
        try {
            if (!StrUtil.checkEmpty(jsonStr) && jsonStr.contains("sol")) {
                Map<String, Object> jsonMap = JsonUtil.strToMap(jsonStr);
                if(jsonMap.containsKey("balances")){
                    Map<String, Object> balanceMap = (Map<String, Object>) jsonMap.get("balances");
                    if(balanceMap.containsKey("sol")){
                        balance = StrUtil.truncateNmDecimal(Double.parseDouble(balanceMap.get("sol").toString()), 9);
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return balance;
    }

    /**
     * 添加gameshift账号
     */
    public static void addGameShiftAccount(int userId, String email) {
        //查询玩家账号信息
        Web3GameShiftAccountEntity entity = Web3GameShiftAccountDao.getInstance().loadByMsg(userId);
        if(entity==null) {
            //注册gameshift账号
            boolean flag = GameShiftUtil.register(userId, email);
            String wallet;//钱包地址
            if(flag){
                //查询钱包地址
                wallet = GameShiftUtil.loadWalletAddress(userId);
            }else{
                LogUtil.getLogger().error("玩家{}通过email{}创建gameshift账号失败------", userId, email);
                wallet = GameShiftUtil.loadWalletAddress(userId);
            }
            if (!StrUtil.checkEmpty(wallet)) {
                //创建axc代币账号
                Web3WalletMsg walletMsg = SolanaUtil.createAccount(wallet, SolanaUtil.axcMintPubkey());
                String axcAccount = walletMsg.getAta();
                //创建USDT代币账号
                walletMsg = SolanaUtil.createAccount(wallet, SolanaUtil.usdtMintPubkey());
                String usdtAccount = walletMsg.getAta();
                Web3GameShiftAccountDao.getInstance().insert(initWeb3GameShiftAccountEntity(
                        userId, wallet, axcAccount, usdtAccount));
            }
        }
    }

    /**
     * 填充web3 gameShift账号实体信息
     */
    private static Web3GameShiftAccountEntity initWeb3GameShiftAccountEntity(int userId, String wallet,
            String axcAccount, String usdtAccount) {
        Web3GameShiftAccountEntity entity = new Web3GameShiftAccountEntity();
        entity.setUserId(userId);//玩家ID
        entity.setWallet(wallet);//钱包
        entity.setAxcAccount(axcAccount);//axc账号
        entity.setUsdtAccount(usdtAccount);//usdt账号
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }

    /**
     * 交易签名url
     */
    public static String signTransaction(int userId, String transaction){
        String url = "";
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,
                    "{\"serializedTransaction\":\""+transaction+"\",\"onBehalfOf\":\""+userId+"\"}");
            Request request = new Request.Builder()
                    .url("https://api.gameshift.dev/transactions/sign")
                    .post(body)
                    .addHeader("x-api-key", GameShiftConfigMsg.xApiKey)
                    .addHeader("content-type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            int code = response.code();
            if(code==200 || code==201){
                //处理返回
                String responseMsg = response.body().string();
                if(!StrUtil.checkEmpty(responseMsg) && responseMsg.contains("url")){
                    Map<String, Object> responseMap = JsonUtil.strToMap(responseMsg);
                    if(responseMap.containsKey("url")){
                        url = responseMap.get("url").toString();
                    }
                }
            }
            LogUtil.getLogger().error("玩家{}获取交易签名url，结果{}---------", userId, url);

        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return url;
    }
}
