package avatar.entity.crossServer;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="cross_server_media_prefix" , comment = "跨服多媒体信息")
public class CrossServerMediaPrefixEntity extends BaseEntity {
    public CrossServerMediaPrefixEntity() {
        super(CrossServerMediaPrefixEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "玩家id" )
    private int id;

    @Column(name = "server_type" , comment = "服务端类型" )
    private int serverType;

    @Column(name = "media_prefix" , comment = "前缀路径")
    private String mediaPrefix;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public String getMediaPrefix() {
        return mediaPrefix;
    }

    public void setMediaPrefix(String mediaPrefix) {
        this.mediaPrefix = mediaPrefix;
    }
}
