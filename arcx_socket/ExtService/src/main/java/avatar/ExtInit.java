package avatar;

import avatar.event.CommonNotifyConfigEvent;
import avatar.event.InternalEventDispatcher;
import avatar.module.offline.UserOfflineService;
import avatar.service.system.HandleNotifyConfigService;
import avatar.task.innoMsg.ConnectInnoProductTask;
import avatar.task.normalProduct.ConnectInnerNormalProductTask;
import avatar.task.online.DelOnlineMsgTask;
import avatar.task.server.InitExtConfigTask;
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
        // 用户下线事件
        UserOfflineService.getInstance().init();
    }

    private void initListenInternalEvent(){
        InternalEventDispatcher.getInstance().addEventListener(CommonNotifyConfigEvent.type , HandleNotifyConfigService.class);

    }

    private void initScheduler() {
        //启动定时器
        SchedulerSample.init();
        //定时刷新配置信息
        SchedulerSample.register(1* 60* 60 * 1000 , 10 , new InitExtConfigTask());

        //开始连接自研设备服务器
        SchedulerSample.delayed(1000, new ConnectInnoProductTask());
        //开始连接普通设备服务器
        SchedulerSample.delayed(1000, new ConnectInnerNormalProductTask());

        //在线信息处理
        SchedulerSample.delayed(10 , new DelOnlineMsgTask());

    }



}