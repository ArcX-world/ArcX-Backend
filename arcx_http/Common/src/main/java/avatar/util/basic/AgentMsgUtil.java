package avatar.util.basic;

import avatar.data.basic.agent.AgentConnectMsg;
import avatar.entity.basic.agent.AgentAreaMsgEntity;
import avatar.entity.basic.agent.AgentDefaultMsgEntity;
import avatar.entity.basic.ip.IpAddressEntity;
import avatar.module.basic.agent.AgentAreaMsgDao;
import avatar.module.basic.agent.AgentConnectListDao;
import avatar.module.basic.agent.AgentDefaultMsgDao;
import avatar.module.basic.agent.AgentNationAreaMsgDao;
import avatar.module.basic.ip.IpAddressDao;
import avatar.util.LogUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;

import java.util.List;
import java.util.Map;

/**
 * 代理工具类
 */
public class AgentMsgUtil {
    /**
     * 获取代理websocket列表
     */
    public static void loadAgentWebsocketList(String userIp, List<AgentConnectMsg> agentWebsocketList,
            Map<String, Object> dataMap) {
        boolean specifiedFlag = false;
        String nation = "";//国家
        String specifyHttp = "";//指定http域名
        if(!StrUtil.checkEmpty(userIp)) {
            IpAddressEntity ipAdressEntity = IpAddressDao.getInstance().loadByIp(userIp);
            String specifyIp = "";//指定IP
            String specifyPort = "";//指定端口
            if (ipAdressEntity != null && !StrUtil.checkEmpty(ipAdressEntity.getNation())) {
                nation = ipAdressEntity.getNation().trim();//国家
                int areaId = loadNationArea(nation);
                if (areaId > 0) {
                    //根据区域获取IP
                    AgentAreaMsgEntity areaMsgEntity = AgentAreaMsgDao.getInstance()
                            .loadByArea(areaId);
                    if (areaMsgEntity != null) {
                        specifiedFlag = true;
                        specifyIp = areaMsgEntity.getSocketIp();//socket ip
                        specifyPort = areaMsgEntity.getSocketPort();//socket 端口
                        specifyHttp = areaMsgEntity.getHttpDomain();//http域名
                    }
                }
            }
            //第一指定端口
            if(specifiedFlag){
                agentWebsocketList.add(initAgentConnectMsg(specifyIp, specifyPort));
            }
        }
        //添加其他socket端口
        List<AgentConnectMsg> areaList = AgentConnectListDao.getInstance().loadMsg();
        if(areaList.size()>0){
            areaList.forEach(msg->{
                if(!addConnectMsg(agentWebsocketList, msg.getIp(), msg.getPort())){
                    agentWebsocketList.add(initAgentConnectMsg(msg.getIp(), msg.getPort()));
                }
            });
        }
        //添加默认的域名
        if(StrUtil.checkEmpty(specifyHttp)){
            String defaultNation = AgentDefaultMsgDao.getInstance().loadMsg().getDefaultNation();//默认国家
            if(!StrUtil.checkEmpty(defaultNation)){
                //默认区域信息
                int areaId = loadNationArea(nation);
                if (areaId > 0) {
                    //根据区域获取IP
                    AgentAreaMsgEntity areaMsgEntity = AgentAreaMsgDao.getInstance()
                            .loadByArea(areaId);
                    if(areaMsgEntity!=null){
                        //http域名
                        if(StrUtil.checkEmpty(specifyHttp)){
                            specifyHttp = areaMsgEntity.getHttpDomain();
                        }
                    }
                }
            }
        }
        dataMap.put("wsTbln", agentWebsocketList);
        dataMap.put("svDm", specifyHttp);
        LogUtil.getLogger().info("玩家获取代理的国家地区：{}，{}特殊分配IP和端口，返回的代理信息：{}",
                nation, specifiedFlag ? "有" : "无", JsonUtil.mapToJson(dataMap));
    }

    /**
     * 根据国家获取区域ID
     */
    private static int loadNationArea(String nation) {
        //获取区域ID
        int areaId = AgentNationAreaMsgDao.getInstance().loadByNation(nation);
        if(areaId==0){
            //获取默认国家
            String defaultNation = loadWebsocketDefaultNation();
            if(!StrUtil.checkEmpty(defaultNation)){
                //查询默认国家区域
                areaId = AgentNationAreaMsgDao.getInstance().loadByNation(defaultNation);
                if(areaId>0) {
                    LogUtil.getLogger().info("代理玩家初始国家{}没有指定区域websocket，分配到默认的国家{}对应的区域--------",
                            nation, defaultNation);
                }
            }
        }
        return areaId;
    }

    /**
     * 填充代理socket信息
     */
    private static AgentConnectMsg initAgentConnectMsg(String ip, String port) {
        AgentConnectMsg msg = new AgentConnectMsg();
        msg.setIp(ip);//ip
        msg.setPort(port);//端口
        return msg;
    }

    /**
     * 代理连接信息
     */
    public static void initAgentConnectMsg(List<AgentAreaMsgEntity> list, List<AgentConnectMsg> retList) {
        if(list!=null && list.size()>0){
            list.forEach(entity->{
                if(!addConnectMsg(retList, entity.getSocketIp(), entity.getSocketPort())){
                    AgentConnectMsg msg = new AgentConnectMsg();
                    msg.setIp(entity.getSocketIp());
                    msg.setPort(entity.getSocketPort());
                    retList.add(msg);
                }
            });
        }
    }

    /**
     * 是否连接信息已经添加
     */
    private static boolean addConnectMsg(List<AgentConnectMsg> list, String ip, String port) {
        boolean flag = false;
        if(list.size()>0){
            for(AgentConnectMsg msg : list ){
                if(msg.getIp().equals(ip) && msg.getPort().equals(port)){
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 获取websocket默认国家
     */
    private static String loadWebsocketDefaultNation() {
        AgentDefaultMsgEntity entity = AgentDefaultMsgDao.getInstance().loadMsg();
        return entity==null?"":entity.getDefaultNation();
    }
}
