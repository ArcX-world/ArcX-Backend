package avatar.util.user;

import avatar.entity.user.opinion.CommunicateEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.user.opinion.CommunicateMsgDao;
import avatar.util.system.TimeUtil;

/**
 * 玩家意见工具类
 */
public class UserOpinionUtil {
    /**
     * 处理联系信息
     */
    public static void dealCommunicateMsg(String email) {
        //查询信息
        int emailSize = CommunicateMsgDao.getInstance().loadDbNum(email);
        if(emailSize==0){
            CommunicateMsgDao.getInstance().insert(initCommunicateEntity(email));
        }
    }

    /**
     * 填充联系实体信息
     */
    private static CommunicateEntity initCommunicateEntity(String email){
        CommunicateEntity entity = new CommunicateEntity();
        entity.setEmail(email);//邮箱
        entity.setDealBackUserId(0);//后台处理用户ID
        entity.setDealFlag(YesOrNoEnum.NO.getCode());//是否已处理
        entity.setCommentMsg("");//备注
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime("");//更新时间
        return entity;
    }

}
