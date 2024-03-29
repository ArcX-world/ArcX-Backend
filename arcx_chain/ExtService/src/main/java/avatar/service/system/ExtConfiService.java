package avatar.service.system;


import avatar.util.system.ExtConfig;

/**
 * 大厅配置信息
 */
public class ExtConfiService {
    //初始化信息
    public static void initData() {
        ExtConfig.init();//大厅配置文件
    }
}
