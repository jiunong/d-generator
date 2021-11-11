package com.dxh.dgenerator.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.dxh.dgenerator.IDbQuery;
import com.dxh.dgenerator.querys.DMQuery;
import com.dxh.dgenerator.querys.DbQueryRegistry;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/1/22 15:08
 */
@Slf4j
public class DataSourceConfig {
    private IDbQuery dbQuery;
    private DbType dbType;
    private String schemaName;
    private String database;
    private String url;
    private String driverName;
    private String username;
    private String password;


    public IDbQuery getDbQuery() {
        if (null == this.dbQuery) {
            DbType dbType = this.getDbType();
            DbQueryRegistry dbQueryRegistry = new DbQueryRegistry();
            this.dbQuery = Optional.ofNullable(dbQueryRegistry.getDbQuery(dbType)).orElseGet(() -> dbQueryRegistry.getDbQuery(DbType.MYSQL));
        }

        return this.dbQuery;
    }

    public DbType getDbType() {
        if (null == this.dbType) {
            log.info("Unknown type of database!{}", new Object[0]);
        }
        return this.dbType;
    }


    public Connection getConn() {
        try {
            Class.forName(this.driverName);
            Connection conn = DriverManager.getConnection(this.url, this.username, this.password);
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException | ClassNotFoundException var3) {
            throw new RuntimeException(var3);
        }
    }

    /**
     * TODO 默认
     */
    public DataSourceConfig() {
        this.setDbType(DbType.DM)
                .setDriverName("dm.jdbc.driver.DmDriver")
                .setUrl("jdbc:dm://192.168.135.37:12345/OMSPDJX?allowMultiQueries=true")
                .setUsername("OMSPDJXSY")
                .setPassword("OMSPDJXSY")
                .setDatabse("OMSPDJX")
                .setSchemaName("OMSPDJX_SY")
                .setDbQuery(new DMQuery());
    }

    public String getSchemaName() {
        return this.schemaName;
    }


    public String getUrl() {
        return this.url;
    }

    public String getDriverName() {
        return this.driverName;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getDatabase() {
        return this.database;
    }

    public DataSourceConfig setDbQuery(final IDbQuery dbQuery) {
        this.dbQuery = dbQuery;
        return this;
    }

    public DataSourceConfig setDbType(final DbType dbType) {
        this.dbType = dbType;
        return this;
    }

    public DataSourceConfig setSchemaName(final String schemaName) {
        this.schemaName = schemaName;
        return this;
    }


    public DataSourceConfig setUrl(final String url) {
        this.url = url;
        return this;
    }

    public DataSourceConfig setDriverName(final String driverName) {
        this.driverName = driverName;
        return this;
    }

    public DataSourceConfig setUsername(final String username) {
        this.username = username;
        return this;
    }

    public DataSourceConfig setPassword(final String password) {
        this.password = password;
        return this;
    }

    public DataSourceConfig setDatabse(final String database) {
        this.database = database;
        return this;
    }

    public void reFill(DataSourceParam param) {
        this.setDatabse(param.getDatabase())
                .setSchemaName(param.getSchemaName())
                .setUsername(param.getUsername())
                .setPassword(param.getPassword())
                .setUrl(param.getUrl());
    }

    private enum SingletonEnum {
        INSTANCE;
        private DataSourceConfig dataSourceConfig;

        SingletonEnum() {
            dataSourceConfig = new DataSourceConfig();
        }

        private DataSourceConfig getInstance() {
            return dataSourceConfig;
        }
    }

    public static DataSourceConfig getInstance() {
        return SingletonEnum.INSTANCE.getInstance();
    }

}
