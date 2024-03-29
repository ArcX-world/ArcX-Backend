package avatar.entity.product.instruct;

import avatar.util.BaseEntity;
import avatar.util.utilDB.annotation.Column;
import avatar.util.utilDB.annotation.Pk;
import avatar.util.utilDB.annotation.Table;
import org.springframework.stereotype.Service;

@Service
@Table(name="instruct_push_coin" , comment = "推币机操作指令")
public class InstructPushCoinEntity extends BaseEntity {
    public InstructPushCoinEntity() {
        super(InstructPushCoinEntity.class);
    }

    @Pk
    @Column(name = "id" , comment = "id" )
    private int id;

    @Column(name = "name" , comment = "名称")
    private String name;

    @Column(name = "ins_desc" , comment = "描述")
    private String insDesc;

    @Column(name = "instruct" , comment = "指令")
    private String instruct;

    @Column(name = "create_time" , comment = "创建时间")
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInsDesc() {
        return insDesc;
    }

    public void setInsDesc(String insDesc) {
        this.insDesc = insDesc;
    }

    public String getInstruct() {
        return instruct;
    }

    public void setInstruct(String instruct) {
        this.instruct = instruct;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
