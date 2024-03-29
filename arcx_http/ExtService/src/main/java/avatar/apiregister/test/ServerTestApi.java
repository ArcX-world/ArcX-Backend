package avatar.apiregister.test;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.TestHttpCmdName;
import avatar.net.session.Session;
import avatar.service.test.TestService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务测试
 */
@Service
public class ServerTestApi extends SystemEventHttpHandler<Session> {
    protected ServerTestApi() {
        super(TestHttpCmdName.SERVER_TEST);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> TestService.serverTest(map, session));
        cachedPool.shutdown();
    }
}
