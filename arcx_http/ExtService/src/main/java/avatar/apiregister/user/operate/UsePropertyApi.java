package avatar.apiregister.user.operate;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.UserHttpCmdName;
import avatar.net.session.Session;
import avatar.service.user.UserOperateService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用道具
 */
@Service
public class UsePropertyApi extends SystemEventHttpHandler<Session> {
    protected UsePropertyApi() {
        super(UserHttpCmdName.USE_PROPERTY);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> UserOperateService.useProperty(map, session));
        cachedPool.shutdown();
    }
}
