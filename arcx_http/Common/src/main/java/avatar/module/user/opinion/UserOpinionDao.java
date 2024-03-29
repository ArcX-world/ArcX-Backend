package avatar.module.user.opinion;

import avatar.entity.user.opinion.UserOpinionEntity;
import avatar.global.enumMsg.basic.system.DealStatusEnum;
import avatar.util.GameData;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**
 * 玩家帮助与反馈接口
 */
public class UserOpinionDao {
    private static final UserOpinionDao instance = new UserOpinionDao();
    public static final UserOpinionDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public boolean insert(int userId, String opinion, String imgUrl){
        UserOpinionEntity entity = new UserOpinionEntity();
        entity.setUserId(userId);//玩家ID
        entity.setOpinion(opinion);//帮助与反馈
        entity.setImgUrl(imgUrl);//照片URL
        entity.setDealOpinion("");//处理意见
        entity.setDealBackUserId(0);//处理的后台用户ID
        entity.setStatus(DealStatusEnum.NO_DEAL.getCode());//状态
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        return GameData.getDB().insert(entity);
    }

    /**
     * 根据玩家ID查询玩家签到信息
     */
    public int loadDbByUserId(int userId) {
        String sql = "select IFNULL(count(id),0) from user_opinion where user_id=? and create_time>=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{userId, TimeUtil.getTodayTime()});
        return StrUtil.listNum(list);
    }
}
