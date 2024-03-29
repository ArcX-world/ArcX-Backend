package avatar.util.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * list工具类
 */
public class ListUtil {

    /**
     * 填充分页信息
     */
    public static void fillPageMsg(Map<String, Object> map, int dataSumNum, int pageNum, int pageSize){
        int dataNum = 0;//当前页数据量
        if(pageNum<=(dataSumNum/pageSize)){
            dataNum = pageSize;
        }else{
            if(pageNum==(dataSumNum/pageSize+1)){
                dataNum = dataSumNum%pageSize;
            }
        }
        int pageSumNum = dataSumNum/pageSize;//总页数
        if(dataSumNum%pageSize>0){
            pageSumNum += 1;
        }
        map.put("dataSumNum", dataSumNum);//数据总量
        map.put("dataNum", dataNum);//当前页数据量
        map.put("pageNum", pageNum);//当前页码
        map.put("pageSumNum", pageSumNum);//总页数
    }

    /**
     * 获取分页数据
     * @return
     */
    public static List getPageList(int pageNum, int pageSize, List dealList){
        if(dealList!=null) {
            int startNum = (pageNum - 1) * pageSize;//开始数量
            int endNum = startNum + pageSize;//结束数量
            List list = new ArrayList(dealList);//处理的列表
            int size = list == null ? 0 : list.size();//动态数量
            //返回的信息
            List newList = new ArrayList<>();
            if (size > 0) {
                if (startNum < (size)) {
                    if (endNum <= (size - 1)) {
                        newList = (List) list.stream().skip(startNum).limit(pageSize).collect(Collectors.toList());
                    } else {
                        newList = (List) list.stream().skip(startNum).limit(size - startNum).collect(Collectors.toList());
                    }
                } else if (startNum == (size - 1)) {
                    newList.add(list.get(startNum));
                }
            }
            return newList;
        }else{
            return new ArrayList();
        }
    }
}
