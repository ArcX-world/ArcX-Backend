package avatar.module.crossServer;

import avatar.entity.user.info.UserDefaultHeadImgEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.CrossServerPrefixMsg;
import avatar.util.GameData;
import avatar.util.basic.general.MediaUtil;
import avatar.util.system.StrUtil;

/**
 * 玩家跨平台头像数据接口
 */
public class UserCrossPlatformImgDao {
    private static final UserCrossPlatformImgDao instance = new UserCrossPlatformImgDao();
    public static final UserCrossPlatformImgDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public String loadMsg(){
        String imgUrl = loadCache();
        if(StrUtil.checkEmpty(imgUrl)){
            //查询数据库
            imgUrl = loadDbMsg();
            if(!StrUtil.checkEmpty(imgUrl)) {
                //设置缓存
                setCache(imgUrl);
            }
        }
        return imgUrl;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private String loadCache(){
        Object obj = GameData.getCache().get(CrossServerPrefixMsg.USER_CROSS_PLATFORM_IMG);
        return obj==null?"":obj.toString();
    }

    /**
     * 添加缓存
     */
    private void setCache(String imgUrl){
        //保存缓存信息
        GameData.getCache().set(CrossServerPrefixMsg.USER_CROSS_PLATFORM_IMG, imgUrl);
    }


    //=========================db===========================

    /**
     * 查询数据
     */
    private String loadDbMsg() {
        String sql = "select * from user_default_head_img where is_cross_platform=?";
        UserDefaultHeadImgEntity entity = GameData.getDB().get(UserDefaultHeadImgEntity.class, sql,
                new Object[]{YesOrNoEnum.YES.getCode()});
        if(entity!=null){
            return MediaUtil.getMediaUrl(entity.getImgUrl());
        }else{
            return "";
        }
    }


}
