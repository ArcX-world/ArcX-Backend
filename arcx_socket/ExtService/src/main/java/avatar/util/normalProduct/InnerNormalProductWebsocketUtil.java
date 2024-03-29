package avatar.util.normalProduct;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.normalProduct.ProductGeneralParamsMsg;
import avatar.data.product.normalProduct.RequestGeneralMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.user.info.UserInfoEntity;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.user.info.UserInfoDao;
import avatar.util.LogUtil;
import avatar.util.basic.general.MediaUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.StrUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内部普通设备websocket工具类
 */
public class InnerNormalProductWebsocketUtil {
    /**
     * 填充请求通用信息
     */
    public static RequestGeneralMsg initRequestGeneralMsg(int userId, int productId) {
        RequestGeneralMsg msg = new RequestGeneralMsg();
        msg.setServerSideType(CrossServerMsgUtil.arcxServer());//服务端类型：潮玩街机
        msg.setAlias(ProductUtil.loadProductAlias(productId));//设备号
        if(userId>0) {
            //查询玩家信息
            UserInfoEntity userInfoEntity = UserInfoDao.getInstance().loadByUserId(userId);
            msg.setUserId(userId);//玩家ID
            msg.setUserName(userInfoEntity == null ? "" : userInfoEntity.getNickName());//玩家昵称
            msg.setImgUrl(userInfoEntity == null ? "" : MediaUtil.getMediaUrl(userInfoEntity.getImgUrl()));//玩家头像
        }else{
            msg.setUserId(0);//玩家ID
            msg.setUserName("");//玩家昵称
            msg.setImgUrl("");//玩家头像
        }
        return msg;
    }

    /**
     * 填充请求通用信息
     */
    private static RequestGeneralMsg initRequestGeneralMsg(ProductGamingUserMsg msg) {
        RequestGeneralMsg requestGeneralMsg = new RequestGeneralMsg();
        requestGeneralMsg.setServerSideType(msg.getServerSideType());//服务端信息
        requestGeneralMsg.setUserId(msg.getUserId());//玩家ID
        requestGeneralMsg.setUserName(msg.getUserName());//玩家昵称
        requestGeneralMsg.setImgUrl(msg.getImgUrl());//头像
        requestGeneralMsg.setAlias(ProductUtil.loadProductAlias(msg.getProductId()));//设备号
        return requestGeneralMsg;
    }

    /**
     * 发送操作信息
     */
    public static int sendOperateMsg(int cmd, String host, int port, Map<String, Object> map) {
        int status = ClientCode.SUCCESS.getCode();//成功
        //链接设备的socket
        int hostId = InnerNormalProductConnectUtil.loadUid(host, port);
        String linkMsg = host+port+hostId;//连接信息（构成key的参数）
        //链接设备的socket
        InnerNormalProductClient client = InnerNormalProductOperateUtil.loadClient(host, port, linkMsg);
        if(client.isOpen()) {
            if (hostId > 0) {
                SendNormalProductInnerMsgUtil.sendClientMsg(client, cmd, hostId, map);
            }else{
                LogUtil.getLogger().info("链接普通设备服务器的时候，填充本机的hostId失败----------");
                status = ClientCode.PRODUCT_EXCEPTION.getCode();//设备异常
            }
        }else{
            LogUtil.getLogger().info("普通设备服务器{}，端口{}尚未连接-----------", host, port);
            status = ClientCode.PRODUCT_EXCEPTION.getCode();//设备异常
        }
        return status;
    }

    /**
     * 发送设备中心信息
     */
    public static void sendMsg(int cmd, String host, int port, RequestGeneralMsg requestGeneralMsg) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("requestGeneralMsg", requestGeneralMsg);
        sendOperateMsg(cmd, host, port, dataMap);
    }

    /**
     * 填充操作的对象信息
     */
    public static Map<String, Object> initOperateMap(int productId, int userId, List<String> operateList) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestGeneralMsg", initRequestGeneralMsg(userId, productId));//请求通用信息
        map.put("operateStruct", initOperateStruct(productId, operateList));//操作指令
        return map;
    }

    /**
     * 填充操作指令
     */
    private static Map<String, Object> initOperateStruct(int productId, List<String> operateList) {
        //查询设备信息
        ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        int productType = productInfoEntity.getProductType();//设备分类
        int secondType = productInfoEntity.getSecondType();//设备二级分类
        Map<String, Object> retMap = new HashMap<>();
        if(operateList!=null && operateList.size()>0){
            operateList.forEach(operateStr->{
                String operate = operateStr;//默认就是操作，街机的投币指令为：operate_time
                if(operateStr.contains("_")){
                    //街机投币指令
                    operate = operateStr.split("_")[0];
                }
                if(!retMap.containsKey(operate)){
                    //查询指令信息
                    String instruct = ProductUtil.loadInstruct(productType, secondType, operateStr);
                    if(!StrUtil.checkEmpty(instruct)){
                        retMap.put(operate, instruct);
                    }
                }
            });
        }
        return retMap;
    }

    /**
     * 填充设备操作的对象信息
     */
    public static Map<String, Object> initProductOperateMap(int productId, int userId, List<String> operateList) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestGeneralMsg", initRequestGeneralMsg(userId, productId));//请求通用信息
        map.put("operateStruct", initOperateStruct(productId, operateList));//操作指令
        return map;
    }

    /**
     * 填充获得币的对象信息
     */
    public static Map<String, Object> initGetCoinOperateMap(int productId, int userId, List<String> operateList) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestGeneralMsg", initRequestGeneralMsg(userId, productId));//请求通用信息
        map.put("operateStruct", initOperateStruct(productId, operateList));//操作指令
        map.put("productGeneralParams", initProductGeneralParams(productId, 0));//设备信息通用参数
        return map;
    }

    /**
     * 设备通用信息参数
     */
    private static ProductGeneralParamsMsg initProductGeneralParams(int productId, int operateState) {
        ProductGeneralParamsMsg retMsg = new ProductGeneralParamsMsg();
        //查询设备房间信息
        ProductRoomMsg msg = ProductRoomDao.getInstance().loadByProductId(productId);
        retMsg.setGameTime(ProductUtil.startGameTime(productId));//游戏次数
        retMsg.setOnProductTime(msg.getOnProductTime());//玩家上机时间
        retMsg.setPushCoinOnTime(msg.getPushCoinOnTime());//最近一次刷新时间
        retMsg.setOperateState(operateState);//设备操作
        return retMsg;
    }

    /**
     * 填充只结算的对象信息
     */
    public static Map<String, Object> initSettlementOperateMap(int productId, int userId, int operateState,
            List<String> operateList) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestGeneralMsg", initRequestGeneralMsg(userId, productId));//请求通用信息
        map.put("operateStruct", initOperateStruct(productId, operateList));//操作指令
        map.put("productGeneralParams", initProductGeneralParams(productId, operateState));//设备信息通用参数
        return map;
    }

    /**
     * 填充下爪的对象信息
     */
    public static Map<String, Object> initDownCatchOperateMap(int productId, int userId, List<String> operateList) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestGeneralMsg", initRequestGeneralMsg(userId, productId));//请求通用信息
        map.put("operateStruct", initOperateStruct(productId, operateList));//操作指令
        map.put("productGeneralParams", initProductGeneralParams(productId, 0));//设备信息通用参数
        return map;
    }

    /**
     * 填充设备信息操作的对象信息
     */
    public static Map<String, Object> initProductMsgOperateMap(ProductGamingUserMsg msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestGeneralMsg", initRequestGeneralMsg(msg));//请求通用信息
        return map;
    }

}
