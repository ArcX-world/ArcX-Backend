package avatar.util.nft;

import avatar.data.nft.MarketNftMsg;
import avatar.data.nft.NftKnapsackMsg;
import avatar.entity.nft.NftConfigEntity;
import avatar.entity.nft.NftHoldHistoryEntity;
import avatar.entity.nft.SellGoldMachineMsgEntity;
import avatar.global.enumMsg.basic.nft.NftTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.nft.info.NftConfigDao;
import avatar.module.nft.info.SellGoldMachineMsgDao;
import avatar.util.basic.CommodityUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.Map;

/**
 * NFT工具类
 */
public class NftUtil {
    /**
     * 交易费
     */
    public static int saleTax() {
        //查询配置信息
        NftConfigEntity entity = NftConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getSaleTax();
    }

    /**
     * 获取耐久度
     */
    public static long durability() {
        //查询配置信息
        NftConfigEntity entity = NftConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getDurability();
    }

    /**
     * 填充NFT背包信息
     */
    public static NftKnapsackMsg initNftKnapsackMsg(String nftCode) {
        NftKnapsackMsg msg = new NftKnapsackMsg();
        int nftType = loadNftType(nftCode);//NFT类型
        if(nftType==NftTypeEnum.SELL_COIN_MACHINE.getCode()){
            //售币机
            SellGoldMachineUtil.fillKnapsackMsg(nftCode, msg);
        }else{
            msg = null;
        }
        return msg;
    }

    /**
     * 填充NFT信息
     */
    public static int initNftMsg(String nftCode, Map<String, Object>dataMap) {
        int status = ClientCode.SUCCESS.getCode();//成功
        int nftType = loadNftType(nftCode);//NFT类型
        if(nftType==NftTypeEnum.SELL_COIN_MACHINE.getCode()){
            SellGoldMachineUtil.initNftMsg(nftCode, dataMap);
        }else{
            status = ClientCode.NFT_NO_EXIST.getCode();//无对应NFT信息
        }
        return status;
    }

    /**
     * 市场NFT信息
     */
    public static int initMarketNftMsg(String nftCode, Map<String, Object> dataMap) {
        int status = ClientCode.SUCCESS.getCode();//成功
        int nftType = loadNftType(nftCode);//NFT类型
        if(nftType==NftTypeEnum.SELL_COIN_MACHINE.getCode()){
            SellGoldMachineUtil.initMarketNftMsg(nftCode, dataMap);
        }else{
            status = ClientCode.NFT_NO_EXIST.getCode();//无对应NFT信息
        }
        return status;
    }

    /**
     * 填充NFT市场信息
     */
    public static MarketNftMsg initMarketNftMsg(String nftCode) {
        MarketNftMsg msg = new MarketNftMsg();
        //查询售币机信息
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        if(entity!=null){
            SellGoldMachineUtil.fillMarketNftMsg(entity, msg);
        }else{
            msg = null;
        }
        return msg;
    }

    /**
     * NFT类型
     */
    public static int loadNftType(String nftCode) {
        int nftType = 0;
        //查询售币机信息
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        if(entity!=null){
            nftType = NftTypeEnum.SELL_COIN_MACHINE.getCode();
        }
        return nftType;
    }

    /**
     * 填充NFT持有记录信息
     */
    public static NftHoldHistoryEntity initNftHoldHistoryEntity(int nftType, String nftCode, int userId) {
        NftHoldHistoryEntity entity = new NftHoldHistoryEntity();
        entity.setNftType(nftType);//NFT类型
        entity.setNftCode(nftCode);//NFT编号
        entity.setUserId(userId);//持有玩家ID
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }

    /**
     * 售出NFT实际收入
     */
    public static double loadSaleNftEarn(long saleNum) {
        return StrUtil.truncateFourDecimal((100-saleTax())*1.0/100*saleNum);
    }
}
