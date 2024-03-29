package avatar.apiregister.login;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.LoginHttpCmdName;
import avatar.net.session.Session;
import avatar.service.login.LoginDealService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 注销账号
 */
@Service
public class LogoutAccountApi extends SystemEventHttpHandler<Session> {
    protected LogoutAccountApi() {
        super(LoginHttpCmdName.LOGOUT_ACCOUNT);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> LoginDealService.logoutAccount(map, session));
        cachedPool.shutdown();

    }
}
