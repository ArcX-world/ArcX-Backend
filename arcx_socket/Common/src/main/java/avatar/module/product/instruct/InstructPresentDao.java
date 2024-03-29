package avatar.module.product.instruct;

import avatar.entity.product.instruct.InstructPresentEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作礼品机指令数据接口
 */
public class InstructPresentDao {
    private static final InstructPresentDao instance = new InstructPresentDao();
    public static final InstructPresentDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public String loadByName(String name){
        String instruct = loadCache(name);
        if(instruct==null){
            //查询数据库
            InstructPresentEntity entity = loadDbByName(name);
            if(entity!=null){
                instruct = entity.getInstruct();//指令
                setCache(name, instruct);
            }
        }
        return instruct;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private String loadCache(String name){
        Object obj = GameData.getCache().get(ProductPrefixMsg.INSTRUCT_PRESENT+"_"+name);
        if(obj!=null){
            return obj.toString();
        }
        return null;
    }

    /**
     * 添加缓存
     */
    private void setCache(String name, String instruct){
        GameData.getCache().set(ProductPrefixMsg.INSTRUCT_PRESENT+"_"+name, instruct);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private InstructPresentEntity loadDbByName(String name) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("name", name);//名称
        String sql = SqlUtil.getSql("instruct_present", paramsMap).toString();
        return GameData.getDB().get(InstructPresentEntity.class, sql, new Object[]{});
    }

}
