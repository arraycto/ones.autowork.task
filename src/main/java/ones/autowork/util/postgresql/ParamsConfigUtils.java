package ones.autowork.util.postgresql;

/**
 * Created by liYueYang on 2020/6/11.
 * 参数读取工具类
 */
public class ParamsConfigUtils {
    // pg数据库
    public static final String pgDriver = PropertiesUtil.getParam("PGDRIVER");
    public static final String pgUrl = PropertiesUtil.getParam("PGURL");
    public static final String pgUserName = PropertiesUtil.getParam("PGUSERNAME");
    public static final String pgPassWord = PropertiesUtil.getParam("PGPASSWORD");
}
