package avatar.entity.user.thirdpart;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="web3_axc_account" , comment = "web3 axc代币账号")
public class Web3AxcAccountEntity extends BaseEntity {
    public Web3AxcAccountEntity() {
        super(Web3AxcAccountEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "user_id" , comment = "玩家ID" )
    private int userId;

    @Column(name = "account_pk" , comment = "账号公钥" )
    private String accountPk;

    @Column(name = "account_sk" , comment = "账号私钥" )
    private String accountSk;

    @Column(name = "axc_account" , comment = "axc账号" )
    private String axcAccount;

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

    public String getAccountPk() {
        return accountPk;
    }

    public void setAccountPk(String accountPk) {
        this.accountPk = accountPk;
    }

    public String getAccountSk() {
        return accountSk;
    }

    public void setAccountSk(String accountSk) {
        this.accountSk = accountSk;
    }

    public String getAxcAccount() {
        return axcAccount;
    }

    public void setAxcAccount(String axcAccount) {
        this.axcAccount = axcAccount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
