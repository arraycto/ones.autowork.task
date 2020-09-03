package ones.autowork.util.postgresql;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by liYueYang on 2020/6/11.
 * 配置文件读取工具类
 */
public class PropertiesUtil {

//    private static Resource resource;
//
//    static {
//        resource = new ClassPathResource("application.properties");
//    }
//
//    /**
//     * @param key 读取
//     * @return
//     * @throws Exception
//     */
//    public static String getParam(String key) {
//        InputStream is = null;
//        Properties properties = new Properties();
//        try {
//            is = resource.getInputStream();
//            properties.load(is);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return properties.getProperty(key);
//    }

    private static ResourceBundle bundle;

    static {
        bundle = ResourceBundle.getBundle("application");
    }

    /**
     * @param key 读取
     * @return
     * @throws Exception
     */
    public static String getParam(String key) {
        return bundle.getString(key);
    }

    // TODO 打包使用这种方式，测试环境使用上面的方式
//    public static String getParam(String key) {
//        Properties properties = PropertiesUtil.getProperties("application.properties");
//        String value = properties.getProperty(key);
//        return value;
//    }

    public static Properties getProperties(String fileName) {
        try {
            String outpath = System.getProperty("user.dir") + File.separator + "config" + File.separator;//先读取config目录的，没有再加载classpath的
            Properties properties = new Properties();
            InputStream in = new FileInputStream(new File(outpath + fileName));
            properties.load(in);
            return properties;
        } catch (IOException e) {
            try {
                Properties properties = new Properties();
                InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);//默认加载classpath的
                properties.load(in);
                return properties;
            } catch (IOException es) {
                return null;
            }
        }
    }
}
