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
 * 刷新商城道具
 */
@Service
public class RefreshMallPropertyApi extends SystemEventHttpHandler<Session> {
    protected RefreshMallPropertyApi() {
        super(RechargeHttpCmdName.REFRESH_MALL_PROPERTY);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> RechargeService.refreshMallProperty(map, session));
        cachedPool.shutdown();
    }
}
