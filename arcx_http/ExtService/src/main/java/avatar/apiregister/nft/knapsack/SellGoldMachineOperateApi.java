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
 * 售币机营业
 */
@Service
public class SellGoldMachineOperateApi extends SystemEventHttpHandler<Session> {
    protected SellGoldMachineOperateApi() {
        super(NftHttpCmdName.SELL_GOLD_MACHINE_OPERATE);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> NftKnapsackService.sellGoldMachineOperate(map, session));
        cachedPool.shutdown();
    }
}
