package avatar.util.system;

import avatar.util.checkParams.ErrorDealUtil;
import com.alibaba.fastjson.JSONArray;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 字符串工具类
 */
public class StrUtil {

    private static final char[] ary = {'0','1','2','3','4','5','6','7','8','9',
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    /**
     * 字符串是否为空
     * @param str
     * @return
     */
    public static boolean checkEmpty(String str){
        if(str!=null){
            return str.trim()==null || "".equals(str.trim());
        }else{
            return true;
        }
    }

    /**
     * 获取文件名
     * @param str
     * @return
     */
    public static String getFileName(String str){
        return str.substring(str.lastIndexOf("/")+1,str.length());
    }

    /**
     * 生成随机订单号
     * @return
     */
    public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    /**
     * list转换成str，用","拼接
     * @param list
     * @return
     */
    public static String listToStr(List<Integer> list) {
        String str = "";
        for(int giftId : list){
            if(str!=null && !"".equals(str)){
                str += ",";
            }
            str += giftId;
        }
        return str;
    }

    /**
     * 字符串转换成list
     * @param str
     * @return
     */
    public static List<Integer> strToList(String str) {
        List<Integer> list = new ArrayList<>();
        str.split(",");
        for(String st : str.split(",")){
            list.add(Integer.parseInt(st));
        }
        return list;
    }

    /**
     * 获取小写随机字母
     * @return
     */
    public static String getLowerRandomLetter(){
        String chars = "abcdefghijklmnopqrstuvwxyz";
        String str = chars.charAt((int)(Math.random() * 26))+"";
        return str;
    }

    /**
     * 获取大写随机字母
     * @return
     */
    public static String getUpperRandomLetter(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String str = chars.charAt((int)(Math.random() * 26))+"";
        return str;
    }


    /**
     * 十六进制转10进制数字
     * @param str
     * @return
     */
    public static int sixteenParseTen(String str){
        int num = 0;
        try {
            num = Integer.parseInt(str, 16);
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return num;
    }

    /**
     * 字符串转换成Map,字符串格式为xxx?timeStamp=1010&code=xxxx
     * @param uri
     * @return
     */
    public static Map<String, Object> strToMap(String uri){
        String str = uri.substring(uri.lastIndexOf("?")+1);
        Map<String, Object> map = new HashMap<>();
        if(str!=null && !"".equals(str)){
            String[] strs = str.split("&");
            for(String s : strs){
                if(s.contains("=")){
                    String[] keyStrs = s.split("=");
                    map.put(keyStrs[0], keyStrs[1]);
                }
            }
        }
        return map;
    }

    /**
     * 判断是否纯数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        return (Pattern.compile("[0-9]*")).matcher(str).matches();
    }

    /**
     * 去重
     * @return
     */
    public static List<Integer> distinct(List<Integer> list){
        if(list!=null && list.size()>0){
            return list.stream().distinct().collect(Collectors.toList());
        }else{
            return new ArrayList<>();
        }
    }

    /**
     * list转换成字符串
     * @param nameList
     * @return
     */
    public static String strlistToStr(List<String> nameList) {
        String str = "";
        for(String name : nameList){
            if(str!=null && !"".equals(str)){
                str += ",";
            }
            str += name;
        }
        return str;
    }

    /**
     * 根据IP获取区域信息
     * @param ip
     * @return
     */
    public static String loadAddressByIp(String ip) {
        String address = "";
        if(ip.contains(":")){
            ip = ip.substring(0, ip.lastIndexOf(":"));
        }
        String url_str = "http://opendata.baidu.com/api.php?query="+ip+"&resource_id=6006&format=json";
        String result = HttpClientUtil.sendHttpGet(url_str);
        if(!checkEmpty(result)){
            Map<String,Object> map = JsonUtil.strToMap(result);
            JSONArray dataArr = (JSONArray) map.get("data");
            if(dataArr!=null && dataArr.size()>0) {
                String dataStr = dataArr.getString(0);
                if(!checkEmpty(dataStr)){
                    Map<String,Object> dataMap = JsonUtil.strToMap(dataStr);
                    if(dataMap!=null && dataMap.size()>0){
                        address = dataMap.get("location")==null?"":dataMap.get("location").toString();
                    }
                }
            }
        }else{

        }
        return address;
    }


    /**
     * 生成cdkey
     * @return
     */
    public static String createCdKey(String channelKey, List<String> addList, List<String> poolList){
        Random random = new Random();
        int num = random.nextInt(10000);
        String str = ("wd"+channelKey+num+UUID.randomUUID()).toUpperCase();
        str = str.replaceAll("-","");
        str = str.substring(0,18);
        if(addList.contains(str) || poolList.contains(str)) {
            createCdKey(channelKey, addList, poolList);
        }
        return str;
    }


    /**
     * 获取所有数字
     * @return
     */
    public static int getAllNum(String str){
        String resultStr = "";
        str=str.trim();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                resultStr += str.charAt(i);
            }
        }
        if(!StrUtil.checkEmpty(resultStr) &&isNumber(resultStr)){
            return Integer.parseInt(resultStr);
        }else {
            return 0;
        }
    }

