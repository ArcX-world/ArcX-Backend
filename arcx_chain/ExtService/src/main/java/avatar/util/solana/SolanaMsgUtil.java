package avatar.util.solana;

import avatar.global.basicConfig.LocalSolanaConfigMsg;
import avatar.global.basicConfig.OnlineSolanaConfigMsg;
import avatar.util.basic.system.CheckUtil;

/**
 * solana信息工具类
 */
public class SolanaMsgUtil {
    /**
     * 获取域名信息
     */
    public static String domainName(){
        if(CheckUtil.isTestEnv()){
            //测试缓存
            return LocalSolanaConfigMsg.domain;
        }else{
            //线上
            return OnlineSolanaConfigMsg.domain;
        }
    }

    /**
     * solana订阅账号
     */
    public static String solSubAcccount(){
        if(CheckUtil.isTestEnv()){
            //测试缓存
            return LocalSolanaConfigMsg.solSubAccount;
        }else{
            //线上
            return OnlineSolanaConfigMsg.solSubAccount;
        }
    }

    /**
     * axc订阅账号
     */
    public static String axcSubAcccount(){
        if(CheckUtil.isTestEnv()){
            //测试缓存
            return LocalSolanaConfigMsg.axcSubAccount;
        }else{
            //线上
            return OnlineSolanaConfigMsg.axcSubAccount;
        }
    }

    /**
     * usdt订阅账号
     */
    public static String usdtSubAcccount(){
        if(CheckUtil.isTestEnv()){
            //测试缓存
            return LocalSolanaConfigMsg.usdtSubAccount;
        }else{
            //线上
            return OnlineSolanaConfigMsg.usdtSubAccount;
        }
    }

}
