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
 * 售币机增加储币
 */
@Service
public class SellGoldMachineAddCoinApi extends SystemEventHttpHandler<Session> {
    protected SellGoldMachineAddCoinApi() {
        super(NftHttpCmdName.SELL_GOLD_MACHINE_ADD_GOLD);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> NftKnapsackService.sellGoldMachineAddCoin(map, session));
        cachedPool.shutdown();
    }
}
