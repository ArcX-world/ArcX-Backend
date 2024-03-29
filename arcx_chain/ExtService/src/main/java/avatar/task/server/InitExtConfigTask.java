package avatar.task.server;

import avatar.service.system.ExtConfiService;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 初始化配置定时器
 */
public class InitExtConfigTask extends ScheduledTask {
    public InitExtConfigTask() {
        super("定时初始化大厅配置信息");
    }

    @Override
    public void run() {
        ExtConfiService.initData();
    }
}
