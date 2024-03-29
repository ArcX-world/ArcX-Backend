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
 * 清除签到信息
 */
@Service
public class ClearSignMsgTestApi extends SystemEventHttpHandler<Session> {
    protected ClearSignMsgTestApi() {
        super(TestHttpCmdName.CLEAR_SIGN_MSG_TEST);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> TestService.clearSignMsg(map, session));
        cachedPool.shutdown();
    }
}
