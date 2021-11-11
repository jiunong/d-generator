package com.dxh.dgenerator.service;

import cn.hutool.core.map.MapUtil;
import com.dxh.dgenerator.config.DataSourceConfig;
import com.dxh.dgenerator.models.ResultVO;
import com.dxh.dgenerator.models.TableInfo;
import com.dxh.dgenerator.querys.DecoratorDbQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/2/3 13:39
 */
public class TableService {

    private static DataSourceConfig instance = DataSourceConfig.getInstance();
    private static DecoratorDbQuery dbQuery = null;

    /**
     * TODO   获取所有table
     *
     * @param instance 数据库配置
     * @return java.util.List<com.dxh.dgenerator.models.TableInfo>
     * @author xuhong.ding
     * @since 2021/2/5 10:43
     */
    public static List<TableInfo> getTables() {
        DecoratorDbQuery dbQuery = new DecoratorDbQuery(instance);
        List<TableInfo> tableInfoList = new ArrayList<>();
        try (PreparedStatement preparedStatement = dbQuery.getConnection().prepareStatement(dbQuery.tablesSql())) {
            preparedStatement.setString(1, instance.getSchemaName());
            dbQuery.query(preparedStatement, resultSetWrapper -> {
                tableInfoList.add(TableInfo.builder()
                        .tableId(resultSetWrapper.getStringResult("ID"))
                        .tableName(resultSetWrapper.getStringResult("NAME"))
                        .comment(resultSetWrapper.getStringResult("COMMENT"))
                        .build());
            });
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return tableInfoList;
    }


    public static ResultVO select(String sql) {
        dbQuery = new DecoratorDbQuery(instance);
        List<Map<String, Object>> dataList = new ArrayList<>();
        try (PreparedStatement preparedStatement = dbQuery.getConnection().prepareStatement(sql)) {
            dbQuery.query(preparedStatement, resultSetWrapper -> {
                try {
                    ResultSetMetaData metaData = resultSetWrapper.getResultSet().getMetaData();
                    int columnCount = metaData.getColumnCount();
                    HashMap<String, Object> data = MapUtil.newHashMap();
                    for (int i = 1; i <= columnCount; i++) {
                        data.put(metaData.getColumnName(i).trim(), resultSetWrapper.getStringResult(i));
                    }
                    dataList.add(data);
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            });
        } catch (SQLException sqlException) {
            return ResultVO.fail(sqlException.getMessage());
        }
        return new ResultVO<>(dataList);
    }

}
