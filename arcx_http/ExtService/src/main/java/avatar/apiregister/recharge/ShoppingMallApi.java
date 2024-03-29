package avatar.apiregister.recharge;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.RechargeHttpCmdName;
import avatar.net.session.Session;
import avatar.service.recharge.RechargeService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 商城中心
 */
@Service
public class ShoppingMallApi extends SystemEventHttpHandler<Session> {
    protected ShoppingMallApi() {
        super(RechargeHttpCmdName.SHOPPING_MALL);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> RechargeService.shoppingMall(map, session));
        cachedPool.shutdown();
    }
}
