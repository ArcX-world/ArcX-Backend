package avatar.util.basic;

import avatar.entity.basic.systemMsg.PropertyMsgEntity;
import avatar.module.basic.systemMsg.PropertyMsgDao;

/**
 * 道具信息工具类
 */
public class PropertyMsgUtil {
    //获取道具图片
    public static String loadImgUrl(int propertyType){
        //查询信息
        PropertyMsgEntity entity = PropertyMsgDao.getInstance().loadMsg(propertyType);
        return entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl());
    }
}
