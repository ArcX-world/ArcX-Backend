package avatar.entity.basic.ip;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="ip_address" , comment = "ip对应地址")
public class IpAddressEntity extends BaseEntity {
    public IpAddressEntity() {
        super(IpAddressEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "ip" , comment = "ip")
    private String ip;

    @Column(name = "en_short" , comment = "英文简称")
    private String enShort;

    @Column(name = "en_name" , comment = "英文名字")
    private String enName;

    @Column(name = "global" , comment = "洲际")
    private String global;

    @Column(name = "nation" , comment = "国家")
    private String nation;

    @Column(name = "province" , comment = "省份")
    private String province;

    @Column(name = "city" , comment = "城市")
    private String city;

    @Column(name = "adcode" , comment = "地区码")
    private String adcode;

    @Column(name = "lon" , comment = "经度")
    private double lon;

    @Column(name = "lat" , comment = "纬度")
    private double lat;

    @Column(name = "create_time" , comment = "创建时间")
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEnShort() {
        return enShort;
    }

    public void setEnShort(String enShort) {
        this.enShort = enShort;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getGlobal() {
        return global;
    }

    public void setGlobal(String global) {
        this.global = global;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
