package avatar.module.nft.user;

import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 玩家NFT列表
 */
public class UserNftListDao {
    private static final UserNftListDao instance = new UserNftListDao();
    public static final UserNftListDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public List<String> loadMsg(int userId){
        List<String> list = loadCache(userId);
        if(list==null){
            list = loadDbList(userId);
            //设置缓存
            setCache(userId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<String> loadCache(int userId){
        return (List<String>)
                GameData.getCache().get(NftPrefixMsg.USER_NFT_LIST+"_"+userId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, List<String> list){
        GameData.getCache().set(NftPrefixMsg.USER_NFT_LIST+"_"+userId, list);
    }

    /**
     * 删除缓存
     */
    public void removeCache(int userId){
        GameData.getCache().removeCache(NftPrefixMsg.USER_NFT_LIST+"_"+userId);
    }

    //=========================db===========================

    /**
     * 查询列表
     */
    private List<String> loadDbList(int userId) {
        String sql = "select nft_code from sell_gold_machine_msg where user_id=? order by status desc,lv desc,create_time ";
        List<String> list = GameData.getDB().listString(sql, new Object[]{userId});
        return StrUtil.strRetList(list);
    }
}
