package avatar.util.checkParams;

import avatar.util.LogUtil;

/**
 * 错误处理工具类
 */
public class ErrorDealUtil {
    /**
     * 错误处理（将错误打印到日志里）
     */
    public static void printError(Exception e){
        LogUtil.getLogger().error(e.toString());
        for(StackTraceElement element : e.getStackTrace()){
            LogUtil.getLogger().error(element.toString());
        }
    }
}
