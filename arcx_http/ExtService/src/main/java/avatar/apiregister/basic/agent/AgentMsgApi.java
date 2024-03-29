package avatar.apiregister.basic.agent;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.HttpCmdName;
import avatar.net.session.Session;
import avatar.service.basic.AgentService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 代理信息
 */
@Service
public class AgentMsgApi extends SystemEventHttpHandler<Session> {
    protected AgentMsgApi() {
        super(HttpCmdName.AGENT_MSG);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> AgentService.agentMsg(map, session));
        cachedPool.shutdown();
    }
}
