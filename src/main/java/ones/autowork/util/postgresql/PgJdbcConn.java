package ones.autowork.util.postgresql;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by liYueYang on 2020/6/11.
 * pg数据库连接工具类
 */
@Component
public class PgJdbcConn {

    public static Connection getConn() {
        Connection c = null;
        try {
            Class.forName(ParamsConfigUtils.pgDriver);
            c = DriverManager.getConnection(ParamsConfigUtils.pgUrl, ParamsConfigUtils.pgUserName, ParamsConfigUtils.pgPassWord);
            c.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }
}
