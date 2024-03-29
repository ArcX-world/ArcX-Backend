package avatar.util.nft;

import avatar.data.nft.*;
import avatar.entity.nft.*;
import avatar.global.enumMsg.basic.nft.NftAttributeTypeEnum;
import avatar.global.enumMsg.basic.nft.NftStatusEnum;
import avatar.global.enumMsg.basic.nft.NftTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.nft.info.*;
import avatar.module.nft.record.NftHoldHistoryDao;
import avatar.module.nft.record.SellGoldMachineGoldHistoryDao;
import avatar.module.nft.record.SellGoldMachineOperateHistoryDao;
import avatar.util.LogUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.basic.ImgUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.recharge.RechargeGoldUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserUsdtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 售币机工具类
 */
public class SellGoldMachineUtil {
    /**
     * 展示图片
     */
    public static String showImg(){
        //查询配置信息
        NftConfigEntity entity = NftConfigDao.getInstance().loadMsg();
        return entity==null?"": MediaUtil.getMediaUrl(entity.getStoreShowImg());
    }

    /**
     * 获取销售中的售币机
     */
    public static String loadOperateMachine() {
        //查询列表
        List<String> list = OperateSellGoldMachineListDao.getInstance().loadMsg();
        return list.size()==0?"":list.get(0);
    }

    /**
     * 处理售币机信息
     */
    public static void dealSellGoldMachineMsg(int userId, String nftCode, Map<String, Object> dataMap) {
        //查询售币机信息
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        dataMap.put("nftCd", nftCode);//NFT编号
        long usdtExchange = (long)(entity.getOperatePrice()*1000000);
        dataMap.put("usdtExc", usdtExchange);//USDT兑换值
        double usdtBalance = UserUsdtUtil.usdtBalance(userId);//USDT余额
        dataMap.put("usdtBl", (long)usdtBalance);//玩家USDT余额
        long storedNum = entity.getGoldNum();//储币量
        dataMap.put("maxUsdt", (long)(storedNum/usdtBalance));//可购买的上限USDT
        dataMap.put("maxVl", storedNum);//上限储币量
    }

    /**
     * 检测兑换NFT的金币
     */
    public static int checkExchangeNftGold(Map<String, Object> map, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();//成功
        int userId = ParamsUtil.userId(map);//玩家ID
        String nftCode = ParamsUtil.stringParmasNotNull(map, "nftCd");//NFT编号
        long usdtAmount = ParamsUtil.longParmasNotNull(map, "usdtAmt");//购买的USDT编号
        long usdtExc = ParamsUtil.longParmasNotNull(map, "usdtExc");//USDT兑换值
        if(entity!=null && entity.getStatus()==NftStatusEnum.IN_OPERATION.getCode() && entity.getUserId()!=0){
            long exchangeNum = (long)(entity.getOperatePrice()*1000000);//兑换价格
            if(exchangeNum!=usdtExc){
                status = ClientCode.NFT_PRICE_CHANGE.getCode();//价格变动
            }else {
                if (exchangeNum*usdtAmount>entity.getGoldNum()){
                    status = ClientCode.NFT_STORED_NO_ENOUGH.getCode();//储币不足
                }else{
                    //扣除对应USDT
                    boolean flag = UserCostLogUtil.costNftGoldUsdt(userId, nftCode, usdtAmount);
                    if(!flag){
                        status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
                    }
                }
            }
        }else{
            status = ClientCode.NFT_OFF_OPERATE.getCode();//非营业中
        }
        return status;
    }

    /**
     * NFT兑换金币
     */
    public static void exchangeNftGold(Map<String, Object> map, SellGoldMachineMsgEntity entity) {
        int userId = ParamsUtil.userId(map);//玩家ID
        String nftCode = ParamsUtil.stringParmasNotNull(map, "nftCd");//NFT编号
        long usdtAmount = ParamsUtil.longParmasNotNull(map, "usdtAmt");//购买的USDT编号
        long exchangeNum = (long)(entity.getOperatePrice()*1000000);//兑换价格
        long totalGold = exchangeNum*usdtAmount;//售出的金币
        //添加玩家获得的金币
        UserCostLogUtil.addNftGold(userId, nftCode, totalGold);
        //扣除储币值（储币值不够1USDT则变更为非营业状态）
        costMachineStoredGold(entity, exchangeNum, totalGold);
        //添加售币记录
        double earn = addSellGoldHistory(entity, userId, usdtAmount, totalGold);
        //添加持有者USDT
        UserCostLogUtil.addNftGoldEarn(entity.getUserId(), nftCode, earn);
    }

