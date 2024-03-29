package avatar.util.checkParams;

import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.module.user.token.UserAccessTokenDao;
import avatar.module.user.token.UserTokenMsgDao;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserUtil;

import java.util.Map;

/**
 * 检测参数工具类
 */
public class CheckParamsUtil {
    /**
     * 检测玩家通行证
     */
    public static int checkAccessToken(Map<String, Object> map){
        int status = ClientCode.SUCCESS.getCode();//成功
        //获取用户token
        String accessToken = ParamsUtil.accessToken(map);//调用凭证
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
                if(tokenMsgEntity==null || tokenMsgEntity.getAccessOutTime()<= TimeUtil.getNowTime()){
                    status = ClientCode.ACCESS_TOKEN_OUT_TIME.getCode();//调用凭证过期
                }
            }
        }else{
            status = ClientCode.LOGIN_PLEASE.getCode();//请登录
        }
        return status;
    }

    /**
     * 检测分页参数
     */
    public static int checkPage(Map<String, Object> map){
        int status = ClientCode.SUCCESS.getCode();//成功
        try {
            int pageNum = ParamsUtil.pageNum(map);//页码
            int pageSize = ParamsUtil.pageSize(map);//每页的数量
            if(pageNum<=0 || pageSize<=0){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }catch(Exception e){
            ErrorDealUtil.printError(e);
            status = ClientCode.PARAMS_ERROR.getCode();//参数错误
        }
        return status;
    }

    /**
     * 检测分页参数+玩家信息
     */
    public static int checkAccessTokenPage(Map<String, Object> map){
        int status = checkAccessToken(map);//成功
        if(ParamsUtil.isSuccess(status)) {
            try {
                int pageNum = ParamsUtil.pageNum(map);//页码
                int pageSize = ParamsUtil.pageSize(map);//每页的数量
                if (pageNum <= 0 || pageSize <= 0) {
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }
}
