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
 * 购买NFT
 */
@Service
public class BuyNftApi extends SystemEventHttpHandler<Session> {
    protected BuyNftApi() {
        super(NftHttpCmdName.BUY_NFT);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> NftMarketService.buyNft(map, session));
        cachedPool.shutdown();
    }
}
