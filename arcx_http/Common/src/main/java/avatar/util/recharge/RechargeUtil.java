package avatar.util.recharge;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.entity.recharge.gold.RechargeGoldInfoEntity;
import avatar.global.enumMsg.basic.commodity.RechargeCommodityTypeEnum;
import avatar.global.enumMsg.basic.recharge.PayTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.recharge.gold.RechargeGoldInfoDao;
import avatar.util.nft.SellGoldMachineUtil;
import avatar.util.system.ParamsUtil;

import java.util.List;
import java.util.Map;

/**
 * 充值工具类
 */
public class RechargeUtil {

    /**
     * 充值中心
     */
    public static void shoppingMall(int userId, Map<String, Object> dataMap) {
        if(!SuperPlayerUtil.isSuperPlayer(userId)) {
            dataMap.put("spPlyIfo", SuperPlayerUtil.rechargeSuperPlayerMsg());//超级玩家
        }
        dataMap.put("selCnMch", SellGoldMachineUtil.showImg());//售币机
        dataMap.put("cnTbln", RechargeGoldUtil.loadCoinList());//游戏币列表
        dataMap.put("ppyIfo", RechargePropertyUtil.loadPropertyMsg(userId));//道具信息
    }

    /**
     * 充值商品检测
     */
    public static boolean commodityCheckEmpty(int userId, int rechargeType, int commodityId) {
        boolean flag = false;
        if(rechargeType==RechargeCommodityTypeEnum.GOLD.getCode()){
            //金币
            RechargeGoldInfoEntity entity = RechargeGoldInfoDao.getInstance().loadById(commodityId);
            if(entity==null || !ParamsUtil.isConfirm(entity.getActiveFlag())){
                flag = true;
            }
        }else if(rechargeType==RechargeCommodityTypeEnum.PROPERTY.getCode()){
            //道具
            List<Integer> list = RechargePropertyUtil.userActiveList(userId);
            if(list.size()==0 || !list.contains(commodityId)){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 商品直购
     */
    public static int commodityDirectPurchase(Map<String, Object> map, List<GeneralAwardMsg> retList) {
        int status = ClientCode.SUCCESS.getCode();//成功
        int userId = ParamsUtil.userId(map);//玩家ID
        int rechargeType = ParamsUtil.intParmasNotNull(map, "rcgCmdTp");//充值商品类型
        int commodityId = ParamsUtil.intParmas(map, "cmdId");//商品ID
        int productId = ParamsUtil.productId(map);//设备ID
        if(rechargeType==RechargeCommodityTypeEnum.SUPER_PLAYER.getCode()){
            //超级玩家
            status = SuperPlayerUtil.openSuperPlayer(userId, retList);
        }else if(rechargeType==RechargeCommodityTypeEnum.GOLD.getCode()){
            //金币
            status = RechargeGoldUtil.rechargeGold(userId, productId, commodityId, retList);
        }else if(rechargeType==RechargeCommodityTypeEnum.PROPERTY.getCode()){
            //道具
            status = RechargePropertyUtil.rechargeProperty(userId, commodityId, retList);
        }
        return status;
    }

    /**
     * 是否直接支付类型
     */
    public static boolean isDirectPayType(int payType) {
        return payType==PayTypeEnum.USDT.getCode();
    }
}
