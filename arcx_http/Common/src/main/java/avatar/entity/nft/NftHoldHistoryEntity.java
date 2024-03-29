package avatar.entity.nft;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="nft_hold_history" , comment = "NFT持有记录")
public class NftHoldHistoryEntity extends BaseEntity {
    public NftHoldHistoryEntity() {
        super(NftHoldHistoryEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "nft_type" , comment = "NFT类型" )
    private int nftType;

    @Column(name = "nft_code" , comment = "NFT编号")
    private String nftCode;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "create_time" , comment = "创建时间")
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNftType() {
        return nftType;
    }

    public void setNftType(int nftType) {
        this.nftType = nftType;
    }

    public String getNftCode() {
        return nftCode;
    }

    public void setNftCode(String nftCode) {
        this.nftCode = nftCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
