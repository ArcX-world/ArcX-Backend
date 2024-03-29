package avatar.module.activity.sign;

import avatar.entity.activity.sign.info.WelfareSignAwardEntity;
import avatar.global.prefixMsg.ActivityPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 福利签到奖励数据接口
 */
public class WelfareSignAwardDao {
    private static final WelfareSignAwardDao instance = new WelfareSignAwardDao();
    public static final WelfareSignAwardDao getInstance(){
        return instance;
    }

    /**
     * 查询福利签到奖励
     */
    public ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> loadAll() {
        //从缓存获取
        ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> map = loadCache();
        if(map==null){
            //查询信息
            List<WelfareSignAwardEntity> list = loadDbAll();
            if(list!=null && list.size()>0){
                //设置缓存
                map = setCache(list);
            }
        }
        return map;
    }

    /**
     * 查询所有福利签到天数
     */
    public List<Integer> loadAllDay() {
        List<Integer> list = new ArrayList<>();
        ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> map = loadAll();
        if(map!=null && map.size()>0){
            list.addAll(map.keySet());
        }
        Collections.sort(list);
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> loadCache(){
        return (ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>>) GameData.getCache().get(ActivityPrefixMsg.WELFARE_SIGN_AWARD);
    }

    /**
     * 添加缓存
     */
    private ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> setCache(List<WelfareSignAwardEntity> list){
        ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> map = new ConcurrentHashMap<>();
        list.forEach(entity->{
            int day = entity.getDay();//天数
            List<WelfareSignAwardEntity> msgList = map.get(day);//信息列表
            if(msgList==null){
                msgList = new ArrayList<>();
            }
            msgList.add(entity);
            map.put(day, msgList);
        });
        GameData.getCache().set(ActivityPrefixMsg.WELFARE_SIGN_AWARD, map);
        return map;
    }

    //=========================db===========================

    /**
     * 查询福利签到信息
     */
    private List<WelfareSignAwardEntity> loadDbAll() {
        String sql = "select * from welfare_sign_award order by day,sequence";
        return GameData.getDB().list(WelfareSignAwardEntity.class, sql, new Object[]{});
    }

}