    /**
     * 扣除储币值（储币值不够1USDT则变更为非营业状态）
     */
    private static void costMachineStoredGold(SellGoldMachineMsgEntity entity, long exchangeNum, long totalGold) {
        boolean updateStatusFlag = false;//是否变更状态
        String startOperateTime = entity.getStartOperateTime();//开始营业时间
        entity.setGoldNum(entity.getGoldNum()-totalGold);//储币值
        if(entity.getGoldNum()<exchangeNum){
            //储币值不够
            entity.setStatus(NftStatusEnum.UNUSED.getCode());//闲置中
            entity.setSellTime(0);//售币次数
            entity.setStartOperateTime("");//开始经营时间
            long operateMinute = loadOperateMinute(startOperateTime);//营业时间
            entity.setDurability(Math.max(0, entity.getDurability()-operateMinute));//耐久度
            entity.setExpNum(entity.getExpNum()+operateMinute);//经验
            updateStatusFlag = true;//变更状态
        }else{
            entity.setSellTime(entity.getSellTime()+1);//售币次数
        }
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
        if(updateStatusFlag){
            //添加经营记录
            SellGoldMachineOperateHistoryDao.getInstance().insert(initSellGoldMachineOperateHistoryEntity(
                    entity.getUserId(), entity.getNftCode(), startOperateTime));
        }
    }

    /**
     * 获取经营分钟数
     */
    private static long loadOperateMinute(String durabilityUpdateTime) {
        long totalTime = TimeUtil.getNowTime()-TimeUtil.strToLong(durabilityUpdateTime);//总计消耗时间
        long minuteTime = 60000;//分钟时间
        long totalDurability = totalTime/minuteTime;//总消耗耐久度
        if(totalTime%minuteTime>0){
            totalDurability += 1;
        }
        return totalDurability;
    }

    /**
     * 填充经营记录实体信息
     */
    private static SellGoldMachineOperateHistoryEntity initSellGoldMachineOperateHistoryEntity(int userId, String nftCode,
            String startOperateTime) {
        SellGoldMachineOperateHistoryEntity entity = new SellGoldMachineOperateHistoryEntity();
        entity.setNftCode(nftCode);//NFT编号
        entity.setUserId(userId);//玩家ID
        entity.setCreateTime(startOperateTime);//创建时间
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        return entity;
    }

    /**
     * 添加售币机售币记录
     */
    private static double addSellGoldHistory(SellGoldMachineMsgEntity entity, int userId, long usdtAmount, long totalGold) {
        //获取售币机费率
        double fee = sellCoinMachineTax()*1.0/100;
        double costFee = StrUtil.truncateNmDecimal(usdtAmount*fee, 4);//扣除的费率
        double realEarn = StrUtil.truncateNmDecimal(usdtAmount-costFee, 4);//实际收入
        //添加数据
        SellGoldMachineGoldHistoryDao.getInstance().insert(initSellGoldMachineGoldHistoryEntity(
                entity, userId, totalGold, usdtAmount, costFee, realEarn));
        return realEarn;
    }

    /**
     * 售币机费率
     */
    private static int sellCoinMachineTax() {
        //查询配置信息
        NftConfigEntity entity = NftConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getOperateTax();
    }

    /**
     * 填充售币机售币记录
     */
    private static SellGoldMachineGoldHistoryEntity initSellGoldMachineGoldHistoryEntity(SellGoldMachineMsgEntity msgEntity,
            int buyUserId, long totalGold, long usdtAmount, double costFee, double realEarn) {
        SellGoldMachineGoldHistoryEntity entity = new SellGoldMachineGoldHistoryEntity();
        entity.setNftCode(msgEntity.getNftCode());//NFT编号
        entity.setOperatePrice(msgEntity.getOperatePrice());//经营价格(百万)
        entity.setUserId(msgEntity.getUserId());//玩家ID
        entity.setBuyUserId(buyUserId);//购币玩家ID
        entity.setGoldNum(totalGold);//购币数量
        entity.setBalanceNum(msgEntity.getGoldNum());//剩余数量
        entity.setUsdtNum(usdtAmount);//USDT价格
        entity.setTax(sellCoinMachineTax());//营业税(%)
        entity.setTaxNum(costFee);//费率
        entity.setRealEarn(realEarn);//实际收入
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }

