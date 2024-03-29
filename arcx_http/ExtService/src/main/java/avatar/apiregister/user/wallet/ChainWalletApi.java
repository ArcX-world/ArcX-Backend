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
 * 链上钱包
 */
@Service
public class ChainWalletApi extends SystemEventHttpHandler<Session> {
    protected ChainWalletApi() {
        super(UserHttpCmdName.CHAIN_WALLET);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> WalletService.chainWallet(map, session));
        cachedPool.shutdown();
    }
}
