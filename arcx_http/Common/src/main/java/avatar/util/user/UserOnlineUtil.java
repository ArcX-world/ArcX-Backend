package avatar.util.user;

import avatar.data.user.attribute.UserOnlineExpMsg;
import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;

import java.util.List;

/**
 * 玩家在线工具类
 */
public class UserOnlineUtil {
    /**
     * 获取游戏中的设备
     */
    public static int loadGamingProduct(int userId) {
        int retPId = 0;
        //查询在线信息
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        if(list.size()>0){
            for(UserOnlineMsgEntity entity : list){
                int productId = entity.getProductId();//设备ID
                if(entity.getIsGaming()== YesOrNoEnum.YES.getCode()){
                    retPId = productId;
                    break;
                }
            }
        }
        return retPId;
    }

    /**
     * 玩家是否在线
     */
    public static boolean isOnline(int userId) {
        //查询玩家在线信息
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        return list.size()!=0 && list.get(0).getIsOnline() == YesOrNoEnum.YES.getCode();
    }

    /**
     * 填充玩家在线经验信息
     */
    public static UserOnlineExpMsg initUserOnlineExpMsg(int userId) {
        UserOnlineExpMsg msg = new UserOnlineExpMsg();
        msg.setUserId(userId);//玩家ID
        msg.setCoinNum(0);//投入游戏币数
        msg.setExpNum(UserAttributeUtil.loadExpNum(userId));//经验数
        return msg;
    }


}
