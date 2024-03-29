package avatar.util.thirdpart;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.product.repair.ProductRepairConfigEntity;
import avatar.entity.product.repair.ProductRepairUserOfficalEntity;
import avatar.global.code.basicConfig.ConfigMsg;
import avatar.global.code.thirdpart.OfficialAccountMsg;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.product.repair.ProductRepairConfigDao;
import avatar.module.product.repair.ProductRepairUserOfficalDao;
import avatar.module.product.repair.RepairOfficalAccountTokenDao;
import avatar.util.LogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.HttpClientUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机修公众号工具类
 */
public class OfficialAccountUtil {
    /**
     * 发送公众号信息
     */
    public static void sendOfficalAccount(int productId){
        //查询设备信息
        ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        if(productInfoEntity!=null) {
            String productName = "(" + ProductUtil.loadProductName(productId) + ")";
            //查询店铺绑定的机修信息
            List<ProductRepairUserOfficalEntity> userList = ProductRepairUserOfficalDao.getInstance().loadAll();
            if (userList != null && userList.size() > 0) {
                for (ProductRepairUserOfficalEntity entity : userList) {
                    String openId = entity.getOpenid();
                    String url = loadSendAccountUrl();//发送公众号的跳转链接
                    if(!StrUtil.checkEmpty(url)) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("touser", openId);//接收者微信openId
                        map.put("template_id", "IH5NRdH4ayD30gmVfXYZU-3B7qDikETuYa9RaEQhnx4");//模板ID
                        map.put("url", url);//回调接口
                        Map<String, Object> data = new HashMap<>();//data数据
                        Map<String, Object> firstData = new HashMap<>();//标题数据
                        firstData.put("value", "【"+ ConfigMsg.appName+"】您好，您有一台设备需要维修");
                        firstData.put("color", "#FF4635");
                        data.put("first", firstData);
                        Map<String, Object> deviceData = new HashMap<>();//设备数据
                        deviceData.put("value", "【"+ ConfigMsg.appName+"】"+productName);
                        deviceData.put("color", "#FF4635");
                        data.put("device", deviceData);
                        Map<String, Object> timeData = new HashMap<>();//时间数据
                        timeData.put("value", TimeUtil.getNowTimeStr());
                        timeData.put("color", "#FF4635");
                        data.put("time", timeData);
                        Map<String, Object> remarkData = new HashMap<>();//备注数据
                        remarkData.put("value", "请务必维修完成后点击详情反馈我们哦~");
                        remarkData.put("color", "#FF4635");
                        data.put("remark", remarkData);
                        //数据内容
                        map.put("data", data);
                        String json = JsonUtil.mapToJson(map);
                        //获取accessToken
                        String accessToken = getAccessToken();
                        if (!StrUtil.checkEmpty(accessToken)) {
                            LogUtil.getLogger().info("获取到的公众号accessToken为{}", accessToken);
                            //发送Post请求
                            String str = HttpClientUtil.sendHttpPost(OfficialAccountMsg.officalAccountUrl + accessToken, json);
                            LogUtil.getLogger().info("发送公众号模板消息给机修{}后返回结果为：{}", openId, str);
                            if (!StrUtil.checkEmpty(str)) {
                                Map<String, Object> retMap = JsonUtil.strToMap(str);
                                if (retMap != null && retMap.size() > 0 && retMap.containsKey("errcode")) {
                                    int errcode = Integer.parseInt(retMap.get("errcode").toString());
                                    if (errcode == 40001) {
                                        LogUtil.getLogger().info("公众号的accessToken过期了，重新生成-----");
                                        accessToken = loadNewAcceddToken();
                                        if (!StrUtil.checkEmpty(accessToken)) {
                                            RepairOfficalAccountTokenDao.getInstance().setCache(accessToken);
                                            //重新发送一次
                                            sendOfficalAccount(productId);
                                        }
                                    }
                                }
                            }
                        } else {
                            LogUtil.getLogger().info("发送公众号信息获取accessToken失败--------");
                        }
                    }else{
                        LogUtil.getLogger().info("发送公众号信息的时候获取不到对应发送链接-----------");
                    }
                }
            }
        }else{
            LogUtil.getLogger().info("发送公众号信息获取设备{}信息失败--------", productId);
        }
    }

    /**
     * 获取发送的链接
     */
    private static String loadSendAccountUrl() {
        String url = "";//发送的链接
        //查询设备维护配置信息
        ProductRepairConfigEntity configEntity = ProductRepairConfigDao.getInstance().loadMsg();
        if(configEntity!=null && !StrUtil.checkEmpty(configEntity.getOfficalUrl())) {
            //用后台配置的url
            url = configEntity.getOfficalUrl();
        }
        return url;
    }

    /**
     * 获取公众号accessToken
     */
    private static String getAccessToken(){
        //获取token
        String token = RepairOfficalAccountTokenDao.getInstance().loadToken();
        if(StrUtil.checkEmpty(token)) {
            String url = OfficialAccountMsg.accessTokenUrl + "&appid=" +
                    OfficialAccountMsg.wx_appid + "&secret=" + OfficialAccountMsg.wx_appsecret;
            String retMsg = HttpClientUtil.sendHttpGet(url);
            LogUtil.getLogger().info("获取公众号accessToken返回的token返回码为{}", retMsg);
            if (!StrUtil.checkEmpty(retMsg)) {
                Map<String, Object> map = JsonUtil.strToMap(retMsg);
                if(map==null){
                    LogUtil.getLogger().info("获取公众号的accessToken失败---------");
                    token = "";
                }else {
                    token = map.get("access_token").toString();
                    //设置缓存
                    RepairOfficalAccountTokenDao.getInstance().setCache(token);
                }
            } else {
                token = "";
            }
        }
        return token;
    }

    /**
     * 重新生成公众号token
     */
    private static String loadNewAcceddToken(){
        String token;
        //获取token
        String url = OfficialAccountMsg.accessTokenUrl + "&appid=" + OfficialAccountMsg.wx_appid + "&secret=" + OfficialAccountMsg.wx_appsecret;
        String retMsg = HttpClientUtil.sendHttpGet(url);
        LogUtil.getLogger().info("重新获取公众号accessToken返回的token返回码为{}", retMsg);
        if (!StrUtil.checkEmpty(retMsg)) {
            Map<String, Object> map = JsonUtil.strToMap(retMsg);
            if(map==null){
                LogUtil.getLogger().info("获取公众号的accessToken失败---------");
                token = "";
            }else {
                token = map.get("access_token").toString();
                //设置缓存
                RepairOfficalAccountTokenDao.getInstance().setCache(token);
            }
        } else {
            token = "";
        }
        return token;
    }
}
