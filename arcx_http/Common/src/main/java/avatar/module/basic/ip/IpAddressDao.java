package avatar.module.basic.ip;

import avatar.entity.basic.ip.IpAddressEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;
import avatar.util.thirdpart.IcretAddressUtil;

/**
 * ip对应地址信息数据接口
 */
public class IpAddressDao {
    private static final IpAddressDao instance = new IpAddressDao();
    public static final IpAddressDao getInstance(){
        return instance;
    }

    /**
     * 根据IP查询地址
     */
    public IpAddressEntity loadByIp(String ip) {
        //从缓存获取
        IpAddressEntity entity = loadCache(ip);
        if(entity==null){
            //查询数据库信息
            entity = loadDbByIp(ip);
            if(entity!=null){
                //添加缓存
                setCache(ip, entity);
            }else{
                //获取IP信息
                String address = IcretAddressUtil.loadAddressByIp(ip);
                if(!StrUtil.checkEmpty(address)){
                    //添加数据
                    entity = insert(ip, address);
                    if(entity!=null){
                        //添加缓存
                        setCache(ip, entity);
                    }
                }
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private IpAddressEntity loadCache(String ip){
        return (IpAddressEntity) GameData.getCache().get(PrefixMsg.IP_ADDRESS+"_"+ip);
    }

    /**
     * 添加缓存
     */
    private void setCache(String ip, IpAddressEntity entity){
        GameData.getCache().set(PrefixMsg.IP_ADDRESS+"_"+ip, entity);
    }

    //=========================db===========================

    /**
     * 根据IP查询地址
     */
    private IpAddressEntity loadDbByIp(String ip) {
        String sql = "select * from ip_address where ip=?";
        return GameData.getDB().get(IpAddressEntity.class, sql, new Object[]{ip});
    }

    /**
     * 添加数据
     */
    private IpAddressEntity insert(String ip, String address) {
        //填充实体
        IpAddressEntity entity = IcretAddressUtil.initIpAddress(ip, address);
        if(entity==null){
            return null;
        }
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);
            return entity;
        }else{
            return null;
        }
    }
}
