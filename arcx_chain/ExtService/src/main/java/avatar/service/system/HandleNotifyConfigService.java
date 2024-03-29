package avatar.service.system;

import avatar.event.CommonNotifyConfigEvent;
import avatar.event.ListenInternalEvent;
import avatar.net.session.Session;
import avatar.util.GameData;

/**
 * 处理登录后的推送配置事件 -- TODO 暂时写这里，以后应该从配置服务器获取配置信息
 */
public class HandleNotifyConfigService {

    @ListenInternalEvent(CommonNotifyConfigEvent.type)
    public void handle(CommonNotifyConfigEvent event){
        String accessToken = event.getAccessToken();
        Session session = GameData.getSessionManager().getSessionByAccesstoken(accessToken);
        if(session == null){
            return;
        }
    }
}
