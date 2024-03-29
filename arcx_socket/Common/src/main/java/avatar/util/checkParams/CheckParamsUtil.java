package avatar.util.checkParams;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductStatusEnum;
import avatar.module.product.info.ProductInfoDao;
import avatar.util.LogUtil;
import avatar.util.basic.encode.WebsocketEncodeUtil;
import avatar.util.basic.general.CheckUtil;
import avatar.util.product.InnoProductUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.ForbidUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * 检测参数工具类
 */
public class CheckParamsUtil {
    /**
     * 检测设备操作的参数
     */
    public static int checkProductOperation(String accessToken, JSONObject jsonObject, JSONObject dataJson) {
        int status = WebsocketEncodeUtil.checkEncode(accessToken, true, jsonObject);
        if(ParamsUtil.isSuccess(status)){
            try{
                int userId = UserUtil.loadUserIdByToken(accessToken);//玩家ID
                int productId = jsonObject.getInteger("devId");//设备ID
                int operateState = jsonObject.getInteger("hdlTp");//设备操作类型
                int coinMulti = jsonObject.containsKey("gdMul")?jsonObject.getInteger("gdMul"):0;//投币倍率
                String version = jsonObject.containsKey("vsCd")?jsonObject.getString("vsCd"):"";//版本号
                boolean unlockFlag = InnoProductUtil.isUnlockVersion(version);//是否不锁定限制
                //查询设备信息
                ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
                if(productId<=0 || operateState<0){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(CheckUtil.isSystemMaintain() && operateState== ProductOperationEnum.START_GAME.getCode()){
                    status = ClientCode.SYSTEM_MAINTAIN.getCode();//系统维护中
                }else if(entity==null){
                    status = ClientCode.PRODUCT_NO_EXIST.getCode();//设备不存在
                }else if(entity.getStatus()!= ProductStatusEnum.NORMAL.getCode()){
                    status = ClientCode.PRODUCT_EXCEPTION.getCode();//设备异常
                }else if(operateState==ProductOperationEnum.START_GAME.getCode() &&
                        UserUtil.isAccountForbid(userId)){
                    status = ClientCode.ACCOUNT_DISABLED.getCode();//账号异常
                    LogUtil.getLogger().error("玩家{}开始游戏，账号异常--------", userId);
                }else if(coinMulti>0 && !ProductUtil.isCoinMultiExist(productId, coinMulti)){
                    LogUtil.getLogger().error("玩家{}在设备{}开始游戏，选择的倍率{}错误------", userId, productId, coinMulti);
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(!unlockFlag && InnoProductUtil.isCoinMultiLowerLimit(userId, coinMulti, productId)){
                    status = ClientCode.MULTI_LOCK.getCode();//低倍率限制
                }else{
                    status = ForbidUtil.checkGamingForbidProduct(userId, productId, unlockFlag);
                    if(!ParamsUtil.isSuccess(status)){
                        LogUtil.getLogger().info("玩家{}通过socket操作设备{}，但是该玩家被限制了，操作失败---------", userId, productId);
                    }
                }
            }catch (Exception e){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                ErrorDealUtil.printError(e);
            }
        }
        return status;
    }
}
