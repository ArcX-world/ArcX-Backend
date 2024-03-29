package avatar.util.recharge;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.recharge.RechargePropertyDetailMsg;
import avatar.data.recharge.RechargePropertyMsg;
import avatar.data.recharge.UserRechargePropertyMsg;
import avatar.entity.basic.systemMsg.PropertyMsgEntity;
import avatar.entity.recharge.property.RechargePropertyConfigEntity;
import avatar.entity.recharge.property.RechargePropertyMsgEntity;
import avatar.entity.recharge.property.RechargePropertyOrderEntity;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.basic.systemMsg.PropertyListDao;
import avatar.module.basic.systemMsg.PropertyMsgDao;
import avatar.module.recharge.property.*;
import avatar.task.recharge.AddRechargePropertyAwardTask;
import avatar.util.basic.AwardUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.basic.PropertyMsgUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 充值道具工具类
 */
public class RechargePropertyUtil {
    /**
     * 展示数量
     */
    private static int showNum() {
        //查询配置信息
        RechargePropertyConfigEntity entity = RechargePropertyConfigDao.getInstance().loadMsg();
        int showNum = entity==null?0:entity.getShowNum();
        return showNum==0?6:showNum;
    }

    /**
     * 获取在线充值道具
     */
    public static List<Integer> loadOnlineList() {
        List<Integer> retList = new ArrayList<>();
        int showNum = showNum();
        if(showNum>0){
            List<Integer> list = RechargePropertyListDao.getInstance().loadMsg();
            Collections.shuffle(list);
            for(int i=0;i<showNum;i++){
                retList.add(list.get(i));
                if(retList.size()==list.size()){
                    break;
                }
            }
        }
        return retList;
    }

    /**
     * 道具信息
     */
    public static RechargePropertyMsg loadPropertyMsg(int userId) {
        RechargePropertyMsg msg = new RechargePropertyMsg();
        msg.setRfTm((TimeUtil.getZeroLongTime(TimeUtil.getNextDay(TimeUtil.getNowTimeStr()))-TimeUtil.getNowTime())/1000);//下次刷新时间
        msg.setRfAxcAmt(loadRefreshAxcPrice());//刷新AXC价格
        msg.setPpyTbln(loadPropertyList(userId));//道具列表
        return msg;
    }

    /**
     * 道具列表
     */
    public static List<RechargePropertyDetailMsg> loadPropertyList(int userId) {
        List<RechargePropertyDetailMsg> retList = new ArrayList<>();
        List<Integer> propertyList = new ArrayList<>();
        //查询玩家信息
        UserRechargePropertyMsg userMsg = UserRechargePropertyListDao.getInstance().loadMsg(userId);
        if (userMsg != null && userMsg.getPropertyList().size() > 0) {
            propertyList = userMsg.getPropertyList();
        }
        if(propertyList.size()==0){
            //查询公共在线道具信息
            propertyList = RechargePropertyOnlineListDao.getInstance().loadMsg();
        }
        if(propertyList.size()>0){
            //激活的道具
            List<Integer> activeList = PropertyListDao.getInstance().loadMsg();
            //填充道具详情信息
            propertyList.forEach(id-> {
                if(activeList.contains(id)) {
                    RechargePropertyDetailMsg msg = initRechargePropertyDetailMsg(id, userMsg.getBuyList());
                    if (msg != null) {
                        retList.add(msg);
                    }
                }
            });
        }
        return retList;
    }

    /**
     * 玩家激活的列表
     */
    public static List<Integer> userActiveList(int userId){
        List<Integer> retList = new ArrayList<>();
        List<RechargePropertyDetailMsg> list = loadPropertyList(userId);
        if(list.size()>0){
            list.forEach(msg-> retList.add(msg.getCmdId()));
        }
        return retList;
    }

    /**
     * 填充道具详情信息
     */
    private static RechargePropertyDetailMsg initRechargePropertyDetailMsg(int id, List<Integer> buyList) {
        RechargePropertyDetailMsg msg = new RechargePropertyDetailMsg();
        msg.setCmdId(id);//商品ID
        //查询道具信息
        RechargePropertyMsgEntity entity = RechargePropertyMsgDao.getInstance().loadMsg(id);
        if(entity!=null){
            msg.setPpyAmt(entity.getNum());//道具数量
            //查询道具信息
            PropertyMsgEntity msgEntity = PropertyMsgDao.getInstance().loadMsg(entity.getPropertyType());
            if(msgEntity!=null) {
                msg.setNm(msgEntity.getName());//道具名称
                msg.setDsc(msgEntity.getDesc());//道具描述
                msg.setPct(MediaUtil.getMediaUrl(msgEntity.getImgUrl()));//图片
            }
            msg.setAxcAmt(entity.getPrice());//AXC价格
            msg.setSoFlg((!ParamsUtil.isConfirm(entity.getActiveFlag()) || buyList.contains(id))?
                    YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());//是否售罄
        }else{
            return null;
        }
        return msg;
    }

    /**
     * 获取刷新AXC价格
     */
    private static int loadRefreshAxcPrice() {
        //查询配置信息
        RechargePropertyConfigEntity entity = RechargePropertyConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getRefreshPrice();
    }

