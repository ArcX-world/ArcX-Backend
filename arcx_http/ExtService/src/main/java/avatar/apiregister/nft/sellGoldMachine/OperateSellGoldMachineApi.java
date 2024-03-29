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
 * 销售中的售币机
 */
@Service
public class OperateSellGoldMachineApi extends SystemEventHttpHandler<Session> {
    protected OperateSellGoldMachineApi() {
        super(NftHttpCmdName.OPERATE_SELL_GOLD_MACHINE);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> SellGoldMachineService.operateSellGoldMachine(map, session));
        cachedPool.shutdown();
    }
}
