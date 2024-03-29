package avatar.apiregister.websocket.normalMsg;

import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.net.session.Session;
import org.springframework.stereotype.Service;

/**
 * 设备到服务端心跳
 */
@Service
public class InnerProductHeartApi extends SystemEventHandler2<Session> {
    protected InnerProductHeartApi() {
        super(WebsocketInnerCmd.S2C_HEART);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {

    }
}
