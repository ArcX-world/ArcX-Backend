package avatar.apiregister.nft.sellGoldMachine;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.NftHttpCmdName;
import avatar.net.session.Session;
import avatar.service.nft.SellGoldMachineService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 兑换NFT的金币
 */
@Service
public class ExchangeNftGoldApi extends SystemEventHttpHandler<Session> {
    protected ExchangeNftGoldApi() {
        super(NftHttpCmdName.EXCHANGE_NFT_GOLD);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> SellGoldMachineService.exchangeNftGold(map, session));
        cachedPool.shutdown();
    }
}
