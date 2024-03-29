package avatar.apiregister.crossServer;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.CrossServerHttpCmdName;
import avatar.net.session.Session;
import avatar.service.crossServer.CrossServerService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 获取玩家信息
 */
@Service
public class CrossServerUserMsgApi extends SystemEventHttpHandler<Session> {
    protected CrossServerUserMsgApi() {
        super(CrossServerHttpCmdName.CROSS_SERVER_USER_MSG);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> CrossServerService.crossServerUserMsg(map, session));
        cachedPool.shutdown();
    }
}
