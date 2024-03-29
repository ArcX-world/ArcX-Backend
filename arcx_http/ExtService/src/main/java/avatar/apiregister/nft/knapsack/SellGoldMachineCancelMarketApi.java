package avatar.apiregister.nft.knapsack;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.NftHttpCmdName;
import avatar.net.session.Session;
import avatar.service.nft.NftKnapsackService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 售币机取消上架市场
 */
@Service
public class SellGoldMachineCancelMarketApi extends SystemEventHttpHandler<Session> {
    protected SellGoldMachineCancelMarketApi() {
        super(NftHttpCmdName.SELL_GOLD_MACHINE_CANCEL_MARKET);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> NftKnapsackService.sellGoldMachineCalcelMarket(map, session));
        cachedPool.shutdown();
    }
}
