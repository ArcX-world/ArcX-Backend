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
 * 钱包充值
 */
@Service
public class WalletRechargeApi extends SystemEventHttpHandler<Session> {
    protected WalletRechargeApi() {
        super(UserHttpCmdName.WALLET_RECHARGE);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> WalletService.walletRecharge(map, session));
        cachedPool.shutdown();
    }
}
