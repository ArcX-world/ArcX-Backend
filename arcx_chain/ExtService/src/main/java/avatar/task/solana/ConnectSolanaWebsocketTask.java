package avatar.task.solana;

import avatar.util.solana.SolanaConnectUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 开始监听solana订阅信息
 */
public class ConnectSolanaWebsocketTask extends ScheduledTask {
    public ConnectSolanaWebsocketTask() {
        super("开始监听solana订阅信息");
    }

    @Override
    public void run() {
        SolanaConnectUtil.connectSolanaServer();
    }
}
