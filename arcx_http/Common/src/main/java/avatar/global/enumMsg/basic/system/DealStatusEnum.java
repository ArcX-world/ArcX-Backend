package avatar.global.enumMsg.basic.system;

/**
 * 处理状态枚举
 */
public enum DealStatusEnum {
    NO_DEAL(0,"未处理"),
    DEAL(1,"已处理"),
    ;

    private int code;
    private String name;

    DealStatusEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){
        return name;
    }
}
