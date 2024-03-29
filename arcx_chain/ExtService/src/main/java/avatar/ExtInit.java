package avatar;

import avatar.event.CommonNotifyConfigEvent;
import avatar.event.InternalEventDispatcher;
import avatar.service.system.HandleNotifyConfigService;
import avatar.task.server.InitExtConfigTask;
import avatar.task.solana.ConnectSolanaWebsocketTask;
import avatar.util.solana.SolanaRequestUtil;
import avatar.util.trigger.SchedulerSample;

/**
 * 系统定时器初始化地方
 */
public class ExtInit extends ServerInit {
    @Override
    public void init() {
        addCommonEventListener();
        initListenInternalEvent();
        initScheduler();

    }

    // 监听通用事件
    private void addCommonEventListener() {

    }

    private void initListenInternalEvent(){
        InternalEventDispatcher.getInstance().addEventListener(CommonNotifyConfigEvent.type , HandleNotifyConfigService.class);

    }

    private void initScheduler() {
        //启动定时器
        SchedulerSample.init();
        //定时刷新配置信息
        SchedulerSample.register(1* 60* 60 * 1000 , 10 , new InitExtConfigTask());

        //开始监听solana websocket处理
        SchedulerSample.delayed(1000, new ConnectSolanaWebsocketTask());

    }



}