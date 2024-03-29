package avatar.service.user;

import avatar.data.user.balance.PropertyKnapsackMsg;
import avatar.module.basic.systemMsg.PropertyListDao;
import avatar.net.session.Session;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ListUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserPropertyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 玩家背包接口实现类
 */
public class UserKnapsackService {

    /**
     * 道具背包
     */
    public static void propertyKnapsack(Map<String, Object> map, Session session) {
        List<PropertyKnapsackMsg> retList = new ArrayList<>();
        //检测参数
        int status = CheckParamsUtil.checkAccessTokenPage(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            //查询道具列表信息
            List<Integer> propertyList = PropertyListDao.getInstance().loadMsg();
            //分页处理
            List<Integer> loadList = ListUtil.getPageList(ParamsUtil.pageNum(map),
                    ParamsUtil.pageSize(map), propertyList);
            if(loadList.size()>0) {
                //信息处理
                UserPropertyUtil.propertyKnapsack(userId, loadList, retList);
            }
        }
        //传输的jsonMap，先填充list
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("serverTbln", retList);
        //推送结果
        SendMsgUtil.sendBySessionAndList(session, status, jsonMap);
    }
}
