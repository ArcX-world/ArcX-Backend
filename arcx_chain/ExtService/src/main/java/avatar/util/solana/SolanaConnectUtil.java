package avatar.util.solana;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * solana链接工具类
 */
public class SolanaConnectUtil {
    //本地缓存的socketmap
    public static ConcurrentMap<String, SolanaClient> webSocket = new ConcurrentHashMap<>();

    /**
     * 初始化solana链接
     */
    public static void connectSolanaServer() {
        try {
            //重新生成连接
            SolanaClient myClient = new SolanaClient("ws://3.90.29.127:8900");
            //开启连接
            myClient.connect();
            //填充处理
            webSocket.put("111", myClient);
        }catch (Exception e){

        }
    }
}
