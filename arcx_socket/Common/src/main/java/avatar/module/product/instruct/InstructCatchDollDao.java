package avatar.module.product.instruct;

import avatar.entity.product.instruct.InstructCatchDollEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作抓娃娃指令数据接口
 */
public class InstructCatchDollDao {
    private static final InstructCatchDollDao instance = new InstructCatchDollDao();
    public static final InstructCatchDollDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public String loadByName(String name){
        String instruct = loadCache(name);
        if(instruct==null){
            //查询数据库
            InstructCatchDollEntity entity = loadDbByName(name);
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
        Object obj = GameData.getCache().get(ProductPrefixMsg.INSTRUCT_CATCH_DOLL+"_"+name);
        if(obj!=null){
            return obj.toString();
        }
        return null;
    }

    /**
     * 添加缓存
     */
    private void setCache(String name, String instruct){
        GameData.getCache().set(ProductPrefixMsg.INSTRUCT_CATCH_DOLL+"_"+name, instruct);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private InstructCatchDollEntity loadDbByName(String name) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("name", name);//名称
        String sql = SqlUtil.getSql("instruct_catch_doll", paramsMap).toString();
        return GameData.getDB().get(InstructCatchDollEntity.class, sql, new Object[]{});
    }

}
