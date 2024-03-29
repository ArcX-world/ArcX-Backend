package avatar.util.system;

import avatar.global.enumMsg.basic.system.MobilePlatformTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.user.token.UserAccessTokenDao;
import avatar.util.basic.CheckUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 参数工具类
 */
public class ParamsUtil {

    /**
     * 非空int类型参数
     */
    public static int intParmasNotNull(Map<String, Object> map, String paramsName) {
        return Integer.parseInt(map.get(paramsName).toString());
    }

    /**
     * 可能为空int类型状态参数
     */
    public static int intStatusParmas(Map<String, Object> map, String paramsName) {
        return map.get(paramsName)==null?-1:Integer.parseInt(map.get(paramsName).toString());
    }

    /**
     * 可能为空int类型参数
     */
    public static int intParmas(Map<String, Object> map, String paramsName) {
        return map.get(paramsName)==null?0:Integer.parseInt(map.get(paramsName).toString());
    }

    /**
     * 非空double类型参数
     */
    public static double doubleParmasNotNull(Map<String, Object> map, String paramsName) {
        return Double.parseDouble(map.get(paramsName).toString());
    }

    /**
     * 非空String参数
     */
    public static String stringParmasNotNull(Map<String, Object> map, String paramsName) {
        return map.get(paramsName).toString().trim();
    }

    /**
     * 非空long类型参数
     */
    public static long longParmasNotNull(Map<String, Object> map, String paramsName) {
        return Long.parseLong(map.get(paramsName).toString());
    }

    /**
     * 非空String不替换参数
     */
    public static String stringParmasNotReplace(Map<String, Object> map, String paramsName) {
        return map.get(paramsName).toString();
    }

    /**
     * 可能为空String参数
     */
    public static String stringParmas(Map<String, Object> map, String paramsName) {
        return map.get(paramsName)==null?"":map.get(paramsName).toString();
    }

    /**
     * 获取object类型参数
     */
    public static Object objectParmas(Map<String, Object> map, String paramsName) {
        return map.get(paramsName);
    }

    /**
     * 拼接后台数据信息
     */
    public static String operateMsg(String name, Object desc) {
        return "【"+name+"】"+desc;
    }

    /**
     * 是否成功结果
     */
    public static boolean isSuccess(int status) {
        return status == ClientCode.SUCCESS.getCode();
    }

    /**
     * id列表参数
     */
    public static List<Integer> idList(Map<String, Object> map) {
        List<Integer> idList = new ArrayList<>();
        Object object = map.get("idList");
        if(object!=null){
            String objStr = object.toString();
            idList = JsonUtil.strToStrIntegerList(objStr);
        }
        return idList;
    }

    /**
     * IP参数
     */
    public static String ip(Map<String, Object> map) {
        String ip = ParamsUtil.stringParmas(map, "ip");
        if(!StrUtil.checkEmpty(ip) && ip.contains(",")){
            List<String> ipList = StrUtil.strToStrList(ip, ",");
            ip = ipList.get(0);
        }
        return ip;
    }

    /**
     * 调用凭证
     */
    public static String accessToken(Map<String, Object> map) {
        return stringParmas(map, "aesTkn");
    }

    /**
     * mac ID
     */
    public static String macId(Map<String, Object> map) {
        return stringParmas(map, "mcCd");
    }

    /**
     * 设备ID
     */
    public static int productId(Map<String, Object> map) {
        return intParmas(map, "devId");
    }

    /**
     * 登录方式
     */
    public static int loginWayType(Map<String, Object> map) {
        return intParmas(map, "lgWt");
    }

    /**
     * 手机平台类型
     */
    public static int loadMobilePlatform(Map<String, Object> map) {
        String platform = loadPlatform(map);
        if(platform.equals("ios")){
            //苹果
            return MobilePlatformTypeEnum.APPLE.getCode();
        }else if(platform.equals("android")){
            //安卓
            return MobilePlatformTypeEnum.ANDROID.getCode();
        }else{
            //web
            return MobilePlatformTypeEnum.WEB.getCode();
        }
    }

    /**
     * 平台类型
     */
    public static String loadPlatform(Map<String, Object> map){
        return ParamsUtil.stringParmas(map, "plm");
    }

    /**
     * 版本号
     */
    public static String versionCode(Map<String, Object> map) {
        return stringParmas(map, "vsCd");
    }

    /**
     * 玩家ID
     */
    public static int userId(Map<String, Object> map) {
        int arcxUid = 0;//玩家ID
        String accessToken = accessToken(map);//调用凭证
        if(!StrUtil.checkEmpty(accessToken)){
            //查询信息
            arcxUid = UserAccessTokenDao.getInstance().loadByToken(accessToken);
        }
        return arcxUid;
    }

    /**
     * 拼接http请求
     */
    public static String httpRequestDomain(String domainName, Map<String, Object> paramsMap){
        StringBuilder retStr = new StringBuilder();
        retStr.append(domainName);//请求域名
        if(paramsMap.keySet().size()>0){
            retStr.append("?");
            List<String> keyList = new ArrayList<>(paramsMap.keySet());
            for(int i=0;i<keyList.size();i++){
                if(i>0){
                    retStr.append("&");
                }
                String paramsName = keyList.get(i);
                retStr.append(paramsName);//参数名称
                retStr.append("=");
                retStr.append(paramsMap.get(paramsName));//参数值
            }
        }
        return retStr.toString();
    }

    /**
     * 页码
     */
    public static int pageNum(Map<String, Object> map){
        return ParamsUtil.intParmasNotNull(map, "pgNm");
    }

    /**
     * 一页的数量
     */
    public static int pageSize(Map<String, Object> map){
        return ParamsUtil.intParmasNotNull(map, "pgAmt");
    }

    /**
     * 拼接http请求
     */
    public static String httpRequestProduct(String route, Map<String, Object> paramsMap){
        StringBuilder retStr = new StringBuilder();
        String name = CheckUtil.loadDomainName();
        if(!StrUtil.checkEmpty(name)) {
            retStr.append(name);//域名
            retStr.append(route);//路由
            if(paramsMap.keySet().size()>0){
                retStr.append("?");
                List<String> keyList = new ArrayList<>(paramsMap.keySet());
                for(int i=0;i<keyList.size();i++){
                    if(i>0){
                        retStr.append("&");
                    }
                    String paramsName = keyList.get(i);
                    retStr.append(paramsName);//参数名称
                    retStr.append("=");
                    retStr.append(paramsMap.get(paramsName));//参数值
                }
            }
        }
        return retStr.toString();
    }

    /**
     * 是否标志
     */
    public static boolean isConfirm(int flag){
        return flag== YesOrNoEnum.YES.getCode();
    }

    /**
     * NFT编号
     */
    public static String nftCode(Map<String, Object> map){
        return ParamsUtil.stringParmasNotNull(map, "nftCd");
    }

}
