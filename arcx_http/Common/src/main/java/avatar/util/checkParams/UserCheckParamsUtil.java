package avatar.util.checkParams;

import avatar.entity.user.thirdpart.Web3GameShiftAccountEntity;
import avatar.global.basicConfig.basic.RechargeConfigMsg;
import avatar.global.basicConfig.basic.UserConfigMsg;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.user.SexEnum;
import avatar.global.enumMsg.user.UserAttributeTypeEnum;
import avatar.module.user.info.EmailUserDao;
import avatar.module.user.thirdpart.Web3GameShiftAccountDao;
import avatar.util.LogUtil;
import avatar.util.login.LoginUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;

import java.util.Map;

/**
 * 玩家接口参数检测工具类
 */
public class UserCheckParamsUtil {
    /**
     * 设备列表
     */
    public static int updateUserInfo(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                String nickName = ParamsUtil.stringParmasNotNull(map, "plyNm");//玩家昵称
                int sex = ParamsUtil.intParmasNotNull(map, "sex");//性别
                if(StrUtil.checkEmpty(nickName) || StrUtil.checkEmpty(SexEnum.getNameByCode(sex))){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(nickName.length()>UserConfigMsg.nickLength){
                    status = ClientCode.LENGTH_LIMIT.getCode();//长度超出限制
                }
            }catch(Exception e){
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * 更新玩家密码
     */
    public static int updateUserPassword(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int userId = ParamsUtil.userId(map);//玩家ID
                String email = ParamsUtil.stringParmasNotNull(map, "email");//邮箱
                String verifyCode = ParamsUtil.stringParmasNotNull(map, "vfyCd");//验证码
                String password = ParamsUtil.stringParmasNotNull(map, "pwd");//密码
                int emailUser = EmailUserDao.getInstance().loadMsg(email);//邮箱玩家
                if(StrUtil.checkEmpty(email) || StrUtil.checkEmpty(verifyCode) ||
                        StrUtil.checkEmpty(password)){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(password.length()>UserConfigMsg.passwordLength){
                    status = ClientCode.LENGTH_LIMIT.getCode();//长度超出限制
                }else if(emailUser>0 && emailUser!=userId){
                    status = ClientCode.MSG_NOT_FIT.getCode();//信息不匹配
                }else{
                    //邮箱验证码登录
                    status = LoginUtil.verifyEmailCodeOutTime(email, verifyCode);
                }
            }catch(Exception e){
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * 玩家帮助与反馈
     */
    public static int userOpinion(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                String content = ParamsUtil.stringParmasNotNull(map, "fbCnt");//内容
                if(StrUtil.checkEmpty(content)){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }

    /**
     * 联系信息
     */
    public static int communicateMsg(Map<String, Object> map) {
        int status = ClientCode.SUCCESS.getCode();//成功
        try {
            String email = ParamsUtil.stringParmasNotNull(map, "email");//邮件
            if(StrUtil.checkEmpty(email)){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        } catch(Exception e){
            status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            ErrorDealUtil.printError(e);
        }
        return status;
    }

    /**
     * 属性升级
     */
    public static int upgradeAttribute(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int atbTp = ParamsUtil.intParmasNotNull(map, "atbTp");//玩家属性类型
                if(StrUtil.checkEmpty(UserAttributeTypeEnum.getNameByCode(atbTp))){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }

    /**
     * 使用道具
     */
    public static int useProperty(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int pptTp = ParamsUtil.intParmasNotNull(map, "pptTp");//道具类型
                if(StrUtil.checkEmpty(PropertyTypeEnum.getNameByCode(pptTp))){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }

    /**
     * 链上钱包
     */
    public static int chainWallet(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                //查询账号信息
                Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(
                        ParamsUtil.userId(map));
                if(accountEntity==null || StrUtil.checkEmpty(accountEntity.getWallet()) ||
                        StrUtil.checkEmpty(accountEntity.getAxcAccount()) ||
                        StrUtil.checkEmpty(accountEntity.getUsdtAccount())){
                    status = ClientCode.WALLET_ERROR.getCode();//钱包信息异常
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }

    /**
     * 钱包提现
     */
    public static int walletWithdraw(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int tokenType = ParamsUtil.intParmasNotNull(map, "tkTp");//代币类型
                int amount = ParamsUtil.intParmasNotNull(map, "amt");//转移代币数
                //查询账号信息
                Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(
                        ParamsUtil.userId(map));
                if(amount<=0){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(accountEntity==null || StrUtil.checkEmpty(accountEntity.getWallet()) ||
                        StrUtil.checkEmpty(accountEntity.getAxcAccount()) ||
                        StrUtil.checkEmpty(accountEntity.getUsdtAccount())){
                    status = ClientCode.WALLET_ERROR.getCode();//钱包信息异常
                }else if(!RechargeConfigMsg.centerTokens.contains(tokenType+"")){
                    status = ClientCode.TOKENS_TYPE_ERROR.getCode();//代币类型错误
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }

    /**
     * 钱包充值
     */
    public static int walletRecharge(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int tokenType = ParamsUtil.intParmasNotNull(map, "tkTp");//代币类型
                int amount = ParamsUtil.intParmasNotNull(map, "amt");//转入代币数
                //查询账号信息
                Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(
                        ParamsUtil.userId(map));
                if(amount<=0){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(accountEntity==null || StrUtil.checkEmpty(accountEntity.getWallet()) ||
                        StrUtil.checkEmpty(accountEntity.getAxcAccount()) ||
                        StrUtil.checkEmpty(accountEntity.getUsdtAccount())){
                    status = ClientCode.WALLET_ERROR.getCode();//钱包信息异常
                }else if(!RechargeConfigMsg.centerTokens.contains(tokenType+"")){
                    status = ClientCode.TOKENS_TYPE_ERROR.getCode();//代币类型错误
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }

    /**
     * 代币转移
     */
    public static int transferTokens(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int tokenType = ParamsUtil.intParmasNotNull(map, "tkTp");//代币类型
                double amount = ParamsUtil.doubleParmasNotNull(map, "amt");//转移代币数
                String address = ParamsUtil.stringParmasNotNull(map, "ads");//地址
                //查询账号信息
                Web3GameShiftAccountEntity accountEntity = Web3GameShiftAccountDao.getInstance().loadByMsg(
                        ParamsUtil.userId(map));
                if(amount<=0 || StrUtil.checkEmpty(address)){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(accountEntity==null || StrUtil.checkEmpty(accountEntity.getWallet()) ||
                        StrUtil.checkEmpty(accountEntity.getAxcAccount()) ||
                        StrUtil.checkEmpty(accountEntity.getUsdtAccount())){
                    status = ClientCode.WALLET_ERROR.getCode();//钱包信息异常
                }else if(!RechargeConfigMsg.centerTokens.contains(tokenType+"")){
                    status = ClientCode.TOKENS_TYPE_ERROR.getCode();//代币类型错误
                }
            } catch(Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }
}
