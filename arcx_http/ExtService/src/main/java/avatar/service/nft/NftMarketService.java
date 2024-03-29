package avatar.service.nft;

import avatar.data.nft.MarketNftMsg;
import avatar.global.enumMsg.basic.nft.NftTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.lockMsg.LockMsg;
import avatar.module.nft.info.NftMarketListDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.checkParams.NftCheckParamsUtil;
import avatar.util.nft.NftUtil;
import avatar.util.nft.SellGoldMachineUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ListUtil;
import avatar.util.system.ParamsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NFT市场接口实现类
 */
public class NftMarketService {
    /**
     * NFT市场列表
     */
    public static void marketNftList(Map<String, Object> map, Session session) {
        List<MarketNftMsg> retList = new ArrayList<>();
        //检测参数
        int status = CheckParamsUtil.checkPage(map);
        if(ParamsUtil.isSuccess(status)) {
            //查询信息
            List<String> list = ListUtil.getPageList(ParamsUtil.pageNum(map),
                    ParamsUtil.pageSize(map), NftMarketListDao.getInstance().loadMsg());
            if(list.size()>0){
                list.forEach(nftCode-> {
                    MarketNftMsg msg = NftUtil.initMarketNftMsg(nftCode);
                    if(msg!=null){
                        retList.add(msg);
                    }
                });
            }
        }
        //传输的jsonMap，先填充list
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("serverTbln", retList);
        //推送结果
        SendMsgUtil.sendBySessionAndList(session, status, jsonMap);
    }

    /**
     * 市场NFT信息
     */
    public static void marketNftMsg(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.nftMsg(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);//NFT编号
            //填充市场NFT信息
            status = NftUtil.initMarketNftMsg(nftCode, dataMap);
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 购买NFT
     */
    public static void buyNft(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.buyNft(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);//NFT编号
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int userId = ParamsUtil.userId(map);//玩家ID
                    int nftType = NftUtil.loadNftType(nftCode);//NFT类型
                    if(nftType==NftTypeEnum.SELL_COIN_MACHINE.getCode()){
                        //售币机
                        status = SellGoldMachineUtil.buyNft(userId, nftCode);
                    }else{
                        status = ClientCode.NFT_NO_EXIST.getCode();//无对应NFT信息
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }
}
