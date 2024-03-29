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
 * 设备信息
 */
@Service
public class ProductMsgApi extends SystemEventHttpHandler<Session> {
    protected ProductMsgApi() {
        super(ProductHttpCmdName.PRODUCT_MSG);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> ProductService.productMsg(map, session));
        cachedPool.shutdown();
    }
}
