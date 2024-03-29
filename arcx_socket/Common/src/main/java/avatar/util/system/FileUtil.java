package avatar.util.system;

import avatar.util.checkParams.ErrorDealUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 */
public class FileUtil {
    /**
     * 获取敏感词汇文件
     * @param filePath
     * @return
     */
    public static List<String> readTxtFile(String filePath){
        List<String> list = new ArrayList<>();
        try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    System.out.println(lineTxt);
                    if(lineTxt.contains("、")) {
                        String[] strArr = lineTxt.split("、");
                        for(String str : strArr){
                            list.add(str);
                        }
                    }
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            ErrorDealUtil.printError(e);
        }
        return list;
    }

}
