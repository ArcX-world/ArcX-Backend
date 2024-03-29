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
 * 钱包开销
 */
@Service
public class WalletSpendingApi extends SystemEventHttpHandler<Session> {
    protected WalletSpendingApi() {
        super(UserHttpCmdName.WALLET_SPENDING);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> WalletService.walletSpending(map, session));
        cachedPool.shutdown();
    }
}
