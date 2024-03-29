package avatar.service.product;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.data.product.info.ProductMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.product.repair.ProductRepairEntity;
import avatar.global.basicConfig.basic.ProductConfigMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.product.info.ProductSecondTypeFoldingListDao;
import avatar.module.product.info.ProductTypeFordingListDao;
import avatar.module.product.repair.ProductRepairDao;
import avatar.net.session.Session;
import avatar.task.product.RepairProductDealTask;
import avatar.util.basic.ImgUtil;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.ProductCheckParamsUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ListUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserOnlineUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备接口实现类
 */
public class ProductService {
    /**
     * 设备信息
     */
    public static void productMsg(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = ProductCheckParamsUtil.checkProductStatus(map);
        if (ParamsUtil.isSuccess(status)) {
            int productId = ParamsUtil.productId(map);//设备ID
            int userId = ParamsUtil.userId(map);//玩家ID
            String versionCode = ParamsUtil.versionCode(map);//版本号
            //查询设备信息
            ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
            dataMap.put("devId", productId);//设备ID
            dataMap.put("devNm", entity.getProductName());//设备名称
            //设备类型
            int productType = entity.getProductType();//设备类型
            dataMap.put("devTp", productType);//设备类型
            dataMap.put("sndTp", entity.getSecondType());//设备二级分类
            dataMap.put("devIdx", ProductUtil.productIndex(productId, entity.getLiveUrl()));//设备位置
            dataMap.put("devPct", ImgUtil.loadProductImg(entity.getImgProductId()));//图标路径
            dataMap.put("lvTp", entity.getLiveType());//播流类型
            dataMap.put("lvAds", entity.getLiveUrl());//视频流路径
            dataMap.put("wbLvAds", StrUtil.checkEmpty(entity.getWebLiveUrl())?entity.getLiveUrl():entity.getWebLiveUrl());//web播流地址
            dataMap.put("cmdTp", entity.getCostCommodityType());//消耗商品类型
            dataMap.put("csAmt", entity.getCost());//游戏价格
            dataMap.put("devSts", ProductUtil.loadGamingUser(productId).getUserId()>0?
                    YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());//设备占用状态
            dataMap.put("devDtPct", ImgUtil.loadProductDetailImg(entity.getImgProductDetailId()));//设备详情URL
            ProductGamingUserMsg gamingUserMsg = ProductUtil.loadGamingUser(productId);//游戏中玩家ID
            //游戏玩家信息
            if(gamingUserMsg.getUserId()>0) {
                dataMap.put("gmPly", CrossServerMsgUtil.initConciseServerUserMsg(gamingUserMsg));//游戏中玩家信息
            }
            //投币倍率信息
            ProductUtil.coinMultiMsg(userId, productId, versionCode, dataMap);
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 设备列表
     */
    public static void productList(Map<String, Object> map, Session session) {
        List<ProductMsg> retList = new ArrayList<>();
        int status = ProductCheckParamsUtil.productList(map);
        if(ParamsUtil.isSuccess(status)) {
            int productType = ParamsUtil.intParmasNotNull(map, "devTp");//设备类型
            //查询设备分类列表列表
            List<Integer> productList = ProductTypeFordingListDao.getInstance().loadList(productType);
            if(productList.size()>0) {
                //获取分页信息
                List<Integer> loadList = ListUtil.getPageList(ParamsUtil.pageNum(map),
                        ParamsUtil.pageSize(map), productList);
                loadList.forEach(pId -> retList.add(ProductUtil.ProductMsg(pId)));
            }
        }
        //传输的jsonMap，先填充list
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("serverTbln", retList);
        //推送结果
        SendMsgUtil.sendBySessionAndList(session, status, jsonMap);
    }

    /**
     * 快速加入的设备
     */
    public static void fastJoinProduct(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = ProductCheckParamsUtil.fastJoinProduct(map);
        if (ParamsUtil.isSuccess(status)) {
            int productType = ParamsUtil.intParmasNotNull(map, "devTp");//设备类型
            ProductMsg productMsg = null;
            int retProductId = ProductUtil.loadFastJoinProduct(productType, dataMap);//返回的设备ID
            //返回的设备信息
            if (retProductId > 0) {
                productMsg = ProductUtil.ProductMsg(retProductId);
            }
            dataMap.put("devIfo", productMsg);
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 游戏中的设备
     */
    public static void gamingProduct(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = CheckParamsUtil.checkAccessToken(map);
        if (ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            dataMap.put("devId", UserOnlineUtil.loadGamingProduct(userId));//设备ID
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 设备报修
     */
    public static void repairProduct(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = ProductCheckParamsUtil.repairProduct(map);
        int productId = 0;//设备ID
        int userId = 0;//玩家ID
        if(ParamsUtil.isSuccess(status)){
            userId = ParamsUtil.userId(map);//玩家ID
            productId = ParamsUtil.productId(map);//设备ID
            //查询维修信息
            ProductRepairEntity entity = ProductRepairDao.getInstance().loadByProductId(productId);
            if(entity!=null && (TimeUtil.getNowTime()-TimeUtil.strToLong(entity.getCreateTime()))
                    < ProductConfigMsg.productRepairIntervalTime*1000){
                status = ClientCode.REPAIR_MSG_EXIST.getCode();//报修信息已经存在
            }else{
                //添加报修数据
                boolean flag = ProductRepairDao.getInstance().insert(ProductUtil.
                        initProductRepairEntity(userId, productId, 0));
                if(!flag){
                    status = ClientCode.FAIL.getCode();//失败
                }
            }
            //添加操作日志
            if(ParamsUtil.isSuccess(status)){
                UserOperateLogUtil.repairProduct(userId, productId);
            }
        }
        //推送结果
        if(ParamsUtil.isSuccess(status)){
            //报修单独返回
            SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS_REPAIR.getCode(), dataMap);
            //添加后续处理定时器
            SchedulerSample.delayed(10, new RepairProductDealTask(productId, userId));
        }else {
            SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
        }
    }
}
