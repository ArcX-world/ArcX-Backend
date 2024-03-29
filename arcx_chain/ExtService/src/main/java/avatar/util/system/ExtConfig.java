package avatar.util.system;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 大厅配置文件信息
 */
public class ExtConfig {
    private static ExtConfig config=new ExtConfig();
    public static ExtConfig getInstance(){return config;}
    private static final Properties p=new Properties();
    //本机公网IP
    public static String localPublicIp;
    public static void init() {
        InputStream in = null;
        in = ExtConfig.class.getClassLoader().getResourceAsStream("extConfig.properties");
        try {
            p.load(in);
            localPublicIp = p.getProperty("localPublicIp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
