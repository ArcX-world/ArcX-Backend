package avatar.module.solana;

import avatar.entity.solana.SolanaSignMsgEntity;
import avatar.util.GameData;
import avatar.util.solana.SolanaUtil;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * solana签名列表数据接口
 */
public class SolanaSignMsgDao {
    private static final SolanaSignMsgDao instance = new SolanaSignMsgDao();
    public static final SolanaSignMsgDao getInstance() {
        return instance;
    }

    //=========================db===========================

    /**
     * 根据签名查询信息
     */
    public boolean loadDbBySign(String signature){
        String sql = "select id from solana_sign_msg where signature=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{signature});
        boolean flag = StrUtil.listSize(list)>0;
        if(!flag){
            //添加
            insert(SolanaUtil.initSolanaSignMsgEntity(signature));
        }
        return flag;
    }

    /**
     * 添加
     */
    private void insert(SolanaSignMsgEntity entity) {
        GameData.getDB().insert(entity);
    }

}
