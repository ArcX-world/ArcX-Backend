package avatar.module.crossServer;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.user.UserUtil;

/**
 * 服务端玩家信息
 */
public class ServerSideUserMsgDao {
    private static final ServerSideUserMsgDao instance = new ServerSideUserMsgDao();
    public static final ServerSideUserMsgDao getInstance(){
        return instance;
    }

    /**
     * 根据
     */
    public ProductGamingUserMsg loadByMsg(int userId, int serverSideType){
        //从缓存获取
        return loadCache(userId, serverSideType);
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private ProductGamingUserMsg loadCache(int userId, int serverSideType) {
        return GameData.getCache().get(ProductPrefixMsg.SERVER_SIDE_USER_MSG+"_"+userId+"_"+serverSideType)==null?
                UserUtil.initProductGamingUserMsg(userId, serverSideType) :
                (ProductGamingUserMsg) GameData.getCache().get(ProductPrefixMsg.SERVER_SIDE_USER_MSG+"_"+userId+"_"+serverSideType);
    }

    /**
     * 设置缓存
     */
    public void setCache(int userId, int serverSideType, ProductGamingUserMsg userMsg) {
        GameData.getCache().set(ProductPrefixMsg.SERVER_SIDE_USER_MSG+"_"+userId+"_"+serverSideType, userMsg);
    }

}
