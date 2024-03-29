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
 * NFT升级
 */
@Service
public class UpgradeNftApi extends SystemEventHttpHandler<Session> {
    protected UpgradeNftApi() {
        super(NftHttpCmdName.NFT_MSG);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> NftKnapsackService.nftMsg(map, session));
        cachedPool.shutdown();
    }
}
