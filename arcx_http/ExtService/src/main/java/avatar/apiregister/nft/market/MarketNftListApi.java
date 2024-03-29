package avatar.apiregister.nft.market;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.NftHttpCmdName;
import avatar.net.session.Session;
import avatar.service.nft.NftMarketService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NFT市场列表
 */
@Service
public class MarketNftListApi extends SystemEventHttpHandler<Session> {
    protected MarketNftListApi() {
        super(NftHttpCmdName.MARKET_NFT_LIST);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> NftMarketService.marketNftList(map, session));
        cachedPool.shutdown();
    }
}
