package avatar.service.nft;

import avatar.entity.nft.SellGoldMachineMsgEntity;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.lockMsg.LockMsg;
import avatar.module.nft.info.SellGoldMachineMsgDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.checkParams.NftCheckParamsUtil;
import avatar.util.nft.SellGoldMachineUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 售币机接口实现类
 */
public class SellGoldMachineService {
    /**
     * 销售中的售币机
     */
    public static void operateSellGoldMachine(Map<String, Object> map, Session session) {
        int status = CheckParamsUtil.checkAccessToken(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            //获取销售中的售币机
            String nftCode = SellGoldMachineUtil.loadOperateMachine();
            if (!StrUtil.checkEmpty(nftCode)) {
                SellGoldMachineUtil.dealSellGoldMachineMsg(userId, nftCode, dataMap);
            } else {
                status = ClientCode.NO_SELL_GOLD_MACHINE.getCode();//暂无售币机
            }
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 兑换NFT的金币
     */
    public static void exchangeNftGold(Map<String, Object> map, Session session) {
        int status = NftCheckParamsUtil.exchangeNftGold(map);//成功
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(ParamsUtil.isSuccess(status)) {
            String nftCode = ParamsUtil.stringParmasNotNull(map, "nftCd");//NFT编号
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELL_GOLD_MACHINE_LOCK + "_" + nftCode,
                    2000);
            try {
                if (lock.lock()) {
                    //查询售币机信息
                    SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
                    //检测兑换NFT的金币
                    status = SellGoldMachineUtil.checkExchangeNftGold(map, entity);
                    if(ParamsUtil.isSuccess(status)){
                        //兑换金币
                        SellGoldMachineUtil.exchangeNftGold(map, entity);
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
