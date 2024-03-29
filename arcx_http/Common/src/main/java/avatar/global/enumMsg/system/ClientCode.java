package avatar.global.enumMsg.system;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举：错误码
 */
public enum ClientCode {
    FAIL(0,"失败", "fail"),
    SUCCESS(1,"成功", "success"),
    LIMIT(1000,"limit", "limit"),
    ACCOUNT_DISABLED(1001,"账号异常", "Network error"),//账号异常
    SYSTEM_MAINTAIN(1002,"系统维护中", "System Maintaining"),
    PARAMS_ERROR(1003,"参数错误，请核对", "Parameter error, please check"),
    VERIFY_SIGN_ERROR(1004,"验证签名失败", "Verification signature failed"),
    USER_NO_EXIST(1005,"玩家信息不存在", "Player information does not exist"),
    ACCESS_TOKEN_OUT_TIME(1006,"调用凭证过期", "access_token out time"),
    ACCESS_TOKEN_ERROR(1007,"请更新APP", "access token error"),
    REFRESH_TOKEN_OUT_TIME(1008,"刷新凭证过期", "refresh token out time"),
    REFRESH_TOKEN_NOT_FIT(1009,"刷新凭证不匹配", "refresh token not fit"),
    LOGIN_PLEASE(1010,"请登录", "Please log in"),
    CHECK_CODE_FAIL(1011,"校验失败", "Verification Failed"),
    BALANCE_NO_ENOUGH(1012,"余额不足", "Balance is insufficient"),
    PRODUCT_NO_EXIST(1013,"设备不存在", "The device does not exist"),
    PRODUCT_EXCEPTION(1014,"机器故障", "machine fault"),
    NO_BONUS_TIME(1015,"无奖励次数", "No reward times"),
    FREQUENTLY_SEND(1016,"频繁发送","Do not send frequently"),
    VERIFY_CODE_LOSE_EFFICACY(1017,"验证码已失效","The verification code has expired"),
    VERIFY_CODE_ERROR(1018,"验证码错误", "Verification code error"),
    PASSWORD_ERROR(1019,"密码错误","Password error"),
    STATUS_ERROR(1020,"状态错误", "Status error"),
    LENGTH_LIMIT(1021,"长度超出限制","Length exceeds limit"),
    MSG_NOT_FIT(1022,"信息不匹配", "Information mismatch"),
    OPINION_DAILY_MAX(1024,"反馈信息已达每日上限", "Feedback information has reached the daily limit"),
    UPGRADE_CONDITION_NOT_FIT(1025,"升级条件不满足","Upgrade conditions not met"),
    REPAIR_MSG_EXIST(1026, "报修信息已经存在", "The repair information already exists"),
    SUCCESS_REPAIR(1027,"已通知维修人员，请耐心等待！",
            "The maintenance personnel have been notified, please be patient!"),
    SYSTEM_ERROR(1028,"系统错误", "system error"),
    INVALID_COMMODITY(1029,"无效商品","Invalid product"),
    PROPERTY_NO_ENOUGH(1030,"道具数量不足","Insufficient number of props"),
    BUY_LIMIT(1031,"购买上限","Purchase limit"),
    WALLET_ERROR(1032,"钱包信息异常","Wallet information abnormality"),
    TOKENS_TYPE_ERROR(1033,"代币类型错误", "Token type error"),
    NO_SELL_GOLD_MACHINE(1034,"暂无售币机","There is currently no coin vending machine available"),
    NFT_OFF_OPERATE(1035,"非营业中，兑换失败","Non operating, exchange failed"),
    NFT_PRICE_CHANGE(1036,"价格变动，兑换失败","Price change, exchange failed"),
    NFT_STORED_NO_ENOUGH(1037,"储币不足，兑换失败","Insufficient coin storage, exchange failed"),
    NFT_NO_EXIST(1038,"无对应NFT信息","No corresponding NFT information"),
    NFT_STATUS_ERROR(1039,"NFT状态错误", "NFT status error"),
    NFT_USER_ERROR(1040,"NFT玩家不匹配", "NFT player mismatch"),
    NFT_STORED_NUM_ERROR(1041,"NFT储币值异常","Abnormal NFT currency storage value"),
    NFT_DURABILITY(1042,"售币机耐久度不足","Insufficient durability of coin vending machines"),
    ;


    private int code;//错误码

    private String name;//中文描述

    private String english;//英文描述

    ClientCode(int code, String name, String english){
        this.code = code;
        this.name = name;
        this.english = english;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}

    public String getEnglish(){return english;}

    /**
     * 英文描述
     */
    public static Map<Integer, String> toEnglishMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (ClientCode clientCode : ClientCode.values()) {
            map.put(clientCode.getCode(), clientCode.getEnglish());
        }
        return map;
    }

    /**
     * 获取错误信息
     */
    public static String getErrorMsg(int status){
        return toEnglishMap().get(status);//错误信息
    }

}
