package avatar.util.thirdpart;

import avatar.entity.basic.ip.IpAddressEntity;
import avatar.global.basicConfig.thirdpart.IcretAdressMsg;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;


/**
 * 艾科瑞特ip地址查询工具类
 */
public class IcretAddressUtil {
    /**
     * 读取返回结果
     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), "utf-8");
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    /**
     * 获取地址
     */
    public static String loadAddressByIp(String ip){
        String json = "";//返回的结果json
        String urlSend = IcretAdressMsg.ipHost +"?IP_ADDR=" + ip;   // 【5】拼接请求链接
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestProperty("Authorization", "APPCODE " + IcretAdressMsg.ipAppCode);// 格式Authorization:APPCODE (中间是英文空格)
            int httpCode = httpURLCon.getResponseCode();
            if (httpCode == 200) {
                json = read(httpURLCon.getInputStream());
            }
        } catch (MalformedURLException e) {
            LogUtil.getLogger().info("艾科瑞特ip{}获取地址的时候URL格式错误--------", ip);
        } catch (UnknownHostException e) {
            LogUtil.getLogger().info("艾科瑞特ip{}获取地址的时候URL地址错误--------", ip);
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        }
        return json;
    }

    /**
     * 填充ip地址实体信息
     */
    public static IpAddressEntity initIpAddress(String ip, String address) {
        String enShort = "";//英文简称
        String enName = "";//英文名字
        String global = "";//洲际
        String nation = "";//国家
        String province = "";//省份
        String city = "";//城市
        String adcode = "";//地区码
        double lon = 0;//经度
        double lat = 0;//纬度
        try {
            Map<String, Object> dataMap = JsonUtil.strToMap(address);
            if (dataMap != null && dataMap.size() > 0 && dataMap.containsKey("ENTITY")) {
                Map<String, Object> entityMap = (Map<String, Object>) dataMap.get("ENTITY");
                if(entityMap!=null && entityMap.size()>0 && entityMap.containsKey("INPUT_IP_ADDRESS")) {
                    Map<String, Object> resultMap = (Map<String, Object>) entityMap.get("INPUT_IP_ADDRESS");
                    if (resultMap != null && resultMap.size() > 0) {
                        //英文简写
                        if (resultMap.containsKey("NATION_NAME_EN_ABBR")) {
                            enShort = (String) resultMap.get("NATION_NAME_EN_ABBR");
                            if (StrUtil.checkEmpty(enShort) || enShort.equals("null")) {
                                enShort = "";
                            }
                        }
                        //英文名称
                        if (resultMap.containsKey("NATION_NAME_EN")) {
                            enName = (String) resultMap.get("NATION_NAME_EN");
                            if (StrUtil.checkEmpty(enName) || enName.equals("null")) {
                                enName = "";
                            }
                        }
                        //洲际
                        if (resultMap.containsKey("GLOBAL")) {
                            global = (String) resultMap.get("GLOBAL");
                            if (StrUtil.checkEmpty(global) || global.equals("null")) {
                                global = "";
                            }
                        }
                        //国家
                        if (resultMap.containsKey("NATION")) {
                            nation = (String) resultMap.get("NATION");
                            if (StrUtil.checkEmpty(nation) || nation.equals("null")) {
                                nation = "";
                            }
                        }
                        //省份
                        if (resultMap.containsKey("PROVINCE")) {
                            province = (String) resultMap.get("PROVINCE");
                            if (StrUtil.checkEmpty(province) || province.equals("null")) {
                                province = "";
                            }
                        }
                        //城市
                        if (resultMap.containsKey("CITY")) {
                            city = (String) resultMap.get("CITY");
                            if (StrUtil.checkEmpty(city) || city.equals("null")) {
                                city = "";
                            }
                        }
                        //地区码
                        if (resultMap.containsKey("DISTRICT")) {
                            adcode = resultMap.get("DISTRICT").toString();
                            if (StrUtil.checkEmpty(adcode) || adcode.equals("null")) {
                                adcode = "";
                            }
                        }
                        //GPS
                        if (resultMap.containsKey("GPS") && resultMap.get("GPS") != null
                                && !StrUtil.checkEmpty(resultMap.get("GPS").toString())) {
                            String gps = resultMap.get("GPS").toString();
                            if (gps.contains(",")) {
                                //经度
                                lon = Double.parseDouble(gps.split(",")[0]);
                                //纬度
                                lat = Double.parseDouble(gps.split(",")[1]);
                            }
                        }
                        if (StrUtil.checkEmpty(nation)) {
                            nation = "未知";
                        }
                        IpAddressEntity entity = new IpAddressEntity();
                        entity.setIp(ip);//ip
                        entity.setEnShort(enShort);//英文简称
                        entity.setEnName(enName);//英文名字
                        entity.setGlobal(global);//洲际
                        entity.setNation(nation);//国家
                        entity.setProvince(province);//省份
                        entity.setCity(city);//城市
                        entity.setAdcode(adcode);//地区码
                        entity.setLon(lon);//经度
                        entity.setLat(lat);//纬度
                        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
                        return entity;
                    }
                }
            } else {
                LogUtil.getLogger().info("艾科瑞特ip获取ip{}地址的时候返回的信息为{}，为空或者不包含showapi_res_body------", ip, address);
                return null;
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            return null;
        }
        return null;
    }
}
