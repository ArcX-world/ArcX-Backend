package avatar.event;

/**
 * 登录大厅推送配置事件
 */
public class CommonNotifyConfigEvent extends InternalEvent{
    public static final String type = "CommonNotifyConfigEvent";
    public CommonNotifyConfigEvent(String accessToken) {
        super(accessToken);
    }
}
