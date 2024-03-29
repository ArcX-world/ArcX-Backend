package avatar.global.enumMsg.product.info;

/**
 * 抓娃娃结果枚举
 */
public enum CatchDollResultEnum {
    NULL(0,"空","0000"),
    LOSE(1,"没抓到","0600"),
    WIN(2,"抓到","0601")
    ;

    private int code;//操作码code
    private String name;//名字
    private String instruct;//指令

    CatchDollResultEnum(int code, String name, String instruct){
        this.code = code;
        this.name = name;
        this.instruct = instruct;
    }

    public int getCode(){
        return code;
    }

    public String getInstruct(){
        return instruct;
    }
}
