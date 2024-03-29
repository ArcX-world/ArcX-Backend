package avatar.util.product;

import avatar.data.product.gamingMsg.*;
import avatar.data.product.normalProduct.InnerProductJsonMapMsg;
import avatar.data.user.info.ConciseUserMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.product.innoMsg.InnoProductWinPrizeMsgEntity;
import avatar.entity.product.productType.ProductSecondLevelTypeEntity;
import avatar.entity.product.repair.ProductRepairEntity;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductStatusEnum;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.module.product.gaming.*;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.product.innoMsg.InnoProductWinPrizeMsgDao;
import avatar.module.product.instruct.InstructCatchDollDao;
import avatar.module.product.instruct.InstructPresentDao;
import avatar.module.product.instruct.InstructPushCoinDao;
import avatar.module.product.productType.LotteryCoinPercentDao;
import avatar.module.product.productType.ProductSecondLevelTypeDao;
import avatar.module.product.repair.ProductRepairDao;
import avatar.module.user.online.UserProductOnlineListDao;
import avatar.module.user.product.UserLotteryMsgDao;
import avatar.net.session.Session;
import avatar.util.LogUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.TimeUtil;
import avatar.util.thirdpart.OfficialAccountUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备工具类
 */
public class ProductUtil {
    /**
     * 是否娃娃机（包括礼品机）
     */
    public static boolean isDollMachine(int productId) {
        int productType = loadProductType(productId);//设备类型
        return productType==ProductTypeEnum.DOLL_MACHINE.getCode() ||
                productType==ProductTypeEnum.PRESENT_MACHINE.getCode();
    }

    /**
     * 获取设备类型
     */
    public static int loadProductType(int productId) {
        //查询设备信息
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        return entity==null?0:entity.getProductType();
    }

    /**
     * 是否自研设备
     */
    public static boolean isInnoProduct(int productId) {
        boolean flag = false;
        //查询二级分类
        int secondType = loadSecondType(productId);
        if(secondType>0){
            //查询设备二级分类信息
            ProductSecondLevelTypeEntity entity = ProductSecondLevelTypeDao.getInstance().loadBySecondType(secondType);
            flag = entity!=null && entity.getIsInnoProduct()== YesOrNoEnum.YES.getCode();
        }
        return flag;
    }

    /**
     * 获取设备二级分类
     */
    public static int loadSecondType(int productId) {
        //查询设备信息
        ProductInfoEntity infoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        return infoEntity==null?0:infoEntity.getSecondType();
    }

    /**
     * 获取设备信息日志
     */
    public static String productLog(int productId){
        return productId+"-"+getProductTypeName(productId)+"-"+loadProductName(productId);
    }

    /**
     * 获取设备类型名称
     */
    public static String getProductTypeName(int productId) {
        String retName = "";
        //查询设备信息
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        int productType = entity.getProductType();//设备类型
        if(productType>0){
            retName = ProductTypeEnum.loadNameByCode(productType);
        }
        return retName;
    }

    /**
     * 获取设备名称
     */
    public static String loadProductName(int productId) {
        String name = "";//设备名称
        //查询设备信息
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        if(entity!=null){
            name = entity.getProductName();
        }
        return name;
    }

    /**
     * 设备价格
     */
    public static int productCost(int productId) {
        //查询设备信息
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        int cost = entity==null?0:entity.getCost();
        if(isInnoProduct(productId)) {
            //查询设备锁定信息
            ProductAwardLockMsg lockMsg = ProductAwardLockDao.getInstance().loadByProductId(productId);
            if (lockMsg.getCoinMulti() > 0) {
                cost = lockMsg.getCoinMulti();
            }
        }
        return cost;
    }

