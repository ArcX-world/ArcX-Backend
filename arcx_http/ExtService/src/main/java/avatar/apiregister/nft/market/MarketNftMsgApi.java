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
 * 市场NFT信息
 */
@Service
public class MarketNftMsgApi extends SystemEventHttpHandler<Session> {
    protected MarketNftMsgApi() {
        super(NftHttpCmdName.MARKET_NFT_MSG);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> NftMarketService.marketNftMsg(map, session));
        cachedPool.shutdown();
    }
}
