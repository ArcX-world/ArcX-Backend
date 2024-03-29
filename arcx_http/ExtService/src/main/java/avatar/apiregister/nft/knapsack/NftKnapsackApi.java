package avatar.apiregister.nft.knapsack;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.NftHttpCmdName;
import avatar.net.session.Session;
import avatar.service.nft.NftKnapsackService;
import avatar.service.nft.SellGoldMachineService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NFT背包
 */
@Service
public class NftKnapsackApi extends SystemEventHttpHandler<Session> {
    protected NftKnapsackApi() {
        super(NftHttpCmdName.NFT_KNAPSACK);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> NftKnapsackService.nftKnapsack(map, session));
        cachedPool.shutdown();
    }
}