    /**
     * 处理自研设备中奖信息
     */
    public static void dealSelfProductAwardMsg(List<Long> list, List<Long> awardList, long onProductTime) {
        if(list.size()>0){
            for(long awardId : list){
                //查询中奖信息
                InnoProductWinPrizeMsgEntity entity = InnoProductWinPrizeMsgDao.getInstance().loadDbById(awardId);
                long createTime = TimeUtil.strToLong(entity.getCreateTime());//奖项创建时间
                if(entity.getIsStart()==YesOrNoEnum.YES.getCode() && createTime>=onProductTime
                        && (TimeUtil.getNowTime()-createTime)<=ProductConfigMsg.innoAwardTillTime){
                    //还在中奖并且是当前玩家
                    awardList.add(awardId);
                }
            }
        }
    }

    /**
     * 设备剩余时间
     */
    public static long loadProductLeftTime(long refreshTime, int productId) {
        long leftTime = 0;//剩余时间
        long leftLongTime = TimeUtil.getNowTime()-refreshTime;//剩余时间戳
        int productOffTime = productOffLineTime(productId)-10;//设备下机时间
        if(leftLongTime>=0){
            leftTime = Math.max(0,(productOffTime-leftLongTime/1000));
        }
        return leftTime;
    }

    /**
     * 设备下机时间
     */
    public static int productOffLineTime(int productId){
        int offLineTime = 0;//下机时间
        int secondType = loadSecondType(productId);
        if(secondType>0){
            //查询设备二级分类信息
            ProductSecondLevelTypeEntity entity = ProductSecondLevelTypeDao.getInstance().loadBySecondType(secondType);
            offLineTime = entity==null?0:entity.getServerOffLineTime();
        }
        return offLineTime;
    }

    /**
     * 刷新设备房间信息
     */
    public static void refreshRoomMsg(int productId) {
        //查询设备在线socket
        List<Session> sessionList = ProductSocketUtil.dealOnlineSession(productId);
        if(sessionList.size()>0){
            //填充设备房间信息
            JSONObject jsonMap = initProductRoomMsg(productId);
            sessionList.forEach(session-> SendWebsocketMsgUtil.sendBySession(WebSocketCmd.S2C_ROOM_MSG, ClientCode.SUCCESS.getCode(),
                    session, jsonMap));
        }
    }

