package com.dxh.dgenerator.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import com.dxh.dgenerator.config.DataSourceConfig;
import com.dxh.dgenerator.models.ExportTableModel;
import com.dxh.dgenerator.models.ResultVO;
import com.dxh.dgenerator.models.TableFiled;
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

    public static List<TableFiled> getTableFiled(String tableId) {
        dbQuery = new DecoratorDbQuery(instance);
        List<TableFiled> tableFileds = new ArrayList<>();
        try (PreparedStatement preparedStatement = dbQuery.getConnection().prepareStatement(dbQuery.tableFieldsSql())) {
            preparedStatement.setString(1, tableId);
            dbQuery.query(preparedStatement, u -> {
                tableFileds.add(TableFiled.builder()
                        .name(u.getStringResult("NAME"))
                        .type(u.getStringResult("TYPE"))
                        .length(u.getStringResult("LENGTH"))
                        .nullAble(u.getStringResult("NULLABLE"))
                        .comment(u.getStringResult("RESVD5"))
                        .build());
            });
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return tableFileds;
    }

    public static List<ExportTableModel> getTableDataForDownload(String tableId){
        List<ExportTableModel> list = ListUtil.list(false);
        List<TableInfo> tables = getTables();
        tables.forEach(u->{
            List<TableFiled> tableFiled = getTableFiled(u.getTableId());
            tableFiled.forEach(v->{
                ExportTableModel eto = new ExportTableModel();
                eto.setTableName(u.getTableName());
                eto.setTableComment(u.getComment());
                eto.setColumnName(v.getName());
                eto.setColumnType(v.getType());
                eto.setLength(v.getLength());
                eto.setColumnComment(v.getComment());
                eto.setNullAble(v.getNullAble());
                list.add(eto);
            });
        });
        return list;
    }


}
