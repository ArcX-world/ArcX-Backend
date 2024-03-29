package avatar.util.solana;

import avatar.task.solana.ConnectSolanaWebsocketTask;
import avatar.util.LogUtil;
import avatar.util.trigger.SchedulerSample;

/**
 * solana处理工具类
 */
public class SolanaDealUtil {
    /**
     * socket连接成功处理
     */
    public static void socketOpen(String host, int port) {
        LogUtil.getLogger().info("solana订阅链接成功------");
        //添加订阅处理
        SolanaDealUtil.addLogsSubscribe();
    }

    /**
     * socket关闭处理
     */
    public static void socketClose() {
        LogUtil.getLogger().info("solana订阅连接关闭了------");
        //3秒后重新连
        SchedulerSample.delayed(3000, new ConnectSolanaWebsocketTask());
    }

    /**
     * socket连接报错处理
     */
    public static void socketError(String host, int port) {
        LogUtil.getLogger().info("solana订阅连接服务器连接异常，报错了------");
    }

    /**
     * 信息处理
     */
    public static void dealMsg(String jsonStr) {
        if(jsonStr.contains("logsNotification")) {
//            LogUtil.getLogger().info("接收到solana的logsNotification订阅信息，内容{}-------", jsonStr);
            SolanaSubUtil.logsNotification(jsonStr);
        }
    }

    /**
     * logsSubscribe订阅处理
     */
    public static void addLogsSubscribe() {
        String jsonStr = "{" +
                "  \"jsonrpc\": \"2.0\"," +
                "  \"id\": 1," +
                "  \"method\": \"logsSubscribe\"," +
                "  \"params\": [" +
                "    {\n" +
                "      \"mentions\": [ \"xxAccount\" ]" +
                "    }," +
                "    {" +
                "      \"commitment\": \"finalized\"" +
                "    }" +
                "  ]" +
                "}";
        SolanaClient client = SolanaConnectUtil.webSocket.get("111");
        if(client!=null && client.isOpen()){
            //订阅solana账号
            jsonStr = jsonStr.replaceAll("xxAccount", SolanaMsgUtil.solSubAcccount());
            LogUtil.getLogger().info("发送logsSubscribe订阅solana账号信息成功-------");
            //订阅AXC账号
            jsonStr = jsonStr.replaceAll("xxAccount", SolanaMsgUtil.axcSubAcccount());
            LogUtil.getLogger().info("发送logsSubscribe订阅AXC账号信息成功-------");
            //订阅USDT账号
            jsonStr = jsonStr.replaceAll("xxAccount", SolanaMsgUtil.usdtSubAcccount());
            LogUtil.getLogger().info("发送logsSubscribe订阅USDT账号信息成功-------");
            client.send(jsonStr);

        }
    }

}
