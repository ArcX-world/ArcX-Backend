package avatar.service.nft;

import avatar.data.nft.NftKnapsackMsg;
import avatar.data.nft.NftReportMsg;
import avatar.entity.nft.SellGoldMachineGoldHistoryEntity;
import avatar.entity.nft.SellGoldMachineMsgEntity;
import avatar.global.lockMsg.LockMsg;
import avatar.module.nft.info.SellGoldMachineMsgDao;
import avatar.module.nft.record.SellGoldMachineGoldHistoryDao;
import avatar.module.nft.user.UserNftListDao;
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
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NFT背包接口实现类
 */
public class NftKnapsackService {
    /**
     * NFT背包
     */
    public static void nftKnapsack(Map<String, Object> map, Session session) {
        List<NftKnapsackMsg> retList = new ArrayList<>();
        //检测参数
        int status = CheckParamsUtil.checkAccessTokenPage(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            //查询背包信息
            List<String> list = ListUtil.getPageList(ParamsUtil.pageNum(map),
                    ParamsUtil.pageSize(map), UserNftListDao.getInstance().loadMsg(userId));
            if(list.size()>0){
                list.forEach(nftCode-> {
                    NftKnapsackMsg msg = NftUtil.initNftKnapsackMsg(nftCode);
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
     * NFT信息
     */
    public static void nftMsg(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.nftMsg(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);//NFT编号
            //填充NFT信息
            status = NftUtil.initNftMsg(nftCode, dataMap);
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * NFT升级
     */
    public static void upgradeNft(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.upgradeNft(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);//NFT编号
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int attributeType = ParamsUtil.intParmasNotNull(map, "atbTp");
                    int userId = ParamsUtil.userId(map);//玩家ID
                    //查询售币机信息
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    //检测NFT升级信息
                    status = SellGoldMachineUtil.checkNftUpgrade(userId, attributeType, entity);
                    if(ParamsUtil.isSuccess(status)){
                        //处理NFT升级
                        SellGoldMachineUtil.dealNftUpgrade(attributeType, entity);
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

    /**
     * 售币机增加储币
     */
    public static void sellGoldMachineAddCoin(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.sellGoldMachineAddCoin(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);//NFT编号
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    long goldAmount = ParamsUtil.longParmasNotNull(map, "gdAmt");//补货的金币数量
                    int userId = ParamsUtil.userId(map);//玩家ID
                    //查询售币机信息
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    //检测售币机增加储币信息
                    status = SellGoldMachineUtil.checkSellGoldMachineAddCoin(userId, goldAmount, entity);
                    if(ParamsUtil.isSuccess(status)){
                        //处理售币机增加储币
                        SellGoldMachineUtil.dealSellGoldMachineAddCoin(goldAmount, entity);
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

    /**
     * 售币机维修耐久度
     */
    public static void sellGoldMachineRepairDurability(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.sellGoldMachineRepairDurability(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);//NFT编号
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int userId = ParamsUtil.userId(map);//玩家ID
                    //查询售币机信息
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    //检测售币机维修耐久度
                    status = SellGoldMachineUtil.checkSellGoldMachineRepairDurability(userId, entity);
                    if(ParamsUtil.isSuccess(status)){
                        //处理售币机维修耐久度
                        SellGoldMachineUtil.dealSellGoldMachineRepairDurability(entity);
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

    /**
     * 售币机营业
     */
    public static void sellGoldMachineOperate(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.sellGoldMachineOperate(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);//NFT编号
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int userId = ParamsUtil.userId(map);//玩家ID
                    double salePrice = StrUtil.truncateTwoDecimal(
                            ParamsUtil.doubleParmasNotNull(map, "slPrc"));//出售价格
                    //查询售币机信息
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    //检测售币机营业
                    status = SellGoldMachineUtil.checkSellGoldMachineOperate(userId, entity);
                    if(ParamsUtil.isSuccess(status)){
                        //处理售币机营业
                        SellGoldMachineUtil.dealSellGoldMachineOperate(salePrice, entity);
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

    /**
     * 售币机停止营业
     */
    public static void sellGoldMachineStopOperate(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.sellGoldMachineStopOperate(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);//NFT编号
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int userId = ParamsUtil.userId(map);//玩家ID
                    //查询售币机信息
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    //检测售币机停止营业
                    status = SellGoldMachineUtil.checkSellGoldMachineStopOperate(userId, entity);
                    if(ParamsUtil.isSuccess(status)){
                        //处理售币机停止营业
                        SellGoldMachineUtil.dealSellGoldMachineStopOperate(entity);
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

    /**
     * 售币机上架市场
     */
    public static void sellGoldMachineListMarket(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.sellGoldMachineListMarket(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);//NFT编号
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int userId = ParamsUtil.userId(map);//玩家ID
                    long salePrice = ParamsUtil.longParmasNotNull(map, "slPrc");//出售价格
                    //查询售币机信息
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    //检测售币机上架市场
                    status = SellGoldMachineUtil.checkSellGoldMachineListMarket(userId, entity);
                    if(ParamsUtil.isSuccess(status)){
                        //处理售币机上架市场
                        SellGoldMachineUtil.dealSellGoldMachineListMarket(salePrice, entity);
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

    /**
     * 售币机取消上架市场
     */
    public static void sellGoldMachineCalcelMarket(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.sellGoldMachineCancelMarket(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);//NFT编号
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    int userId = ParamsUtil.userId(map);//玩家ID
                    //查询售币机信息
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    //检测售币机取消上架市场
                    status = SellGoldMachineUtil.checkSellGoldMachineCancelMarket(userId, entity);
                    if(ParamsUtil.isSuccess(status)){
                        //处理售币机取消上架市场
                        SellGoldMachineUtil.dealSellGoldMachineCancelMarket(entity);
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

    /**
     * NFT报告
     */
    public static void nftReport(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.nftReport(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.nftCode(map);//NFT编号
            List<NftReportMsg> reportList = new ArrayList<>();//报告列表
            //盈利总额
            double earnAmount = SellGoldMachineGoldHistoryDao.getInstance().loadDbEarn(nftCode);
            //查询列表
            List<SellGoldMachineGoldHistoryEntity> list = SellGoldMachineGoldHistoryDao.getInstance().
                    loadDbReport(nftCode, ParamsUtil.pageNum(map), ParamsUtil.pageSize(map));
            if(list.size()>0){
                list.forEach(entity-> reportList.add(SellGoldMachineUtil.initNftReportMsg(entity)));
            }
            dataMap.put("ttAmt", earnAmount);//总收益
            dataMap.put("opTbln", list);//经营列表
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }
}