    /**
     * 刷新设备房间信息
     */
    public static void refreshRoomMsg(int productId, int userId) {
        //填充设备房间信息
        JSONObject jsonMap = initProductRoomMsg(productId);
        //推送玩家
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_ROOM_MSG, ClientCode.SUCCESS.getCode(),
                userId, jsonMap);
    }

    /**
     * 填充设备房间信息
     */
    private static JSONObject initProductRoomMsg(int productId) {
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("devId", productId);//设备ID
        //查询游戏玩家信息
        ProductGamingUserMsg gamingUserMsg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
        if(gamingUserMsg.getUserId()>0){
            jsonMap.put("gmPly", CrossServerMsgUtil.initServerTypeUserMsg(
                    gamingUserMsg.getUserId(), gamingUserMsg.getServerSideType()));//游戏玩家
        }
        jsonMap.put("vsUsrTbln", loadProductVisitUserList(productId));//旁观人
        jsonMap.put("ppAmt", ProductSocketUtil.sessionMap.get(productId).size());//人气
        return jsonMap;
    }

    /**
     * 设备旁观
     */
    private static List<ConciseUserMsg> loadProductVisitUserList(int productId) {
        int gamingUserId = ProductGamingUtil.loadGamingUserId(productId);//游戏玩家
        List<ConciseUserMsg> retList = new ArrayList<>();
        //查询在线信息
        List<Integer> onlineList = UserProductOnlineListDao.getInstance().loadByProductId(productId);
        if(onlineList.size()>0){
            onlineList.forEach(userId-> {
                if(gamingUserId!=userId){
                    retList.add(UserUtil.initConciseUserMsg(userId));
                }
            });
        }
        return retList;
    }

    /**
     * 是否余额够扣除
     */
    public static boolean isEnoughCost(int userId, int productId, int coinMulti) {
        int cost = coinMulti>0?coinMulti:ProductUtil.productCost(productId);//游戏币
        long balance = UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode());
        return balance>=cost;
    }

    /**
     * 是否正常设备
     */
    public static boolean isNormalProduct(int productId){
        //查询设备信息
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        return entity!=null && entity.getStatus()== ProductStatusEnum.NORMAL.getCode();
    }

    /**
     * 判断是否结算指令
     */
    public static boolean isOffLine(int operate) {
        return operate == ProductOperationEnum.OFF_LINE.getCode();
    }

    /**
     * 设备IP
     */
    public static String productIp(int productId) {
        //查询设备信息
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        return entity==null?"":entity.getIp();
    }

    /**
     * 设备socket端口
     */
    public static int productSocketPort(int productId) {
        //查询设备信息
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        return entity==null?0:Integer.parseInt(entity.getPort());
    }

    /**
     * 获取设备号
     */
    public static String loadProductAlias(int productId) {
        //查询设备信息
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        return entity==null?"":entity.getAlias();
    }

    /**
     * 获取设备二级分类名称
     */
    public static String loadSecondTypeName(int secondType) {
        if(secondType>0) {
            //查询设备二级类型信息
            ProductSecondLevelTypeEntity entity = ProductSecondLevelTypeDao.getInstance().loadBySecondType(secondType);
            return entity == null ? "" : entity.getName();
        }else{
            return "";
        }
    }

    /**
     * 是否超出开始游戏获得币时间
     */
    public static boolean isOutStartGameTime(int productId) {
        boolean flag = false;
        //查询超时时间
        long startGameGetCoinTime = ProductConfigMsg.startGameGetCoinTime*1000;
        if(startGameGetCoinTime==0){
            flag = true;
        }else {
            //查询设备下机信息
            InnoProductOffLineMsg offLineMsg = ProductSettlementMsgDao.getInstance().loadByProductId(productId);
            long offTime = offLineMsg.getOffLineTime();//最近下机时间
            if (offTime==0 || (offTime > 0 && (TimeUtil.getNowTime() - offTime) >= startGameGetCoinTime)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 填充自研设备中奖实体信息
     */
    public static InnoProductWinPrizeMsgEntity initInnoProductWinPrizeMsgEntity(int userId, int productId,
            int awardType, int awardNum, int isStart) {
        InnoProductWinPrizeMsgEntity entity = new InnoProductWinPrizeMsgEntity();
        entity.setUserId(userId);//玩家ID
        entity.setProductId(productId);//设备ID
        entity.setAwardType(awardType);//奖励类型
        entity.setAwardNum(awardNum);//设备显示奖励游戏币
        entity.setIsStart(isStart);//是否中奖中
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        return entity;
    }

    /**
     * 添加报修信息
     */
    public static void addRepairMsg(int productId, int breakType) {
        //查询设备在线玩家
        ProductGamingUserMsg msg = loadGamingUser(productId);
        int userId = msg.getUserId();//玩家ID
        if(userId>0 && CrossServerMsgUtil.isArcxServer(msg.getServerSideType())){
            //查询设备信息
            ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
            //查询维修信息
            ProductRepairEntity repairProductEntity = ProductRepairDao.getInstance().loadByProductId(productId);
            if(productInfoEntity!=null && repairProductEntity==null){
                //添加数据
                ProductRepairDao.getInstance().insert(initProductRepairEntity(userId, productId, breakType));
            }else if(repairProductEntity!=null){
                LogUtil.getLogger().info("添加设备{}报修信息的时候报修信息已存在，不添加-----", productId);
            }else{
                LogUtil.getLogger().info("添加设备{}报修信息的时候查询不到设备信息-----", productId);
            }
            //推送机修维护信息
            OfficialAccountUtil.sendOfficalAccount(productId);
        }else{
            LogUtil.getLogger().info("添加设备{}报修信息的时候查询不到在线的玩家ID-----", productId);
        }
    }

    /**
     * 获取设备在玩玩家
     */
    private static ProductGamingUserMsg loadGamingUser(int productId){
        //查询游戏玩家信息
        return ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
    }

    /**
     * 填充报修设备实体信息
     */
    private static ProductRepairEntity initProductRepairEntity(int userId, int productId, int breakType) {
        ProductRepairEntity entity = new ProductRepairEntity();
        entity.setUserId(userId);//玩家ID
        entity.setProductId(productId);//设备ID
        entity.setBreakType(breakType);//故障类型
        entity.setStatus(YesOrNoEnum.NO.getCode());//是否已维护：否
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime("");//更新时间
        return entity;
    }

    /**
     * 处理推币机获得币
     */
    public static boolean checkPushCoinProductGetCoin(int productId, InnerProductJsonMapMsg jsonMapMsg) {
        boolean flag = true;
        int result = jsonMapMsg.getDataMap().get("retNum") == null ? 0 :
                (int) jsonMapMsg.getDataMap().get("retNum");//获得币结果
        if (result > 0) {
            flag = isOutStartGameTime(productId);
            if (!flag) {
                LogUtil.getLogger().error("设备{}获得币的时间未超出获得币间隔时间{}秒，不做得币处理-----", productId,
                        ProductConfigMsg.startGameGetCoinTime);
            }
        }
        return flag;
    }

    /**
     * 是否彩票设备
     */
    public static boolean isLotteryProduct(int secondType) {
        //查询设备二级分配信息
        ProductSecondLevelTypeEntity entity = ProductSecondLevelTypeDao.getInstance().loadBySecondType(secondType);
        return entity!=null && entity.getIsLotteryProduct()==YesOrNoEnum.YES.getCode();
    }

    /**
     * 查询设备指令
     */
    public static String loadInstruct(int productType, int secondType, String operateStr) {
        String instruct = "";
        if (productType == ProductTypeEnum.DOLL_MACHINE.getCode()) {
            //娃娃机
            instruct = getDollPushInstruct(operateStr, secondType);
        } else if (productType == ProductTypeEnum.PUSH_COIN_MACHINE.getCode()) {
            //推币机
            instruct = getCoinPusherInstruct(operateStr, secondType);
        } else if (productType == ProductTypeEnum.PRESENT_MACHINE.getCode()) {
            //礼品
            instruct = getPresentPushInstruct(operateStr, secondType);
        }
        return instruct;
    }

    /**
     * 获取礼品机设备操作指令
     */
    private static String getPresentPushInstruct(String operate, int levelType) {
        String sendStr = "";//操作指令
        //查询二级信息
        ProductSecondLevelTypeEntity levelEntity = ProductSecondLevelTypeDao.getInstance()
                .loadBySecondType(levelType);
        if (levelEntity != null) {
            String name = levelEntity.getInstructType() + "_" + operate;
            //获取指令信息
            sendStr = InstructPresentDao.getInstance().loadByName(name);
        }
        return sendStr;
    }

    /**
     * 获取抓娃娃设备操作指令
     */
    private static String getDollPushInstruct(String operate, int levelType) {
        String sendStr = "";//操作指令
        //查询二级信息
        ProductSecondLevelTypeEntity levelEntity = ProductSecondLevelTypeDao.getInstance()
                .loadBySecondType(levelType);
        if (levelEntity != null) {
            //todo 暂用强爪
            if(operate.equals(ProductOperationEnum.CATCH.getCode()+"")){
                operate = ProductOperationEnum.STRONG_CATCH.getCode()+"";
            }
            String name = levelEntity.getInstructType() + "_" + operate;
            //获取指令信息
            sendStr = InstructCatchDollDao.getInstance().loadByName(name);
        }
        return sendStr;
    }

    /**
     * 获取推币机操作指令
     */
    private static String getCoinPusherInstruct(String operate, int levelType) {
        String sendStr = "";//操作指令
        //查询二级信息
        ProductSecondLevelTypeEntity levelEntity = ProductSecondLevelTypeDao.getInstance()
                .loadBySecondType(levelType);
        if (levelEntity != null) {
            String name = levelEntity.getInstructType() + "_" + operate;
            //获取指令信息
            sendStr = InstructPushCoinDao.getInstance().loadByName(name);
        }
        return sendStr;
    }

    /**
     * 游戏次数
     */
    public static int startGameTime(int productId) {
        //查询缓存
        DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
        return gamingMsg==null?0:gamingMsg.getTime();
    }

    /**
     * 填充玩家彩票进度信息
     */
    public static LotteryMsg initUserLotteryMsg(int userId, int secondLevelType, int num, int addLotteryNum) {
        if (isLotteryProduct(secondLevelType)) {
            LotteryMsg msg = new LotteryMsg();
            //查询玩家设备分类彩票
            UserLotteryMsg entity = UserLotteryMsgDao.getInstance().loadByMsg(userId, secondLevelType);
            if (entity != null) {
                msg.setAddLotteryNum(addLotteryNum);//添加彩票数
                msg.setNum(entity.getLotteryNum());//彩票数
                //获取彩票兑比
                int maxNum = LotteryCoinPercentDao.getInstance().loadBySecondLevelType(secondLevelType);
                maxNum = maxNum > 0 ? maxNum : ProductConfigMsg.lotteryCoinExchange;
                msg.setMaxNum(maxNum);//彩票上限
                msg.setAddCoin(num);//添加的游戏币数
            } else {
                LogUtil.getLogger().info("获取玩家{}彩票分类{}的彩票信息失败--------", userId, secondLevelType);
            }
            return msg;
        } else {
            LogUtil.getLogger().info("不存在{}对应的彩票二级分类--------", secondLevelType);
            return null;
        }
    }

    /**
     * 通知玩家进入设备通知
     */
    public static void joinProductNotice(int productId, int userId) {
        List<Session> sessionList = ProductSocketUtil.dealOnlineSession(productId);
        if(sessionList.size()>0) {
            //填充进入设备通知信息
            JSONObject jsonMap = initJoinProductNoticeMsg(productId, userId);
            //推送玩家
            sessionList.forEach(session-> SendWebsocketMsgUtil.sendBySession(WebSocketCmd.S2C_JOIN_PRODUCT,
                    ClientCode.SUCCESS.getCode(), session, jsonMap));
        }
    }

    /**
     * 填充进入设备通知信息
     */
    private static JSONObject initJoinProductNoticeMsg(int productId, int userId) {
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("devId", productId);//设备ID
        jsonMap.put("plyId", userId);//玩家ID
        //获取简易玩家信息
        ConciseUserMsg conciseUserMsg = UserUtil.initConciseUserMsg(userId);
        jsonMap.put("plyNm", conciseUserMsg.getPlyNm());//玩家昵称
        jsonMap.put("plyPct", conciseUserMsg.getPlyPct());//玩家头像
        return jsonMap;
    }

    /**
     * 是否投币倍率存在
     */
    public static boolean isCoinMultiExist(int productId, int coinMulti) {
        boolean flag = false;
        if(isInnoProduct(productId)) {
            int secondType = loadSecondType(productId);//设备二级分类
            if (secondType > 0) {
                List<Integer> list = InnoPushCoinMultiDao.getInstance().loadBySecondType(secondType);
                if (list.size() > 0) {
                    flag = list.contains(coinMulti);
                }
            }
        }
        return flag;
    }

    /**
     * 是否指定设备类型机器
     */
    public static boolean isSpecifyMachine(int productId, int productType) {
        return loadProductType(productId)==productType;
    }

}
