package avatar.apiregister.product.repair;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.ProductHttpCmdName;
import avatar.net.session.Session;
import avatar.service.product.ProductService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 设备报修
 */
@Service
public class RepairProductApi extends SystemEventHttpHandler<Session> {
    protected RepairProductApi() {
        super(ProductHttpCmdName.REPAIR_PRODUCT);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            //逻辑处理
            ProductService.repairProduct(map, session);
        });
        cachedPool.shutdown();
    }
}
