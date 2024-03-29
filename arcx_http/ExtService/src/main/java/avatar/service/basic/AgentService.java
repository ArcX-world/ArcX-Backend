package avatar.service.basic;

import avatar.data.basic.agent.AgentConnectMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.net.session.Session;
import avatar.util.LogUtil;
import avatar.util.basic.AgentMsgUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代理接口实现类
 */
public class AgentService {
    /**
     * 代理信息
     */
    public static void agentMsg(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        String userIp = ParamsUtil.ip(map);//玩家ID
        LogUtil.getLogger().info("玩家获取代理信息的IP{}-----", userIp);
        //websocket代理信息列表
        List<AgentConnectMsg> agentWebsocketList = new ArrayList<>();
        AgentMsgUtil.loadAgentWebsocketList(userIp, agentWebsocketList, dataMap);
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), dataMap);
    }
}
