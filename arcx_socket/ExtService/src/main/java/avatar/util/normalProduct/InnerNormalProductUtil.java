package avatar.util.normalProduct;

import avatar.data.product.general.ResponseGeneralMsg;
import avatar.data.product.normalProduct.InnerProductJsonMapMsg;
import avatar.data.product.normalProduct.ProductGeneralParamsMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.basic.errrorCode.NormalProductClientCode;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.module.product.info.ProductAliasDao;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.innoMsg.InnerEnCodeUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 普通设备服务端内部工具类
 */
public class InnerNormalProductUtil {
    /**
     * 接收信息处理
     */
    public static void dealMsg(String jsonStr) {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            //先加密校验
            if(checkEncode(jsonStr)) {
                InnerProductJsonMapMsg jsonMapMsg = dealJsonMsg(jsonStr);
                if (jsonMapMsg != null) {
                    //返回信息处理
                    InnerNormalProductCmdUtil.dealCmdMsg(jsonMapMsg);
                } else {
                    LogUtil.getLogger().info("收到普通设备服务器的信息：{}，格式解析错误--------", jsonStr);
                }
            }
        });
        cachedPool.shutdown();
    }

    /**
     * 处理json信息
     */
    private static InnerProductJsonMapMsg dealJsonMsg(String jsonStr) {
        InnerProductJsonMapMsg msg = new InnerProductJsonMapMsg();
        try{
            if(!StrUtil.checkEmpty(jsonStr)) {
                Map<String, Object> jsonMap = JsonUtil.strToMap(jsonStr);
                if(jsonMap!=null && jsonMap.size()>0) {
                    //内部操作协议号
                    int cmd = (int) jsonMap.get("cmd");
                    msg.setCmd(cmd);
                    //当前通信的服务端ID
                    int hostId = (int) jsonMap.get("userId");
                    msg.setHostId(hostId);
                    Map<String, Object> paramMap = (Map<String, Object>) jsonMap.get("param");//参数内容
                    //具体的内容参数
                    Map<String, Object> dataMap = (Map<String, Object>) paramMap.get("data");
                    msg.setDataMap(dataMap);
                    //操作结果
                    int status = (int) paramMap.get("status");
                    msg.setStatus(status);
                    //服务器时间
                    String time = paramMap.get("time").toString();
                    msg.setTime(time);
                    //获取返回通用信息
                    ResponseGeneralMsg responseGeneralMsg = initResponseGeneralMsg(dataMap);//返回通用信息
                    String alias = responseGeneralMsg.getAlias();//设备号
                    int productId = ProductAliasDao.getInstance().loadByAlias(alias);//设备ID
                    msg.setProductId(productId);//设备ID
                    msg.setUserId(responseGeneralMsg.getUserId());//玩家ID
                    msg.setResponseGeneralMsg(responseGeneralMsg);//返回通用信息
                    //设备信息通用参数
                    if(dataMap.containsKey("productGeneralParams")){
                        msg.setProductGeneralParamsMsg(initProductGeneralParamsMsg(dataMap));
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            return null;
        }
        return msg;
    }

    /**
     * 返回通用信息
     */
    public static ResponseGeneralMsg initResponseGeneralMsg(Map<String, Object> dataMap) {
        ResponseGeneralMsg msg = new ResponseGeneralMsg();
        if(dataMap.containsKey("responseGeneralMsg")) {
            Map<String, Object> responseGeneralMap = (Map<String, Object>) dataMap.get("responseGeneralMsg");
            msg.setServerSideType((int) responseGeneralMap.get("serverSideType"));//服务端类型
            msg.setAlias(responseGeneralMap.get("alias").toString());//设备号
            msg.setUserId((int) responseGeneralMap.get("userId"));//玩家ID
            msg.setUserName(responseGeneralMap.get("userName").toString());//玩家昵称
            msg.setImgUrl(responseGeneralMap.get("imgUrl").toString());//玩家头像
        }
        return msg;
    }

    /**
     * 填充设备通用参数
     */
    public static ProductGeneralParamsMsg initProductGeneralParamsMsg(Map<String, Object> dataMap) {
        ProductGeneralParamsMsg msg = new ProductGeneralParamsMsg();
        Map<String, Object> productGeneralMap = (Map<String, Object>) dataMap.get("productGeneralParams");
        msg.setGameTime((int)productGeneralMap.get("gameTime"));//玩家游戏次数
        msg.setOnProductTime(Long.parseLong(productGeneralMap.get("onProductTime").toString()));//玩家上机时间
        msg.setPushCoinOnTime(Long.parseLong(productGeneralMap.get("pushCoinOnTime").toString()));//最近一次刷新时间
        msg.setOperateState(productGeneralMap.containsKey("operateState")?
                (int)productGeneralMap.get("operateState"):0);//设备操作
        return msg;
    }

    /**
     * 校验加密
     */
    private static boolean checkEncode(String jsonStr) {
        boolean flag = false;
        try{
            if(!StrUtil.checkEmpty(jsonStr)) {
                Map<String, Object> jsonMap = JsonUtil.strToMap(jsonStr);
                if (jsonMap != null && jsonMap.size() > 0) {
                    Map<String, Object> paramMap = (Map<String, Object>) jsonMap.get("param");//参数内容
                    //具体的内容参数
                    Map<String, Object> dataMap = (Map<String, Object>) paramMap.get("data");
                    flag = InnerEnCodeUtil.checkEncode(dataMap);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return flag;
    }

    /**
     * 发送心跳
     */
    public static void heart(InnerNormalProductClient client) {
        int hostId = InnerNormalProductConnectUtil.loadUid(client.getURI().getHost(), client.getURI().getPort());
        if(hostId>0) {
            SendNormalProductInnerMsgUtil.sendClientMsg(client, WebsocketInnerCmd.C2S_HEART, hostId, new HashMap<>());
        }
    }

    /**
     * 将普通设备的错误码转换成系统的错误码
     */
    public static int loadClientCode(int status) {
        if(status==NormalProductClientCode.PRODUCT_OCCUPY.getCode()){
            //设备占用中
            status = ClientCode.PRODUCT_OCCUPY.getCode();
        }else if(status== NormalProductClientCode.PRODUCT_GAMING_USER_NOT_FIT.getCode()){
            //你已离开设备
            status = ClientCode.PRODUCT_GAMING_USER_NOT_FIT.getCode();
        }else if(status==NormalProductClientCode.PRODUCT_EXCEPTION.getCode()){
            //设备异常
            status = ClientCode.PRODUCT_EXCEPTION.getCode();
        }else if(status==NormalProductClientCode.TWICE_OPERATE.getCode()){
            //重复操作
            status = ClientCode.TWICE_OPERATE.getCode();//重复操作
        }
        return status;
    }
}
