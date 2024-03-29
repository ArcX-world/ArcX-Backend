package avatar.apiregister.user.opinion;

import avatar.facade.SystemEventHttpHandler;
import avatar.global.linkMsg.UserHttpCmdName;
import avatar.net.session.Session;
import avatar.service.user.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 联系信息
 */
@Service
public class CommunicateMsgApi extends SystemEventHttpHandler<Session> {
    protected CommunicateMsgApi() {
        super(UserHttpCmdName.COMMUNICATE_MSG);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        //逻辑处理
        UserInfoService.communicateMsg(map, session);
    }
}
