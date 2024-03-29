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
 * 邮箱验证码
 */
@Service
public class EmailVerifyCodeApi extends SystemEventHttpHandler<Session> {
    protected EmailVerifyCodeApi() {
        super(LoginHttpCmdName.EMAIL_VERIFY_CODE);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> LoginDealService.emailVerifyCode(map, session));
        //关闭线程
        cachedPool.shutdown();
    }
}
