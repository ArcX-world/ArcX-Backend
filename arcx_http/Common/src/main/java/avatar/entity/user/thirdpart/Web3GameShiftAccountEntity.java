package avatar.entity.user.thirdpart;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="web3_game_shift_account" , comment = "web3 gameShift账号")
public class Web3GameShiftAccountEntity extends BaseEntity {
    public Web3GameShiftAccountEntity() {
        super(Web3GameShiftAccountEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "wallet" , comment = "gameshift钱包" )
    private String wallet;

    @Column(name = "axc_account" , comment = "axc代币账号" )
    private String axcAccount;

    @Column(name = "usdt_account" , comment = "usdt代币账号" )
    private String usdtAccount;

    @Column(name = "create_time" , comment = "创建时间" )
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getAxcAccount() {
        return axcAccount;
    }

    public void setAxcAccount(String axcAccount) {
        this.axcAccount = axcAccount;
    }

    public String getUsdtAccount() {
        return usdtAccount;
    }

    public void setUsdtAccount(String usdtAccount) {
        this.usdtAccount = usdtAccount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
