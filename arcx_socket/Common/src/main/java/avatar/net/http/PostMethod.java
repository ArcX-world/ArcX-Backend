package avatar.net.http;

import avatar.global.linkMsg.http.NoticeHttpCmdName;

import java.util.ArrayList;
import java.util.List;

/**
 * 是否post
 */
public class PostMethod {

    /**
     * 是否通用post接口
     */
    public static boolean isGeneralPost(String uri){
        List<String> list = new ArrayList<>();
        boolean flag = false;//不包含接口信息
        for(int i=0;i<list.size();i++){
            String s = list.get(i);
            if(uri.contains(s)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 是否回调信息
     */
    public static boolean isCallBack(String uri){
        List<String> list = new ArrayList<>();
        list.add(NoticeHttpCmdName.SYSTEM_NOTICE);
        boolean flag = false;//不包含接口信息
        for(int i=0;i<list.size();i++){
            String s = list.get(i);
            if(uri.contains(s)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 是否不需要检查的回调
     */
    public static boolean isNoCheckCallBack(String uri){
        List<String> list = new ArrayList<>();
        boolean flag = false;//是否包含不需要检测的接口
        for(int i=0;i<list.size();i++){
            String s = list.get(i);
            if(uri.contains(s)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 不需要包含IP接口
     */
    public static boolean isNoConstainIp(String uri){
        List<String> list = new ArrayList<>();
        boolean flag = false;//是否不需要包含IP的接口
        for(int i=0;i<list.size();i++){
            String s = list.get(i);
            if(uri.contains(s)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 是否post请求
     */
    public static boolean isPost(String uri){
        List<String> list = new ArrayList<>();
        return list.contains(uri);
    }
}