    /**
     * 处理识别的数字字符串
     * @param valCode
     * @return
     */
    public static String dealRecogStr(String valCode) {
        valCode = valCode.replaceAll("①", "1");
        valCode = valCode.replaceAll("②", "2");
        valCode = valCode.replaceAll("③", "3");
        valCode = valCode.replaceAll("④", "4");
        valCode = valCode.replaceAll("⑤", "5");
        valCode = valCode.replaceAll("⑥", "6");
        valCode = valCode.replaceAll("⑦", "7");
        valCode = valCode.replaceAll("⑧", "8");
        valCode = valCode.replaceAll("⑨", "9");
        valCode = valCode.replaceAll("o","0");//字母o替换成0
        valCode = valCode.replaceAll("L", "1");//字母L替换成1
        valCode = valCode.replaceAll("l", "1");//字母l替换成1
        if(valCode.startsWith("0")){
            valCode = "1"+valCode;
        }
        return valCode;
    }

    /**
     * 获取参数转换成list
     * @param map
     * @return
     */
    public static List<Integer> getParamsList(Map<String, Object> map, String name) {
        List<Integer> list = new ArrayList<>();
        Object object = map.get(name);
        if(object!=null){
            String objStr = object.toString();
            list = JsonUtil.strToStrIntegerList(objStr);
        }
        return list;
    }

    /**
     * 添加分页信息
     */
    public static StringBuilder appendPageMsg(StringBuilder sb, int pageNum, int pageSize){
        return sb.append(" limit ").append((pageNum-1)*pageSize).append(",").append(pageSize);
    }

    /**
     * 四舍五入
     */
    public static int round(double d){
        BigDecimal bigDecimal = new BigDecimal(d);
        double num = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
        return (int) num;
    }

    /**
     * 截取一位小数
     */
    public static double truncateOneDecimal(double num){
        BigDecimal bg = new BigDecimal(num);
        return bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 截取两位小数
     */
    public static double truncateTwoDecimal(double num){
        BigDecimal bg = new BigDecimal(num);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 返回列表数量
     */
    public static int listNum(List<Integer> list){
        if(list==null || list.size()==0 || list.get(0)==null) {
            return 0;
        }else{
            return list.get(0);
        }
    }

    /**
     * 返回列表数量
     */
    public static int listSize(List<Integer> list){
        if(list==null || list.size()==0 || list.get(0)==null) {
            return 0;
        }else{
            return list.size();
        }
    }

    /**
     * 返回列表数量
     */
    public static int strListSize(List<String> list){
        if(list==null || list.size()==0 || list.get(0)==null) {
            return 0;
        }else{
            return list.size();
        }
    }

    /**
     * 返回列表第一个信息
     */
    public static String listFirstStr(List<String> list){
        return (list==null || list.size()==0 || list.get(0)==null)?"":list.get(0);
    }

    /**
     * 转换数值格式
     */
    public static String toMoneySize(long num){
        BigDecimal bg = new BigDecimal(num);
        double d1 =  bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //转换成带,的两位小数
        DecimalFormat df = new DecimalFormat("###,###");
        if(d1==0){
            return "0";
        }else {
            return df.format(d1);
        }
    }

    /**
     * 返回列表
     */
    public static List<Integer> retList(List<Integer> list){
        if(list==null || list.size()==0 || list.get(0)==null) {
            return new ArrayList<>();
        }else{
            return list;
        }
    }

    /**
     * 字符串转换成list
     */
    public static List<String> strToStrList(String str) {
        return new ArrayList<>(Arrays.asList(str.split(",")));
    }

    /**
     * 获取区间值
     */
    public static int loadInterValNum(int minNum, int maxNum){
        if(minNum==maxNum){
            //相等
            return minNum;
        }else if(minNum<maxNum){
            //最小值小于最大值
            int ranNum = maxNum-minNum;//差值
            int addNum = (new Random()).nextInt(ranNum);//差值随机数
            return minNum+addNum;
        }else{
            //最小值大于最大值
            return 0;
        }
    }

    /**
     * 判断是否中奖
     */
    public static boolean isAward(int awardPro, int totalPro){
        boolean flag = false;
        if(totalPro>awardPro && totalPro>0 && awardPro>0){
            flag = (new Random()).nextInt(totalPro)<=awardPro;
        }else if(awardPro>=totalPro){
            flag = true;
        }
        return flag;
    }

    /**
     * 返回列表
     */
    public static List<String> strRetList(List<String> list){
        if(list==null || list.size()==0 || list.get(0)==null) {
            return new ArrayList<>();
        }else{
            return list;
        }
    }

    /**
     * 返回列表数量
     */
    public static String strListNum(List<String> list){
        if(list==null || list.size()==0 || list.get(0)==null) {
            return "0";
        }else{
            return list.get(0);
        }
    }

}
