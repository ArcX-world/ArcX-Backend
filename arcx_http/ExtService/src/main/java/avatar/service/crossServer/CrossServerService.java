package avatar.service.crossServer;

import avatar.global.enumMsg.system.ClientCode;
import avatar.net.session.Session;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 跨服接口实现类
 */
public class CrossServerService {

    /**
     * 跨服玩家信息
     */
    public static void crossServerUserMsg(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        int csUserId = ParamsUtil.intParmas(map, "csUserId");//跨服玩家ID
        if (csUserId > 0) {
            dataMap.put("userMsg", CrossServerMsgUtil.initUserMsg(csUserId));//玩家信息
        }
        //推送结果
        SendMsgUtil.sendCrossBySessionAndMap(session, ClientCode.SUCCESS.getCode(), dataMap);
    }

}
