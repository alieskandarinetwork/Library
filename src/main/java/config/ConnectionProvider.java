package config;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProvider {
    private static final BasicDataSource BASIC_DATA_SOURCE = new BasicDataSource();

    static {
        BASIC_DATA_SOURCE.setUrl
                (DbConfig.URL
                );
        BASIC_DATA_SOURCE.setUsername
                (DbConfig.USERNAME
                );
        BASIC_DATA_SOURCE.setPassword
                (DbConfig.PASSWORD
                );
        BASIC_DATA_SOURCE.setMinIdle(5);
        BASIC_DATA_SOURCE.setMaxTotal(20);
    }

    public Connection getConnection() throws SQLException {
        return BASIC_DATA_SOURCE.getConnection();
    }

}
