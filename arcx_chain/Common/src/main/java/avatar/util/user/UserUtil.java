package avatar.util.user;

import avatar.entity.user.info.UserInfoEntity;
import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.basicConfig.ConfigMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.module.user.info.UserInfoDao;
import avatar.module.user.token.UserAccessTokenDao;
import avatar.module.user.token.UserTokenMsgDao;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

/**
 * 玩家工具类
 */
public class UserUtil {
    /**
     * 获取通行证
     */
    public static String loadAccessToken(int userId){
        //查询信息
        UserTokenMsgEntity entity = UserTokenMsgDao.getInstance().loadByUserId(userId);
        return entity==null?"":entity.getAccessToken();
    }

    /**
     * 根据accessToken获取玩家ID
     */
    public static int loadUserIdByToken(String accessToken) {
        int userId = 0;
        if(!StrUtil.checkEmpty(accessToken)){
            //查询信息
            userId = UserAccessTokenDao.getInstance().loadByToken(accessToken);
        }
        return userId;
    }

    /**
     * 是否玩家存在
     */
    public static boolean existUser(int userId) {
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        return entity!=null && entity.getStatus()== UserStatusEnum.NORMAL.getCode();
    }

    /**
     * 检测玩家通行证
     */
    public static int checkAccessToken(String accessToken) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(!StrUtil.checkEmpty(accessToken)){
            //查询对应玩家ID
            int userId = UserAccessTokenDao.getInstance().loadByToken(accessToken);
            if(userId==0){
                status = ClientCode.ACCESS_TOKEN_ERROR.getCode();//调用凭证错误
            }else if(userId>0 && UserUtil.loadUserStatus(userId)!= UserStatusEnum.NORMAL.getCode()){
                status = ClientCode.ACCOUNT_DISABLED.getCode();//账号异常
            }else{
                //查询调用凭证过期时间
                UserTokenMsgEntity tokenMsgEntity = UserTokenMsgDao.getInstance().loadByUserId(userId);
                if(tokenMsgEntity==null || tokenMsgEntity.getAccessOutTime()<=TimeUtil.getNowTime()){
                    status = ClientCode.ACCESS_TOKEN_OUT_TIME.getCode();//调用凭证过期
                }
            }
        }
        return status;
    }

    /**
     * 获取玩家状态
     */
    private static int loadUserStatus(int userId) {
        //查询玩家信息
        UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
        return userInfoEntity==null?-1:userInfoEntity.getStatus();
    }

    /**
     * 是否游客token
     */
    public static boolean isTourist(String accessToken){
        return accessToken.equals(ConfigMsg.touristAccessToken);
    }

    /**
     * 获取玩家IP
     */
    public static String loadUserIp(int userId){
        //查询玩家信息
        UserInfoEntity entity = UserInfoDao.getInstance().loadByUserId(userId);
        return entity==null?"":entity.getIp();
    }

}
