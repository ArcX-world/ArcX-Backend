package avatar.global.basicConfig.thirdpart;

/**
 * 机修公众号参数信息
 */
public class OfficialAccountMsg {
    //公众号请求连接
    public static String officalAccountUrl="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    //微信公众号的appid
    public static String wx_appid="wx407a009dd16fb1e7";
    //微信公众号的appsecret
    public static String wx_appsecret="613d809b94cd7965eba54f07c3611592";
    //获取公众号access_token的url
    public static String accessTokenUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
}
