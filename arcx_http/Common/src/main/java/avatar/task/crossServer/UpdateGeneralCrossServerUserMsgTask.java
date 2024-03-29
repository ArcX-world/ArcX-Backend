package avatar.task.crossServer;

import avatar.data.crossServer.GeneralCrossServerUserMsg;
import avatar.module.crossServer.CrossServerUserMsgDao;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 更新通用跨服玩家信息定时器
 */
public class UpdateGeneralCrossServerUserMsgTask extends ScheduledTask {

    //玩家信息
    private GeneralCrossServerUserMsg userMsg;

    public UpdateGeneralCrossServerUserMsgTask(GeneralCrossServerUserMsg userMsg) {
        super("更新通用跨服玩家信息定时器");
        this.userMsg = userMsg;
    }

    @Override
    public void run() {
        try {
            int serverSideType = userMsg.getServerSideType();//服务端类型
            int userId = userMsg.getUserId();//玩家ID
            //查询信息
            GeneralCrossServerUserMsg msg = CrossServerMsgUtil.loadGeneralCrossServerUserMsg(
                    serverSideType, userId);
            if(msg!=null){
                CrossServerUserMsgDao.getInstance().setCache(serverSideType, userId, msg);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
    }

}
