package avatar;

/**
 * 初始化大厅所有的配置
 */
public class InitExtDBConfing extends InitDBConfig{
    @Override
    public void loadConfig() {
        initAllConfig();
    }


    public static void initAllConfig(){
        initAllDBConfig();
    }

    /**
     * 加载db中的配置 -- TODO 以后需要重构，将配置从db中抽离出来，统一excel管理，可以进行svn版本控制
     */
    public static void initAllDBConfig(){
    }

}
