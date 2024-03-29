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
 * 售币机维修耐久度
 */
@Service
public class SellGoldMachineRepairDurabilityApi extends SystemEventHttpHandler<Session> {
    protected SellGoldMachineRepairDurabilityApi() {
        super(NftHttpCmdName.SELL_GOLD_MACHINE_REPAIR_DURABILITY);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> NftKnapsackService.sellGoldMachineRepairDurability(map, session));
        cachedPool.shutdown();
    }
}
