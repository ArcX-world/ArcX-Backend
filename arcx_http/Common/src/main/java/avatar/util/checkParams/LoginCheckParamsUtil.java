package avatar.util.checkParams;

import avatar.global.enumMsg.basic.system.LoginWayTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.util.LogUtil;
import avatar.util.login.LoginUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;

import java.util.Map;

/**
 * 登录检测参数工具类
 */
public class LoginCheckParamsUtil {
    /**
     * 检测凭证更新的参数
     */
    public static int checkRefreshToken(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);//验证调用凭证
        if(ParamsUtil.isSuccess(status)) {
            try {
                String refreshToken = map.get("refTkn").toString();//刷新凭证
                LogUtil.getLogger().info("玩家refreshToken{}-------", refreshToken);
                if(StrUtil.checkEmpty(refreshToken)){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * 检测玩家登录的参数
     */
    public static int userLogin(Map<String, Object> map) {
        int status = ClientCode.SUCCESS.getCode();//成功
        try {
            int loginWayType = ParamsUtil.loginWayType(map);//登录方式
            String mac = ParamsUtil.macId(map);//设备唯一ID
            String code = ParamsUtil.stringParmas(map, "iosTkn");//授权code
            String email = ParamsUtil.stringParmas(map, "email");//邮箱
            String vfyCd = ParamsUtil.stringParmas(map, "vfyCd");//验证码
            String pwd = ParamsUtil.stringParmas(map, "pwd");//密码
            if(StrUtil.checkEmpty(LoginWayTypeEnum.getNameByCode(loginWayType)) || StrUtil.checkEmpty(mac)){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }else if(loginWayType==LoginWayTypeEnum.EMAIL.getCode() && StrUtil.checkEmpty(email)){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }else if(loginWayType==LoginWayTypeEnum.EMAIL.getCode() && StrUtil.checkEmpty(vfyCd) &&
                    StrUtil.checkEmpty(pwd)){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }else if(loginWayType==LoginWayTypeEnum.EMAIL.getCode() && !StrUtil.checkEmpty(vfyCd)){
                //邮箱验证码登录
                status = LoginUtil.verifyEmailCodeOutTime(email, vfyCd);
            }else if(loginWayType==LoginWayTypeEnum.EMAIL.getCode() && !StrUtil.checkEmpty(pwd)){
                //邮箱密码登录
                status = LoginUtil.checkEmailUser(email, pwd);
            }
        }catch(Exception e){
            ErrorDealUtil.printError(e);
            status = ClientCode.PARAMS_ERROR.getCode();//参数错误
        }
        return status;
    }

    /**
     * 邮箱验证码
     */
    public static int emailVerifyCode(Map<String, Object> map) {
        int status = ClientCode.SUCCESS.getCode();//成功
        try {
            String email = ParamsUtil.stringParmasNotNull(map, "email");//邮箱
            if(StrUtil.checkEmpty(email)){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }catch(Exception e){
            ErrorDealUtil.printError(e);
            status = ClientCode.PARAMS_ERROR.getCode();//参数错误
        }
        return status;
    }
}
