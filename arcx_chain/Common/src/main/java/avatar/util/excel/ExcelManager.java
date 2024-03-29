package avatar.util.excel;

import avatar.global.Config;

/**
 * excel 配置表操作类
 */
public class ExcelManager {
    private static final ExcelManager instance = new ExcelManager();
    public static final ExcelManager getInstance(){
        return instance;
    }

    private String getPath(){
        if(Config.getInstance().isWindowsOS()){
            return "../deploy/config/excel/";
        }else{
            return "./config/excel/";
        }
    }

    /**
     * 获得一个Excel读取器，特别注意，一定要释放（就是读取完后要调用close）
     */
    public MyXlsReader buildReader(String fileName){
        fileName = String.format("%s%s", getPath() , fileName);
        MyXlsReader myXlsReader = new MyXlsReader(fileName);
        return myXlsReader;
    }

}
