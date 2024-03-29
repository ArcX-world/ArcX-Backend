package avatar.task.user;

import avatar.util.user.UserNoticePushUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 系统通知
 */
public class SystemNoticeNoticeTask extends ScheduledTask {
    private int userId;//玩家ID

    private String content;//通知内容

    public SystemNoticeNoticeTask(int userId, String content) {
        super("系统通知");
        this.userId = userId;
        this.content = content;
    }

    @Override
    public void run() {
        //推送socket
        UserNoticePushUtil.systemNoticePush(userId, content);
    }
}
