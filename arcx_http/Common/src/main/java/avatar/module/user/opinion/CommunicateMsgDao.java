package avatar.module.user.opinion;

import avatar.entity.user.opinion.CommunicateEntity;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 联系信息数据接口
 */
public class CommunicateMsgDao {
    private static final CommunicateMsgDao instance = new CommunicateMsgDao();
    public static final CommunicateMsgDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public void insert(CommunicateEntity entity){
        GameData.getDB().insert(entity);
    }

    /**
     * 查询数量
     */
    public int loadDbNum(String email) {
        String sql = "select count(id) from communicate_msg where email=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{email});
        return StrUtil.listNum(list);
    }
}
