package avatar.util.system;

import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.util.basic.system.CheckUtil;

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
        return map.get(paramsName).toString();
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
     * 是否验签失败
     */
    public static boolean isVerifyError(int status) {
        return status == ClientCode.VERIFY_SIGN_ERROR.getCode();
    }

    /**
     * 是否标志
     */
    public static boolean isConfirm(int flag){
        return flag== YesOrNoEnum.YES.getCode();
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

}
