package avatar.apiregister.activity.sign;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.ActivityHttpCmdName;
import avatar.net.session.Session;
import avatar.service.activity.WelfareService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 签到信息
 */
@Service
public class SignMsgApi extends SystemEventHttpHandler<Session> {
    protected SignMsgApi() {
        super(ActivityHttpCmdName.SIGN_MSG);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> WelfareService.signMsg(map, session));
        cachedPool.shutdown();
    }
}
