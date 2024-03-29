package avatar.global.enumMsg.basic.system;

/**
 * 服务器类型
 */
public enum ServerTypeEnum {
    LOCAL(1, "本地"),
    TEST(2, "测试"),
    ONLINE(3,"线上");

    private int code;

    private String name;

    ServerTypeEnum(int code, String name) {
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
