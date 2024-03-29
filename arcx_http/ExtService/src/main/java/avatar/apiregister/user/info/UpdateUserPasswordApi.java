package avatar.apiregister.user.info;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.UserHttpCmdName;
import avatar.net.session.Session;
import avatar.service.user.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 更新玩家密码
 */
@Service
public class UpdateUserPasswordApi extends SystemEventHttpHandler<Session> {
    protected UpdateUserPasswordApi() {
        super(UserHttpCmdName.UPDATE_USER_PASSWORD);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> UserInfoService.updateUserPassword(map, session));
        cachedPool.shutdown();
    }
}
