package avatar.global.enumMsg.system;

/**
 * 枚举：时间范围
 */
public enum SearchTimeEnum {
    ALL(0,"全部", 0),
    ONE_MONTH(1,"一个月", 1),
    THREE_MONTH(2,"三个月", 3),
    HALF_YEAR(3, "近半年", 6),
    ;

    private int code;
    private String name;
    private int month;

    SearchTimeEnum(int code, String name, int month){
        this.code = code;
        this.name = name;
        this.month = month;
    }

    public int getCode(){
        return code;
    }

    public int getMonth(){return month;}
}
