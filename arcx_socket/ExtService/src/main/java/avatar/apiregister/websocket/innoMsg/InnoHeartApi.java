package avatar.apiregister.websocket.innoMsg;

import avatar.facade.SystemEventHandler2;
import avatar.global.linkMsg.websocket.SelfInnoWebsocketInnerCmd;
import avatar.net.session.Session;
import org.springframework.stereotype.Service;

/**
 * 自研设备心跳
 */
@Service
public class InnoHeartApi extends SystemEventHandler2<Session> {
    protected InnoHeartApi() {
        super(SelfInnoWebsocketInnerCmd.S2P_HEART);
    }

    @Override
    public void method(Session session, byte[] bytes) throws Exception {
    }
}
