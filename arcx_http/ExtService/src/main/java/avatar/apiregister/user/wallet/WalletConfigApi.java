package avatar.apiregister.user.wallet;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.UserHttpCmdName;
import avatar.net.session.Session;
import avatar.service.user.WalletService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 钱包配置
 */
@Service
public class WalletConfigApi extends SystemEventHttpHandler<Session> {
    protected WalletConfigApi() {
        super(UserHttpCmdName.WALLET_CONFIG);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> WalletService.walletConfig(session));
        cachedPool.shutdown();
    }
}
