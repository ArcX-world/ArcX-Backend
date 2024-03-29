package avatar.util.checkParams;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.enumMsg.product.ProductStatusEnum;
import avatar.global.enumMsg.product.ProductTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.product.info.ProductInfoDao;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;

import java.util.Map;

/**
 * 设备检测参数工具类
 */
public class ProductCheckParamsUtil {
    /**
     * 检测设备ID，包括状态
     */
    public static int checkProductStatus(Map<String, Object> map) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(ParamsUtil.isSuccess(status)){
            try {
                int productId = ParamsUtil.productId(map);//设备ID
                //查询设备信息
                ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
                if(productId==0){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(entity==null){
                    status = ClientCode.PRODUCT_NO_EXIST.getCode();//设备不存在
                }else if(entity.getStatus()!=ProductStatusEnum.NORMAL.getCode()){
                    status = ClientCode.PRODUCT_EXCEPTION.getCode();//设备故障
                }
            }catch(Exception e){
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * 设备列表
     */
    public static int productList(Map<String, Object> map) {
        int status = CheckParamsUtil.checkPage(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                int productType = ParamsUtil.intParmasNotNull(map, "devTp");//设备类型
                if(StrUtil.checkEmpty(ProductTypeEnum.loadNameByCode(productType))){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }
            }catch(Exception e){
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * 快速加入的设备
     */
    public static int fastJoinProduct(Map<String, Object> map) {
        int status = ClientCode.SUCCESS.getCode();//成功
        try {
            int productType = ParamsUtil.intParmasNotNull(map, "devTp");//设备类型
            if(StrUtil.checkEmpty(ProductTypeEnum.loadNameByCode(productType))){
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }catch(Exception e){
            ErrorDealUtil.printError(e);
            status = ClientCode.PARAMS_ERROR.getCode();//参数错误
        }
        return status;
    }

    /**
     * 设备报修
     */
    public static int repairProduct(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);
        if(ParamsUtil.isSuccess(status)){
            try {
                status = checkProductStatus(map);
            }catch(Exception e){
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

}
