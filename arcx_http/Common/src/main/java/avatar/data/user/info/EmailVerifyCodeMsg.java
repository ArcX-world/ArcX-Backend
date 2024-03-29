package avatar.data.user.info;

import java.io.Serializable;

/**
 * 邮箱验证码信息
 */
public class EmailVerifyCodeMsg implements Serializable {
    //邮箱
    private String email;

    //验证码
    private String verifyCode;

    //发送时间
    private long sendTime;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
