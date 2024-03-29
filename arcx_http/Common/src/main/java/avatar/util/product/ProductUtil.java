package avatar.util.product;

import avatar.data.crossServer.ConciseServerUserMsg;
import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.data.product.info.ProductMsg;
import avatar.data.product.innoMsg.ProductCoinMultiMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.product.innoMsg.InnoPushCoinWindowMsgEntity;
import avatar.entity.product.productType.ProductSecondLevelTypeEntity;
import avatar.entity.product.repair.ProductRepairEntity;
import avatar.global.enumMsg.product.ProductTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.product.gaming.ProductGamingUserMsgDao;
import avatar.module.product.info.ProductGroupListDao;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.product.info.ProductTypeFordingListDao;
import avatar.module.product.innoMsg.InnoPushCoinMultiDao;
import avatar.module.product.innoMsg.InnoPushCoinWindowDao;
import avatar.module.product.productType.ProductSecondLevelTypeDao;
import avatar.util.basic.ImgUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.system.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 设备工具类
 */
public class ProductUtil {
    /**
     * 设备位置
     */
    public static int productIndex(int productId, String liveUrl) {
        int index = 1;//设备位置
        List<Integer> productIdList = ProductGroupListDao.getInstance().loadByLiveUrl(liveUrl);
        if(productIdList.size()>0){
            for(int i=0;i<productIdList.size();i++){
                if(productIdList.get(i)==productId){
                    index = i+1;
                    break;
                }
            }
        }
        return index;
    }

    /**
     * 获取设备在玩玩家
     */
    public static ProductGamingUserMsg loadGamingUser(int productId){
        //查询游戏玩家信息
        return ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
    }

    /**
     * 初始化设备游戏中玩家信息
     */
    public static ProductGamingUserMsg initProductGamingUserMsg(int productId) {
        ProductGamingUserMsg msg = new ProductGamingUserMsg();
        msg.setServerSideType(0);//服务端类型
        msg.setProductId(productId);//设备ID
        msg.setUserId(0);//玩家ID
        msg.setUserName("");//玩家昵称
        msg.setImgUrl("");//玩家头像
        return msg;
    }

    /**
     * 填充设备投币倍率信息
     */
    public static void coinMultiMsg(int userId, int productId, String versionCode, Map<String, Object> dataMap) {
        if (isInnoProduct(productId)) {
            int secondType = loadSecondType(productId);//设备二级分类
            //查询窗口信息
            InnoPushCoinWindowMsgEntity windowMsgEntity = InnoPushCoinWindowDao.getInstance().
                    loadBySecondType(secondType);
            if (windowMsgEntity != null) {
                ProductCoinMultiMsg msg = new ProductCoinMultiMsg();
                msg.setWdwPct(MediaUtil.getMediaUrl(windowMsgEntity.getImgUrl()));//窗口图
                List<Integer> multiList = InnoPushCoinMultiDao.getInstance().loadBySecondType(secondType);//倍率列表
                boolean unlockFlag = InnoProductUtil.isUnlockVersion(versionCode);//是否不锁定限制
                msg.setMulTbln(InnoProductUtil.productMultiLimitList(userId, productId, secondType, multiList, unlockFlag));//倍率信息
                dataMap.put("ptyMul", msg);//投币倍率信息
            }
        }
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
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        return entity==null?0:entity.getSecondType();
    }

    /**
     * 填充设备信息
     */
    public static ProductMsg ProductMsg(int productId) {
        ProductMsg msg = new ProductMsg();
        msg.setDevId(productId);//设备ID
        msg.setDevNm(loadProductName(productId));//设备名称
        //查询设备信息
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        msg.setDevPct(ImgUtil.loadProductImg(entity.getImgProductId()));//设备图片
        msg.setCsAmt(entity.getCost());//游戏价格
        //设备在玩玩家信息
        List<ConciseServerUserMsg> userImgUrlList = new ArrayList<>();
        //获取当前设备在线信息
        //查询游戏玩家信息
        ProductGamingUserMsg gamingUserMsg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
        if(gamingUserMsg!=null && gamingUserMsg.getUserId()>0) {
            userImgUrlList.add(CrossServerMsgUtil.initConciseServerUserMsg(gamingUserMsg));
        }
        if(userImgUrlList.size()>0){
            msg.setPlyTbln(userImgUrlList);
        }
        //设备组
        msg.setDevTbln(ProductGroupListDao.getInstance().loadByLiveUrl(entity.getLiveUrl()));
        return msg;
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
     * 获取快速进入的设备（根据设备类型）
     */
    public static int loadFastJoinProduct(int productType, Map<String, Object> dataMap) {
        int retProductId = 0;
        List<Integer> productIdList = ProductTypeFordingListDao.getInstance().loadList(productType);
        if(productIdList.size()>0){
            retProductId = loadNobodyProduct(productIdList, dataMap);
        }
        return retProductId;
    }

    /**
     * 获取无人玩的设备
     */
    private static int loadNobodyProduct(List<Integer> productIdList,  Map<String, Object> dataMap){
        int retProductId = 0;
        Collections.shuffle(productIdList);
        int oriRetPId = 0;//初始返回的设备ID
        //查询一个无人玩的设备返回
        for(int productId : productIdList){
            ProductGamingUserMsg gamingUserMsg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
            if(gamingUserMsg.getUserId()==0){
                retProductId = productId;
                oriRetPId = productId;
                break;
            }
        }
        if(retProductId==0){
            retProductId = productIdList.get(0);
        }
        if(dataMap!=null){
            dataMap.put("fulFlg", oriRetPId==0?YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode());//是否满员：是
        }
        return retProductId;
    }

    /**
     * 获取设备信息日志
     */
    public static String productLog(int productId){
        return productId+"-"+getProductTypeName(productId)+"-"+
                loadProductName(productId);
    }

    /**
     * 获取设备类型名称
     */
    private static String getProductTypeName(int productId) {
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
     * 填充报修设备实体信息
     */
    public static ProductRepairEntity initProductRepairEntity(int userId, int productId, int breakType) {
        ProductRepairEntity entity = new ProductRepairEntity();
        entity.setUserId(userId);//玩家ID
        entity.setProductId(productId);//设备ID
        entity.setBreakType(breakType);//故障类型
        entity.setStatus(YesOrNoEnum.NO.getCode());//是否已维护：否
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime("");//更新时间
        return entity;
    }

}