    /**
     * 填充背包信息
     */
    public static void fillKnapsackMsg(String nftCode, NftKnapsackMsg msg) {
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        msg.setNftCd(entity.getNftCode());//NFT编号
        msg.setNm(entity.getNftName());//名称
        msg.setPct(ImgUtil.nftImg(entity.getImgId()));//图片
        msg.setNftTp(NftTypeEnum.SELL_COIN_MACHINE.getCode());//NFT类型：售币机
        msg.setStat(entity.getStatus());//状态
        msg.setAtbTbln(conciseAttributeList(entity));
    }

    /**
     * 属性列表
     */
    private static List<ConciseNftAttributeMsg> conciseAttributeList(SellGoldMachineMsgEntity entity) {
        List<ConciseNftAttributeMsg> retList = new ArrayList<>();
        List<NftAttributeTypeEnum> typeList = NftAttributeTypeEnum.loadAll();
        if(typeList.size()>0){
            typeList.forEach(enumMsg->{
                int attributeType = enumMsg.getCode();//属性类型
                switch (attributeType){
                    case 1:
                        //经验等级
                        retList.add(initConciseExpAttribute(attributeType, entity));
                        break;
                    case 2:
                        //储币空间
                        retList.add(initConciseSpaceAttribute(attributeType, entity));
                        break;
                    case 3:
                        //进货折扣
                        retList.add(initConciseIncomeAttribute(attributeType, entity));
                        break;
                }
            });
        }
        return retList;
    }

    /**
     * 进货折扣
     */
    private static ConciseNftAttributeMsg initConciseIncomeAttribute(int attributeType, SellGoldMachineMsgEntity entity) {
        ConciseNftAttributeMsg msg = new ConciseNftAttributeMsg();
        msg.setAtbTp(attributeType);//属性类型
        int lv = entity.getSpaceLv();//储币空间等级
        //查询等级信息
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(lv);
        msg.setAtbIfo(configEntity==null?0:configEntity.getIncomeDiscount());//属性信息
        return msg;
    }

    /**
     * 简易储备空间属性
     */
    private static ConciseNftAttributeMsg initConciseSpaceAttribute(int attributeType, SellGoldMachineMsgEntity entity) {
        ConciseNftAttributeMsg msg = new ConciseNftAttributeMsg();
        msg.setAtbTp(attributeType);//属性类型
        int lv = entity.getSpaceLv();//储币空间等级
        //查询等级信息
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(lv);
        msg.setAtbIfo(configEntity==null?0:configEntity.getStoredMax());//属性信息
        return msg;
    }

    /**
     * 简易经验等级属性
     */
    private static ConciseNftAttributeMsg initConciseExpAttribute(int attributeType, SellGoldMachineMsgEntity entity) {
        ConciseNftAttributeMsg msg = new ConciseNftAttributeMsg();
        msg.setAtbTp(attributeType);//属性类型
        msg.setAtbIfo(entity.getLv());//属性信息
        return msg;
    }

    /**
     * 获取下个等级
     */
    private static int loadNextLevel(int lv, List<Integer> levelList) {
        int retLv = 0;
        if(levelList.size()>0) {
            for (int nxLv : levelList) {
                if (nxLv > lv) {
                    retLv = nxLv;
                    break;
                }
            }
        }
        return retLv;
    }

    /**
     * 填充NFT信息
     */
    public static void initNftMsg(String nftCode, Map<String, Object> dataMap) {
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        dataMap.put("nftCd", entity.getNftCode());//NFT编号
        dataMap.put("nm", entity.getNftName());//名称
        dataMap.put("pct", ImgUtil.nftImg(entity.getImgId()));//图片
        dataMap.put("nftTp", NftTypeEnum.SELL_COIN_MACHINE.getCode());//NFT类型
        dataMap.put("adAmt", entity.getAdv());//广告数
        dataMap.put("slCmdTp", entity.getSaleCommodityType());//交易商品类型
        dataMap.put("slTax", NftUtil.saleTax());//交易费(%)
        dataMap.put("opTm", loadOperateTime(entity));//已经营业的时间
        dataMap.put("stat", entity.getStatus());//状态
        dataMap.put("gdIfo", goldMsg(entity));//储币信息
        dataMap.put("durbtyIfo", durabilityMsg(entity));//耐久度信息
        dataMap.put("atbTbln", attributeList(entity));//属性列表
    }

