package avatar.util.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 */
public class JsonUtil {
    /**
     * map转成json
     * @param mapStr
     * @return
     * @throws JSONException
     */
    public static String mapToJson(Map mapStr) throws JSONException {
        JSONObject jall = new JSONObject();
        jall.put("map", mapStr);//Map转换成Json
        String jsonStr =  jall.toString();
        jsonStr = jsonStr.substring(jsonStr.indexOf(":")+1,jsonStr.length()-1);
        return jsonStr;
    }

    /**
     * 字符串转成list
     * @param str
     * @return
     * @throws JSONException
     */
    public static List<String> strToStrList(String str)throws JSONException {
        Gson gson = new Gson();
        List<String> listMap = gson.fromJson(str, new TypeToken<List<String>>() {}.getType());//将json字符串转为List<Map<String,Object>>
        return listMap;
    }

    /**
     * 字符串转成int list
     * @param str
     * @return
     * @throws JSONException
     */
    public static List<Integer> strToStrIntegerList(String str)throws JSONException {
        Gson gson = new Gson();
        List<Integer> listMap = gson.fromJson(str, new TypeToken<List<Integer>>() {}.getType());//将json字符串转为List<Map<Integer,Object>>
        return listMap;
    }

    /**
     * 字符串转换成map
     * @param str
     * @return
     */
    public static Map<String, Object> strToMap(String str){
        Map<String, Object> maps = (Map<String, Object>) JSON.parse(str);
        return maps;
    }

    /**
     * 字符串转换成object map
     * @param str
     * @return
     */
    public static Map<Object, Object> strToObjectMap(String str){
        Map<Object, Object> maps = (Map<Object, Object>) JSON.parse(str);
        return maps;
    }

    /**
     * 字符转换成json
     * @param bytes
     * @return
     */
    public static JSONObject bytesToJson(byte[] bytes){
        String str = new String(bytes);
        JSONObject jsonObject = JSONObject.parseObject(str);
        return jsonObject;
    }

    /**
     * list转换成json
     * @param list
     * @return
     */
    public static String listToJson(List<Map<String,Object>> list){
        JSONArray jsonArray=new JSONArray(list);
        return jsonArray.toString();
    }


    public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);

        return documentBuilderFactory.newDocumentBuilder();
    }

    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        try {
            DocumentBuilder documentBuilder = newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
