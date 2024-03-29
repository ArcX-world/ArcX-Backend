package avatar.apiregister.user.operate;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.UserHttpCmdName;
import avatar.net.session.Session;
import avatar.service.user.UserInfoService;
import avatar.service.user.UserOperateService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 属性升级
 */
@Service
public class UpgradeAttributeApi extends SystemEventHttpHandler<Session> {
    protected UpgradeAttributeApi() {
        super(UserHttpCmdName.UPGRADE_ATTRIBUTE);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> UserOperateService.upgradeAttribute(map, session));
        cachedPool.shutdown();
    }
}
