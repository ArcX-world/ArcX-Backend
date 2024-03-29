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
 * 领取签到奖励
 */
@Service
public class ReceiveSignAwardApi extends SystemEventHttpHandler<Session> {
    protected ReceiveSignAwardApi() {
        super(ActivityHttpCmdName.RECEIVE_SIGN_AWARD);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> WelfareService.receiveSignAward(map, session));
        cachedPool.shutdown();
    }
}
