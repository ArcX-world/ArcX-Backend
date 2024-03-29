package avatar.task.online;

import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.user.online.UserOnlineMsgDao;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.List;

/**
 * 删除玩家在线信息定时器
 */
public class DelOnlineMsgTask extends ScheduledTask {

    public DelOnlineMsgTask() {
        super("删除玩家在线信息定时器");
    }

    @Override
    public void run() {
        //查询在线信息
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadDbAll();
        if(list!=null && list.size()>0){
            list.forEach(entity-> {
                int isGaming = entity.getIsGaming();//是否游戏中
                if(isGaming== YesOrNoEnum.YES.getCode()){
                    //游戏中，删除游戏缓存
                    ProductRoomDao.getInstance().delUser(entity.getProductId(), entity.getUserId());
                }
                //删除在线信息
                UserOnlineMsgDao.getInstance().delete(entity.getUserId(), entity.getProductId());
            });
        }
    }
}
