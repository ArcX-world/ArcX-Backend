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
 * 游戏中的设备
 */
@Service
public class GamingProductApi extends SystemEventHttpHandler<Session> {
    protected GamingProductApi() {
        super(ProductHttpCmdName.GAMING_PRODUCT);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> ProductService.gamingProduct(map, session));
        cachedPool.shutdown();
    }
}
