package avatar.util.checkParams;

import avatar.global.enumMsg.basic.nft.NftAttributeTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.nft.info.SellGoldMachineMsgDao;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserUsdtUtil;

import java.util.Map;

/**
 * NFT检测参数工具类
 */
public class NftCheckParamsUtil {
    /**
     * 兑换NFT的金币
     */
    public static int exchangeNftGold(Map<String, Object> map){
        int status = CheckParamsUtil.checkAccessToken(map);//成功
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.stringParmasNotNull(map, "nftCd");//NFT编号
                long usdtAmt = ParamsUtil.longParmasNotNull(map, "usdtAmt");//购买的USDT数量
                long usdtExc = ParamsUtil.longParmasNotNull(map, "usdtExc");//USDT兑换值
                if(StrUtil.checkEmpty(nftCd) || usdtAmt<=0 || usdtExc<=0){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(UserUsdtUtil.usdtBalance(ParamsUtil.userId(map))<usdtAmt){
                    status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * NFT信息
     */
    public static int nftMsg(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);//成功
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);//NFT编号
                if(StrUtil.checkEmpty(nftCd)){
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
     * NFT升级
     */
    public static int upgradeNft(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);//成功
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);//NFT编号
                int atbTp = ParamsUtil.intParmasNotNull(map, "atbTp");//属性类型
                if(StrUtil.checkEmpty(nftCd) || StrUtil.checkEmpty(NftAttributeTypeEnum.getNameByCode(atbTp))){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();//无对应NFT信息
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * 售币机增加储币
     */
    public static int sellGoldMachineAddCoin(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);//成功
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);//NFT编号
                long gdAmt = ParamsUtil.longParmasNotNull(map, "gdAmt");//	补货的金币数量
                if(StrUtil.checkEmpty(nftCd) || gdAmt<=0){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();//无对应NFT信息
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * 售币机维修耐久度
     */
    public static int sellGoldMachineRepairDurability(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);//成功
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);//NFT编号
                if(StrUtil.checkEmpty(nftCd)){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();//无对应NFT信息
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * 售币机营业
     */
    public static int sellGoldMachineOperate(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);//成功
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);//NFT编号
                double salePrice = ParamsUtil.doubleParmasNotNull(map, "slPrc");//出售价格
                if(StrUtil.checkEmpty(nftCd) || salePrice<1){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();//无对应NFT信息
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * 售币机停止营业
     */
    public static int sellGoldMachineStopOperate(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);//成功
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);//NFT编号
                if(StrUtil.checkEmpty(nftCd)){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();//无对应NFT信息
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * 售币机上架市场
     */
    public static int sellGoldMachineListMarket(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);//成功
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);//NFT编号
                long salePrice = ParamsUtil.longParmasNotNull(map, "slPrc");
                if(StrUtil.checkEmpty(nftCd) || salePrice<=0){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();//无对应NFT信息
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * 售币机取消上架市场
     */
    public static int sellGoldMachineCancelMarket(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessToken(map);//成功
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);//NFT编号
                if(StrUtil.checkEmpty(nftCd)){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();//无对应NFT信息
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * NFT报告
     */
    public static int nftReport(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessTokenPage(map);//成功
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);//NFT编号
                if(StrUtil.checkEmpty(nftCd)){
                    status = ClientCode.PARAMS_ERROR.getCode();//参数错误
                }else if(SellGoldMachineMsgDao.getInstance().loadMsg(nftCd)==null){
                    status = ClientCode.NFT_NO_EXIST.getCode();//无对应NFT信息
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                status = ClientCode.PARAMS_ERROR.getCode();//参数错误
            }
        }
        return status;
    }

    /**
     * 购买NFT
     */
    public static int buyNft(Map<String, Object> map) {
        int status = CheckParamsUtil.checkAccessTokenPage(map);//成功
        if(ParamsUtil.isSuccess(status)) {
            try {
                String nftCd = ParamsUtil.nftCode(map);//NFT编号
                if(StrUtil.checkEmpty(nftCd)){
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
