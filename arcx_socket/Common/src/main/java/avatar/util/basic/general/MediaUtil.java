package avatar.util.basic.general;

import avatar.entity.basic.systemConfig.SystemConfigEntity;
import avatar.entity.crossServer.CrossServerMediaPrefixEntity;
import avatar.module.basic.systemConfig.SystemConfigDao;
import avatar.module.crossServer.CrossServerMediaPrefixDao;
import avatar.module.crossServer.UserCrossPlatformImgDao;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.system.StrUtil;

/**
 * 多媒体工具类
 */
public class MediaUtil {
    /**
     * 获取多媒体路径
     */
    public static String getMediaUrl(String originalUrl){
        if(StrUtil.checkEmpty(originalUrl)){
            return originalUrl;
        }else {
            if(originalUrl.startsWith("http")){
                return originalUrl;
            }else{
                SystemConfigEntity entity = SystemConfigDao.getInstance().loadMsg();
                String url = "";
                if (entity != null) {
                    url = entity.getMediaPrefix()+originalUrl;
                }
                return url;
            }
        }
    }

    /**
     * 获取跨服多媒体路径
     */
    public static String getCrossServerMediaUrl(int serverType, String originalUrl){
        String retUrl = "";
        if(!StrUtil.checkEmpty(originalUrl)){
            if(originalUrl.startsWith("http")){
                retUrl = originalUrl;
            }else{
                CrossServerMediaPrefixEntity entity = CrossServerMediaPrefixDao.getInstance().loadByServerType(serverType);
                if (entity != null) {
                    retUrl = entity.getMediaPrefix()+originalUrl;
                }
            }
        }
        if(CrossServerMsgUtil.isMetaPusherServer(serverType) || retUrl.contains("default")){
            //mp或者默认头像
            retUrl = UserCrossPlatformImgDao.getInstance().loadMsg();
        }
        return retUrl;
    }

    /**
     * 客服头像
     */
    public static String customerHeadImg() {
        SystemConfigEntity entity = SystemConfigDao.getInstance().loadMsg();
        return getMediaUrl(entity.getServerImg());
    }
}
