package avatar.module.user.info;

import avatar.data.user.info.EmailVerifyCodeMsg;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.login.LoginUtil;

/**
 * 邮箱验证码数据接口
 */
public class EmailVerifyCodeDao {
    private static final EmailVerifyCodeDao instance = new EmailVerifyCodeDao();
    public static final EmailVerifyCodeDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public EmailVerifyCodeMsg loadMsg(String email){
        //从缓存查找
        EmailVerifyCodeMsg msg = loadCache(email);
        if(msg==null){
            //查询数据库
            msg = LoginUtil.initEmailVerifyCodeMsg(email);
            //设置缓存
            setCache(email, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private EmailVerifyCodeMsg loadCache(String email){
        return (EmailVerifyCodeMsg) GameData.getCache().get(UserPrefixMsg.EMAIL_VERIFY_CODE+"_"+email);
    }

    /**
     * 添加缓存
     */
    public void setCache(String email, EmailVerifyCodeMsg msg){
        GameData.getCache().set(UserPrefixMsg.EMAIL_VERIFY_CODE+"_"+email, msg);
    }


}
