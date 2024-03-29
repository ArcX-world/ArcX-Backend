package avatar.util.checkParams;

import avatar.global.enumMsg.basic.commodity.RechargeCommodityTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.util.recharge.RechargeUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;

import java.util.Map;

/**
 * 充值接口参数检测工具类
 */
public class RechargeCheckParamsUtil {
    /**
     * 商品直购
     */
    public static int commodityDirectPurchase(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int userId = ParamsUtil.userId(map);//玩家ID
                int rechargeType = ParamsUtil.intParmasNotNull(map, "rcgCmdTp");//充值商品类型
                int commodityId = ParamsUtil.intParmas(map, "cmdId");//商品ID
                if(StrUtil.checkEmpty(RechargeCommodityTypeEnum.getNameByCode(rechargeType))){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(RechargeUtil.commodityCheckEmpty(userId, rechargeType, commodityId)){
                    status = ClientCode.INVALID_COMMODITY.getCode();//无效商品
                }
            }catch(Exception e){
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }
}
