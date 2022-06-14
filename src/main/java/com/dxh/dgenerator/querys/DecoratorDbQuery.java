/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.dxh.dgenerator.querys;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.dxh.dgenerator.IDbQuery;
import com.dxh.dgenerator.config.DataSourceConfig;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * TODO 装饰DbQuery
 *
 * @author xuhong.ding
 * @since 2021/1/22 15:23
 */
@Slf4j
public class DecoratorDbQuery extends AbstractDbQuery {


    private final IDbQuery dbQuery;
    private final Connection connection;
    private final DbType dbType;
    private final String database;

    public DecoratorDbQuery(DataSourceConfig dataSourceConfig) {
        this.dbQuery = dataSourceConfig.getDbQuery();
        this.connection = dataSourceConfig.getConn();
        this.dbType = dataSourceConfig.getDbType();
        this.database = dataSourceConfig.getDatabase();
    }

    /**
     * 表信息查询 SQL
     */
    @Override
    public String tablesSql() {
        if (DbType.DM.equals(dbType)) {
            return dbQuery.tablesSql().replace("DATABASE", database);
        }else if (DbType.MYSQL.equals(dbType)) {
            return dbQuery.tablesSql().replace("%s", "");
        }else{
            return dbQuery.tablesSql();
        }
    }

    /**
     * 表字段信息查询 SQL
     */
    @Override
    public String tableFieldsSql() {
        if (DbType.DM.equals(dbType)) {
            return dbQuery.tableFieldsSql().replace("DATABASE", database);
        } else {
            return dbQuery.tableFieldsSql();
        }

    }

    /**
     * 表名称
     */
    @Override
    public String tableName() {
        return dbQuery.tableName();
    }

    /**
     * 表注释
     */
    @Override
    public String tableComment() {
        return dbQuery.tableComment();
    }

    /**
     * 字段名称
     */
    @Override
    public String fieldName() {
        return dbQuery.fieldName();
    }

    /**
     * 字段类型
     */
    @Override
    public String fieldType() {
        return dbQuery.fieldType();
    }

    /**
     * 字段注释
     */
    @Override
    public String fieldComment() {
        return dbQuery.fieldComment();
    }

    /**
     * 主键字段
     */
    @Override
    public String fieldKey() {
        return dbQuery.fieldKey();
    }


    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        Optional.ofNullable(connection).ifPresent((con) -> {
            try {
                con.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        });
    }

    public void execute(PreparedStatement preparedStatement, Consumer<Boolean> consumer) throws SQLException {
        consumer.accept(preparedStatement.execute());
        closeConnection();
    }

    public void query(PreparedStatement preparedStatement, Consumer<ResultSetWrapper> consumer) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                consumer.accept(new ResultSetWrapper(resultSet, this, this.dbType));
            }
        }
        closeConnection();
    }

    public void update(PreparedStatement preparedStatement, Consumer<ResultSetWrapper> consumer) throws SQLException {
        int i = preparedStatement.executeUpdate();
        consumer.accept(new ResultSetWrapper(i, this, this.dbType));
        closeConnection();
    }


    public static class ResultSetWrapper {

        private final IDbQuery dbQuery;

        private ResultSet resultSet;

        private final DbType dbType;

        private int i;

        ResultSetWrapper(ResultSet resultSet, IDbQuery dbQuery, DbType dbType) {
            this.resultSet = resultSet;
            this.dbQuery = dbQuery;
            this.dbType = dbType;
        }

        ResultSetWrapper(int i, IDbQuery dbQuery, DbType dbType) {
            this.i = i;
            this.dbQuery = dbQuery;
            this.dbType = dbType;
        }

        public ResultSet getResultSet() {
            return resultSet;
        }

        public String getStringResult(String columnLabel) {
            try {
                return resultSet.getString(columnLabel);
            } catch (SQLException sqlException) {
                throw new RuntimeException(String.format("读取[%s]字段出错!", columnLabel), sqlException);
            }
        }

        public String getStringResult(int columnIndex) {
            try {
                return resultSet.getString(columnIndex);
            } catch (SQLException sqlException) {
                throw new RuntimeException(String.format("读取[%s]字段出错!", columnIndex), sqlException);
            }
        }

        private String getComment(String columnLabel) {
            return StrUtil.isNotBlank(columnLabel) ? formatComment(getStringResult(columnLabel)) : StrUtil.EMPTY;
        }

        public String getTableComment() {
            return getComment(dbQuery.tableComment());
        }

        public String formatComment(String comment) {
            return StrUtil.isBlank(comment) ? StrUtil.EMPTY : comment.replaceAll("\r\n", "\t");
        }

    }
}
