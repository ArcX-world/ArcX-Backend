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
 * 设备列表
 */
@Service
public class ProductListApi extends SystemEventHttpHandler<Session> {
    protected ProductListApi() {
        super(ProductHttpCmdName.PRODUCT_LIST);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> ProductService.productList(map, session));
        cachedPool.shutdown();
    }
}
