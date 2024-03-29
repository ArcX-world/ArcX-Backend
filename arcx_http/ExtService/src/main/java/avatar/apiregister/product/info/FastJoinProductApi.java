package avatar.apiregister.product.info;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.ProductHttpCmdName;
import avatar.net.session.Session;
import avatar.service.product.ProductService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 快速加入的设备
 */
@Service
public class FastJoinProductApi extends SystemEventHttpHandler<Session> {
    protected FastJoinProductApi() {
        super(ProductHttpCmdName.FAST_JOIN_PRODUCT);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> ProductService.fastJoinProduct(map, session));
        cachedPool.shutdown();
    }
}
