package avatar.module.crossServer;

import avatar.data.crossServer.GeneralCrossServerUserMsg;
import avatar.global.prefixMsg.CrossServerPrefixMsg;
import avatar.util.GameData;
import avatar.util.crossServer.CrossServerMsgUtil;

/**
 * 跨服玩家信息数据接口
 */
public class CrossServerUserMsgDao {
    private static final CrossServerUserMsgDao instance = new CrossServerUserMsgDao();
    public static final CrossServerUserMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public GeneralCrossServerUserMsg loadByMsg(int serverSideType, int userId){
        //从缓存查找
        GeneralCrossServerUserMsg CrossServerUserMsg = loadCache(serverSideType, userId);
        if(CrossServerUserMsg==null){
            //查询信息
            CrossServerUserMsg = CrossServerMsgUtil.loadGeneralCrossServerUserMsg(
                    serverSideType,userId);
            //更新缓存
            setCache(serverSideType, userId, CrossServerUserMsg);
        }
        //处理返回信息
        CrossServerMsgUtil.dealCrossServerUserMsg(CrossServerUserMsg);
        return CrossServerUserMsg;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private GeneralCrossServerUserMsg loadCache(int serverSideType, int userId){
        return (GeneralCrossServerUserMsg) GameData.getCache().get(
                CrossServerPrefixMsg.CROSS_SERVER_USER_MSG+"_"+serverSideType+"_"+userId);
    }

    /**
     * 添加缓存
     */
    public void setCache(int serverSideType, int userId, GeneralCrossServerUserMsg msg){
        GameData.getCache().set(CrossServerPrefixMsg.CROSS_SERVER_USER_MSG+"_"+serverSideType+"_"+userId, msg);
    }

}