    /**
     * 已经营业的时间
     */
    private static long loadOperateTime(SellGoldMachineMsgEntity entity) {
        long operateTime = 0;
        if(entity.getStatus()==NftStatusEnum.IN_OPERATION.getCode()){
            operateTime = (TimeUtil.getNowTime()-TimeUtil.strToLong(entity.getStartOperateTime()))/1000;
            operateTime = Math.max(0, operateTime);
        }
        return operateTime;
    }

    /**
     * 属性列表
     */
    private static List<NftAttributeMsg> attributeList(SellGoldMachineMsgEntity entity) {
        List<NftAttributeMsg> retList = new ArrayList<>();
        List<NftAttributeTypeEnum> typeList = NftAttributeTypeEnum.loadAll();
        if(typeList.size()>0){
            //查询等级列表
            List<Integer> lvList = SellGoldMachineUpLvListDao.getInstance().loadMsg();
            typeList.forEach(enumMsg->{
                int attributeType = enumMsg.getCode();//属性类型
                switch (attributeType){
                    case 1:
                        //经验等级
                        retList.add(initExpAttribute(attributeType, lvList, entity));
                        break;
                    case 2:
                        //储币空间
                        retList.add(initSpaceAttribute(attributeType, lvList, entity));
                        break;
                    case 3:
                        //进货折扣
                        retList.add(initIncomeAttribute(attributeType, lvList, entity));
                        break;
                }
            });
        }
        return retList;
    }

    /**
     * 进货折扣属性
     */
    private static NftAttributeMsg initIncomeAttribute(int attributeType, List<Integer> lvList,
            SellGoldMachineMsgEntity entity) {
        NftAttributeMsg msg = new NftAttributeMsg();
        msg.setAtbTp(attributeType);//玩家属性类型
        int incomeLv = entity.getIncomeLv();//进货折扣等级
        int userLv = entity.getLv();//玩家等级
        msg.setLv(incomeLv);//等级
        int nxLv = loadNextLevel(incomeLv, lvList);//下一等级
        msg.setNxLv(nxLv);//下一等级
        //查询配置信息
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(incomeLv);
        msg.setUpFlg((nxLv>0 && nxLv<userLv)
                ? YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());//是否可升级
        msg.setCmdTp(CommodityUtil.axc());//商品类型
        msg.setCsAmt(configEntity.getDiscountAxc());//商品数量
        msg.setLvAmt(configEntity.getIncomeDiscount());//当前等级数量
        //下一等级数量
        if(nxLv>0){
            configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(nxLv);
            msg.setNxLvAmt(configEntity==null?0:configEntity.getIncomeDiscount());
        }else{
            msg.setNxLvAmt(0);
        }
        msg.setSdNma(incomeLv);//当前进度分子
        msg.setSdDma(userLv);//当前进度分母
        return msg;
    }

    /**
     * 储币空间属性
     */
    private static NftAttributeMsg initSpaceAttribute(int attributeType, List<Integer> lvList,
            SellGoldMachineMsgEntity entity) {
        NftAttributeMsg msg = new NftAttributeMsg();
        msg.setAtbTp(attributeType);//玩家属性类型
        int spaceLv = entity.getSpaceLv();//空间等级
        int userLv = entity.getLv();//玩家等级
        msg.setLv(spaceLv);//等级
        int nxLv = loadNextLevel(spaceLv, lvList);//下一等级
        msg.setNxLv(nxLv);//下一等级
        //查询配置信息
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(spaceLv);
        msg.setUpFlg((nxLv>0 && nxLv<userLv)
                ? YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());//是否可升级
        msg.setCmdTp(CommodityUtil.axc());//商品类型
        msg.setCsAmt(configEntity.getStoredAxc());//商品数量
        msg.setLvAmt(configEntity.getStoredMax());//当前等级数量
        //下一等级数量
        if(nxLv>0){
            configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(nxLv);
            msg.setNxLvAmt(configEntity==null?0:configEntity.getStoredMax());
        }else{
            msg.setNxLvAmt(0);
        }
        msg.setSdNma(spaceLv);//当前进度分子
        msg.setSdDma(userLv);//当前进度分母
        return msg;
    }

