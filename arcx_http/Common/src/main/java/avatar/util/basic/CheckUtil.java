package avatar.util.basic;

import avatar.entity.basic.systemConfig.SystemConfigEntity;
import avatar.global.basicConfig.basic.ConfigMsg;
import avatar.global.enumMsg.basic.system.ServerTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.basic.systemConfig.SystemConfigDao;

/**
 * 检测工具类
 */
public class CheckUtil {
    /**
     * 是否测试环境
     */
    public static boolean isTestEnv(){
        //查询配置信息
        SystemConfigEntity entity = SystemConfigDao.getInstance().loadMsg();
        return entity!=null && entity.getServerType()!= ServerTypeEnum.ONLINE.getCode();
    }

    /**
     * 获取域名信息
     */
    public static String loadDomainName(){
        //获取域名
        String name = ConfigMsg.localHttpName;//本地域名
        if(!CheckUtil.isTestEnv()){
            name = ConfigMsg.onlineHttpName;//线上域名
        }
        return name;
    }

    /**
     * 是否系统维护中
     */
    public static boolean isSystemMaintain(){
        //查询系统配置信息
        SystemConfigEntity entity = SystemConfigDao.getInstance().loadMsg();
        return entity.getSystemMaintain()== YesOrNoEnum.YES.getCode();
    }
}
