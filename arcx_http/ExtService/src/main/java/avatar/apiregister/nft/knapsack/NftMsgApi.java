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
 * NFT信息
 */
@Service
public class NftMsgApi extends SystemEventHttpHandler<Session> {
    protected NftMsgApi() {
        super(NftHttpCmdName.UPGRADE_NFT);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> NftKnapsackService.upgradeNft(map, session));
        cachedPool.shutdown();
    }
}