    /**
     * 经验等级属性
     */
    private static NftAttributeMsg initExpAttribute(int attributeType, List<Integer> lvList,
            SellGoldMachineMsgEntity entity) {
        NftAttributeMsg msg = new NftAttributeMsg();
        msg.setAtbTp(attributeType);//玩家属性类型
        //等级
        int lv = entity.getLv();
        msg.setLv(lv);
        int nextLv = loadNextLevel(lv, lvList);//下一等级
        msg.setNxLv(nextLv);//下一等级
        //查询配置信息
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(lv);
        msg.setUpFlg((configEntity.getUpExp()>0 && loadMachineExp(entity)>=configEntity.getUpExp())
                ? YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());//是否可升级
        msg.setCmdTp(CommodityUtil.axc());//商品类型
        msg.setCsAmt(configEntity.getLvAxc());//商品数量
        msg.setLvAmt(lv);//当前等级数量
        msg.setNxLvAmt(nextLv);//下一等级
        msg.setSdNma(entity.getExpNum());//当前进度分子
        msg.setSdDma(configEntity.getUpExp());//当前进度分母
        return msg;
    }

    /**
     * 获取设备经验
     */
    private static long loadMachineExp(SellGoldMachineMsgEntity entity) {
        long expNum = entity.getExpNum();//经验
        if(entity.getStatus()==NftStatusEnum.IN_OPERATION.getCode() && !StrUtil.checkEmpty(entity.getStartOperateTime())){
            expNum += (Math.max(0, (TimeUtil.getNowTime()-TimeUtil.strToLong(entity.getStartOperateTime()))/60000));
        }
        return expNum;
    }

    /**
     * 耐久度信息
     */
    private static NftDurabilityMsg durabilityMsg(SellGoldMachineMsgEntity entity) {
        NftDurabilityMsg msg = new NftDurabilityMsg();
        msg.setDurbtyAmt(loadMachineDurability(entity));//耐久度
        long maxDurability = NftUtil.durability();//上限耐久度
        msg.setCmdTp(CommodityUtil.axc());//商品类型
        msg.setCsAmt(maxDurability-entity.getDurability());//商品数量（目前暂时按照1:1的比例计算）
        return msg;
    }

    /**
     * 获取设备耐久度
     */
    private static long loadMachineDurability(SellGoldMachineMsgEntity entity) {
        long durability = entity.getDurability();//耐久度
        if(entity.getStatus()==NftStatusEnum.IN_OPERATION.getCode() && !StrUtil.checkEmpty(entity.getStartOperateTime())){
            durability -= (Math.max(0, (TimeUtil.getNowTime()-TimeUtil.strToLong(entity.getStartOperateTime()))/60000));
        }
        return Math.max(0, durability);
    }

