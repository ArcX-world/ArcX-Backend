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
 * 商品直购
 */
@Service
public class CommodityDirectPurchaseApi extends SystemEventHttpHandler<Session> {
    protected CommodityDirectPurchaseApi() {
        super(RechargeHttpCmdName.COMMODITY_DIRECT_PURCHASE);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> RechargeService.commodityDirectPurchase(map, session));
        cachedPool.shutdown();
    }
}