    /**
     * 填充玩家充值道具信息
     */
    public static UserRechargePropertyMsg initUserRechargePropertyMsg(int userId) {
        UserRechargePropertyMsg msg = new UserRechargePropertyMsg();
        msg.setUserId(userId);//玩家ID
        msg.setRefreshTime(TimeUtil.getNowTime());//刷新时间
        msg.setPropertyList(new ArrayList<>());//道具信息
        msg.setBuyList(new ArrayList<>());//已购买的列表
        return msg;
    }

    /**
     * 处理玩家返回的道具信息
     * 检测是否过了当天的刷新时间
     */
    public static UserRechargePropertyMsg dealUserRetPropertyMsg(UserRechargePropertyMsg msg) {
        if(TimeUtil.getZeroLongTime(TimeUtil.getNextDay(TimeUtil.getNowTimeStr()))>msg.getRefreshTime()){
            msg.setPropertyList(new ArrayList<>());
            msg.setBuyList(new ArrayList<>());//已购买的列表
            //更新缓存
            UserRechargePropertyListDao.getInstance().setCache(msg.getUserId(), msg);
        }
        return msg;
    }

    /**
     * 刷新商城道具
     */
    public static int refreshMallProperty(int userId) {
        int status = ClientCode.SUCCESS.getCode();
        int costAxc = loadRefreshAxc();//扣除的AXC数量
        if(costAxc>0){
            boolean flag = UserCostLogUtil.refreshMallProperty(userId, costAxc);
            if(!flag){
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
            }else{
                //刷新玩家道具列表
                refreshUserPropertyList(userId);
            }
        }else{
            status = ClientCode.SYSTEM_ERROR.getCode();//系统错误
        }
        return status;
    }

    /**
     * 刷新玩家道具列表
     */
    private static void refreshUserPropertyList(int userId) {
        UserRechargePropertyMsg msg = UserRechargePropertyListDao.getInstance().loadMsg(userId);
        msg.setRefreshTime(TimeUtil.getNowTime());//刷新时间
        msg.setPropertyList(loadOnlineList());//道具列表
        msg.setBuyList(new ArrayList<>());//已购买的列表
        UserRechargePropertyListDao.getInstance().setCache(userId, msg);
    }

    /**
     * 获取刷新道具的AXC
     */
    private static int loadRefreshAxc() {
        //查询配置信息
        RechargePropertyConfigEntity entity = RechargePropertyConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getRefreshPrice();
    }

    /**
     * 道具充值
     */
    public static int rechargeProperty(int userId, int commodityId, List<GeneralAwardMsg> retList) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(buyLimit(userId, commodityId)){
            status = ClientCode.BUY_LIMIT.getCode();//购买上限
        }else {
            //查询商品信息
            RechargePropertyMsgEntity entity = RechargePropertyMsgDao.getInstance().loadMsg(commodityId);
            //扣除充值道具消耗
            boolean flag = UserCostLogUtil.costRechargeProperty(userId, entity.getPrice());
            if (flag) {
                //添加订单
                RechargePropertyOrderDao.getInstance().insert(initRechargePropertyOrderEntity(userId, entity));
                //添加奖励
                addRechargePropertyAward(userId, entity, retList);
                //添加玩家道具购买信息
                addUserBuyProperty(userId, entity.getId());
            } else {
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
            }
        }
        return status;
    }

    /**
     * 是否购买上限
     */
    private static boolean buyLimit(int userId, int commodityId) {
        //查询玩家信息
        UserRechargePropertyMsg msg = UserRechargePropertyListDao.getInstance().loadMsg(userId);
        return msg.getBuyList().contains(commodityId);
    }

    /**
     * 添加玩家购买道具信息
     */
    private static void addUserBuyProperty(int userId, int id) {
        //查询玩家信息
        UserRechargePropertyMsg msg = UserRechargePropertyListDao.getInstance().loadMsg(userId);
        //已购买的道具
        List<Integer> buyList = msg.getBuyList();
        if(!buyList.contains(id)){
            buyList.add(id);
            msg.setBuyList(buyList);
            UserRechargePropertyListDao.getInstance().setCache(userId, msg);
        }
    }

    /**
     * 添加充值道具奖励
     */
    private static void addRechargePropertyAward(int userId, RechargePropertyMsgEntity entity,
            List<GeneralAwardMsg> retList) {
        //添加返回信息
        retList.add(AwardUtil.initGeneralAwardMsg(CommodityUtil.property(), PropertyMsgUtil.loadImgUrl(entity.getPropertyType()),
                entity.getNum()));
        //添加奖励信息
        SchedulerSample.delayed(1, new AddRechargePropertyAwardTask(userId, entity));
    }

    /**
     * 填充充值道具订单信息
     */
    private static RechargePropertyOrderEntity initRechargePropertyOrderEntity(int userId,
            RechargePropertyMsgEntity msgEntity) {
        RechargePropertyOrderEntity entity = new RechargePropertyOrderEntity();
        entity.setUserId(userId);//玩家ID
        entity.setPropertyType(msgEntity.getPropertyType());//道具类型
        entity.setPropertyType(msgEntity.getPrice());//价格
        entity.setNum(msgEntity.getNum());//数量
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }
}