    /**
     * 储币信息
     */
    private static NftGoldMsg goldMsg(SellGoldMachineMsgEntity entity) {
        NftGoldMsg msg = new NftGoldMsg();
        msg.setCnGdAmt(entity.getGoldNum());//当前储币值
        //查询储币等级配置信息
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getSpaceLv());
        msg.setUpGdAmt(Math.max(0,(configEntity.getStoredMax()-entity.getGoldNum())));//可添加的储币值
        //查询折扣等级配置信息
        if(entity.getSpaceLv()!=entity.getIncomeLv()){
            configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getIncomeLv());
        }
        msg.setDscot(configEntity.getIncomeDiscount());//折扣(%)
        msg.setOriAmt(RechargeGoldUtil.basicGoldNum());//未打折前的金币数量
        msg.setTax(sellCoinMachineTax());//营业税(%)
        return msg;
    }

    /**
     * 检测NFT升级信息
     */
    public static int checkNftUpgrade(int userId, int attributeType, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(entity.getStatus()==NftStatusEnum.LISTING.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();//NFT状态错误
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();//NFT玩家不匹配
        }else {
            List<Integer> lvList = SellGoldMachineUpLvListDao.getInstance().loadMsg();//等级列表
            int nextLv = 0;//下一等级
            long costAxc = 0;//扣除AXC数量
            if (attributeType == NftAttributeTypeEnum.LV.getCode()) {
                //等级
                //查询配置信息
                SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getLv());
                nextLv = loadNextLevel(entity.getLv(), lvList);//下一等级
                costAxc = configEntity.getLvAxc();//扣除AXC数量
                if (entity.getExpNum() < configEntity.getUpExp()) {
                    status = ClientCode.UPGRADE_CONDITION_NOT_FIT.getCode();//升级条件不满足
                }
            } else if (attributeType == NftAttributeTypeEnum.SPACE.getCode()) {
                //储币空间
                //查询配置信息
                SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getSpaceLv());
                nextLv = loadNextLevel(entity.getSpaceLv(), lvList);//下一等级
                costAxc = configEntity.getStoredAxc();//扣除AXC数量
            } else if (attributeType == NftAttributeTypeEnum.INCOME.getCode()) {
                //进货折扣
                //查询配置信息
                SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getIncomeLv());
                nextLv = loadNextLevel(entity.getIncomeLv(), lvList);//下一等级
                costAxc = configEntity.getDiscountAxc();//扣除AXC数量
            }
            if(nextLv<=0){
                status = ClientCode.UPGRADE_CONDITION_NOT_FIT.getCode();//升级条件不满足
            }else if(UserCostLogUtil.costNftAttribute(entity.getUserId(), attributeType, entity.getNftCode(), costAxc)){
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
            }
        }
        return status;
    }

    /**
     * 处理NFT升级
     */
    public static void dealNftUpgrade(int attributeType, SellGoldMachineMsgEntity entity) {
        if(attributeType==NftAttributeTypeEnum.LV.getCode()){
            //等级
            int oriLv = entity.getLv();
            entity.setLv(oriLv+1);//等级
            //查询配置信息
            SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(oriLv);
            entity.setExpNum(Math.max(0, entity.getExpNum()-configEntity.getUpExp()));//经验值
        }else if(attributeType==NftAttributeTypeEnum.SPACE.getCode()){
            //储币空间
            entity.setSpaceLv(entity.getSpaceLv()+1);
        }else if(attributeType==NftAttributeTypeEnum.INCOME.getCode()){
            //进货折扣
            entity.setIncomeLv(entity.getIncomeLv()+1);
        }
        //更新
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**
     * 检测售币机增加储币信息
     */
    public static int checkSellGoldMachineAddCoin(int userId, long goldAmount, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(entity.getStatus()!=NftStatusEnum.UNUSED.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();//NFT状态错误
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();//NFT玩家不匹配
        }else {
            //查询配置信息
            SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getSpaceLv());
            SellGoldMachineUpConfigEntity incomeConfigEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getIncomeLv());
            long basicGoldNum = RechargeGoldUtil.basicGoldNum();//基础金币值
            long maxGold = configEntity.getStoredMax();
            double usdtAmount = 0;//usdt价格
            if(goldAmount>(maxGold-entity.getGoldNum()) || goldAmount<basicGoldNum){
                status = ClientCode.NFT_STORED_NUM_ERROR.getCode();//NFT储币值异常
            }else{
                //基础价格*折扣价格
                usdtAmount = StrUtil.truncateFourDecimal((goldAmount*1.0/basicGoldNum)*
                        (100-incomeConfigEntity.getIncomeDiscount())*1.0/100);
            }
            if(usdtAmount>0 && UserCostLogUtil.costSellGoldMachineAddCoin(userId, usdtAmount, entity.getNftCode())){
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
            }
        }
        return status;
    }

    /**
     * 处理售币机增加储币
     */
    public static void dealSellGoldMachineAddCoin(long goldAmount, SellGoldMachineMsgEntity entity) {
        entity.setGoldNum(entity.getGoldNum()+goldAmount);//储币值
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**
     * 检测售币机维修耐久度
     */
    public static int checkSellGoldMachineRepairDurability(int userId, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(entity.getStatus()!=NftStatusEnum.UNUSED.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();//NFT状态错误
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();//NFT玩家不匹配
        }else {
            long durability = NftUtil.durability()-entity.getDurability();
            if(durability>0 && UserCostLogUtil.costNftRepairDurability(userId, entity.getNftCode(), durability)){
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
            }
        }
        return status;
    }

    /**
     * 处理售币机维修耐久度
     */
    public static void dealSellGoldMachineRepairDurability(SellGoldMachineMsgEntity entity) {
        entity.setDurability(NftUtil.durability());//耐久度
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**
     * 检测售币机营业
     */
    public static int checkSellGoldMachineOperate(int userId, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(entity.getStatus()!=NftStatusEnum.UNUSED.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();//NFT状态错误
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();//NFT玩家不匹配
        }else if(entity.getDurability()<=0){
            status = ClientCode.NFT_DURABILITY.getCode();//售币机耐久度不足
        }
        return status;
    }

    /**
     * 处理售币机营业
     */
    public static void dealSellGoldMachineOperate(double salePrice, SellGoldMachineMsgEntity entity) {
        entity.setOperatePrice(salePrice);//经营价格(百万)
        entity.setStartOperateTime(TimeUtil.getNowTimeStr());//开始经营时间
        entity.setStatus(NftStatusEnum.IN_OPERATION.getCode());//营业中
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**
     * 检测售币机停止营业
     */
    public static int checkSellGoldMachineStopOperate(int userId, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(entity.getStatus()!=NftStatusEnum.IN_OPERATION.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();//NFT状态错误
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();//NFT玩家不匹配
        }
        return status;
    }

    /**
     * 处理售币机停止营业
     */
    public static void dealSellGoldMachineStopOperate(SellGoldMachineMsgEntity entity) {
        entity.setStatus(NftStatusEnum.UNUSED.getCode());//闲置中
        entity.setSellTime(0);//售币次数
        entity.setStartOperateTime("");//开始经营时间
        long operateMinute = loadOperateMinute(entity.getStartOperateTime());//营业时间
        entity.setDurability(Math.max(0, entity.getDurability()-operateMinute));//耐久度
        entity.setExpNum(entity.getExpNum()+operateMinute);//经验
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**
     * 检测售币机上架市场
     */
    public static int checkSellGoldMachineListMarket(int userId, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(entity.getStatus()!=NftStatusEnum.UNUSED.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();//NFT状态错误
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();//NFT玩家不匹配
        }
        return status;
    }

    /**
     * 处理售币机上架市场
     */
    public static void dealSellGoldMachineListMarket(long salePrice, SellGoldMachineMsgEntity entity) {
        entity.setSaleNum(salePrice);//出售价格
        entity.setStatus(NftStatusEnum.LISTING.getCode());//上架状态
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**
     * 检测售币机取消上架市场
     */
    public static int checkSellGoldMachineCancelMarket(int userId, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(entity.getStatus()!=NftStatusEnum.LISTING.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();//NFT状态错误
        }else if(entity.getUserId()!=userId){
            status = ClientCode.NFT_USER_ERROR.getCode();//NFT玩家不匹配
        }
        return status;
    }

    /**
     * 处理售币机取消上架市场
     */
    public static void dealSellGoldMachineCancelMarket(SellGoldMachineMsgEntity entity) {
        entity.setStatus(NftStatusEnum.UNUSED.getCode());//闲置
        SellGoldMachineMsgDao.getInstance().update(entity.getUserId(), entity);
    }

    /**
     * 填充NFT报告信息
     */
    public static NftReportMsg initNftReportMsg(SellGoldMachineGoldHistoryEntity entity) {
        NftReportMsg msg = new NftReportMsg();
        msg.setOpTm(TimeUtil.strToLong(entity.getCreateTime()));//出售时间
        msg.setOpPrc((long)(1.1*1000000));//出售价格
        msg.setInc(entity.getRealEarn());//实际收入
        msg.setBlAmt(entity.getGoldNum());//金币数
        msg.setTax(entity.getTax());//费率
        return msg;
    }

    /**
     * 填充NFT市场信息
     */
    public static void fillMarketNftMsg(SellGoldMachineMsgEntity entity, MarketNftMsg msg) {
        msg.setNftCd(entity.getNftCode());//NFT编号
        msg.setNm(entity.getNftName());//名称
        msg.setPct(ImgUtil.nftImg(entity.getImgId()));//图片
        msg.setNftTp(NftTypeEnum.SELL_COIN_MACHINE.getCode());//NFT类型：售币机
        msg.setSlCmdTp(entity.getSaleCommodityType());//出售商品类型
        msg.setSlAmt(entity.getSaleNum());//出售价格
        msg.setAtbTbln(conciseAttributeList(entity));
    }

    /**
     * 市场NFT信息
     */
    public static void initMarketNftMsg(String nftCode, Map<String, Object> dataMap) {
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        dataMap.put("nftCd", entity.getNftCode());//NFT编号
        dataMap.put("nm", entity.getNftName());//名称
        dataMap.put("pct", ImgUtil.nftImg(entity.getImgId()));//图片
        dataMap.put("nftTp", NftTypeEnum.SELL_COIN_MACHINE.getCode());//NFT类型
        dataMap.put("atbTbln", marketAttributeList(entity));//属性列表
    }

    /**
     * 市场NFT信息
     */
    private static List<MarketNftAttributeMsg> marketAttributeList(SellGoldMachineMsgEntity entity) {
        List<MarketNftAttributeMsg> retList = new ArrayList<>();
        List<NftAttributeTypeEnum> typeList = NftAttributeTypeEnum.loadAll();
        if(typeList.size()>0){
            typeList.forEach(enumMsg->{
                int attributeType = enumMsg.getCode();//属性类型
                switch (attributeType){
                    case 1:
                        //经验等级
                        retList.add(initMarketExpAttribute(attributeType, entity));
                        break;
                    case 2:
                        //储币空间
                        retList.add(initMarketSpaceAttribute(attributeType, entity));
                        break;
                    case 3:
                        //进货折扣
                        retList.add(initMarketIncomeAttribute(attributeType, entity));
                        break;
                }
            });
        }
        return retList;
    }

    /**
     * 市场进货折扣属性信息
     */
    private static MarketNftAttributeMsg initMarketIncomeAttribute(int attributeType, SellGoldMachineMsgEntity entity) {
        MarketNftAttributeMsg msg = new MarketNftAttributeMsg();
        msg.setAtbTp(attributeType);//玩家属性类型
        msg.setLv(entity.getSpaceLv());//等级
        msg.setAtbMsg(entity.getGoldNum()+"");//属性信息
        return msg;
    }

    /**
     * 市场储币空间属性信息
     */
    private static MarketNftAttributeMsg initMarketSpaceAttribute(int attributeType, SellGoldMachineMsgEntity entity) {
        MarketNftAttributeMsg msg = new MarketNftAttributeMsg();
        msg.setAtbTp(attributeType);//玩家属性类型
        msg.setLv(entity.getSpaceLv());//等级
        //查询配置信息
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getSpaceLv());
        msg.setAtbMsg(configEntity.getIncomeDiscount()+"%");//属性信息
        return msg;
    }

    /**
     * 市场经验等级属性信息
     */
    private static MarketNftAttributeMsg initMarketExpAttribute(int attributeType, SellGoldMachineMsgEntity entity) {
        MarketNftAttributeMsg msg = new MarketNftAttributeMsg();
        msg.setAtbTp(attributeType);//玩家属性类型
        msg.setLv(entity.getLv());//等级
        //查询配置信息
        SellGoldMachineUpConfigEntity configEntity = SellGoldMachineUpConfigDao.getInstance().loadMsg(entity.getLv());
        msg.setAtbMsg(configEntity==null?"0":configEntity.getUpExp()+"");//属性信息
        return msg;
    }

    /**
     * 购买售币机
     */
    public static int buyNft(int userId, String nftCode) {
        //查询售币机信息
        SellGoldMachineMsgEntity entity = SellGoldMachineMsgDao.getInstance().loadMsg(nftCode);
        //检测购买NFT
        int status = checkBuyNft(userId, entity);
        if(ParamsUtil.isSuccess(status)){
            //处理购买NFT
            dealBuyNft(userId, entity);
        }
        return status;
    }

    /**
     * 检测购买NFT
     */
    private static int checkBuyNft(int userId, SellGoldMachineMsgEntity entity) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(entity.getStatus()!=NftStatusEnum.LISTING.getCode()){
            status = ClientCode.NFT_STATUS_ERROR.getCode();//NFT状态错误
        }else if(!UserCostLogUtil.costBuyNft(userId, entity)){
            status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
        }
        return status;
    }

    /**
     * 处理购买NFT
     */
    private static void dealBuyNft(int userId, SellGoldMachineMsgEntity entity) {
        int oriUserId = entity.getUserId();//原持有玩家ID
        entity.setUserId(userId);//持有玩家ID
        entity.setStatus(NftStatusEnum.UNUSED.getCode());//状态：闲置
        boolean flag = SellGoldMachineMsgDao.getInstance().update(oriUserId, entity);
        if(flag){
            //添加原持有人对应的营收
            UserCostLogUtil.addSaleNftEarn(oriUserId, entity.getSaleCommodityType(),
                    NftUtil.loadSaleNftEarn(entity.getSaleNum()), entity.getNftCode());
            //添加NFT持有记录
            NftHoldHistoryDao.getInstance().insert(NftUtil.initNftHoldHistoryEntity(
                    NftTypeEnum.SELL_COIN_MACHINE.getCode(), entity.getNftCode(), userId));
        }else{
            LogUtil.getLogger().error("玩家{}购买售币机NFT{}，更新失败-------", userId, entity.getNftCode());
        }
    }
}
