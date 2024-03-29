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
 * 代币转移
 */
@Service
public class TransferTokensApi extends SystemEventHttpHandler<Session> {
    protected TransferTokensApi() {
        super(UserHttpCmdName.TRANSFER_TOKENS);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> WalletService.transferTokens(map, session));
        cachedPool.shutdown();
    }
}
