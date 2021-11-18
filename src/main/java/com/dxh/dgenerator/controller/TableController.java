package com.dxh.dgenerator.controller;

import cn.hutool.core.collection.ListUtil;
import com.dxh.dgenerator.config.DataSourceConfig;
import com.dxh.dgenerator.models.ResultCode;
import com.dxh.dgenerator.models.ResultVO;
import com.dxh.dgenerator.models.TableFiled;
import com.dxh.dgenerator.querys.DecoratorDbQuery;
import com.dxh.dgenerator.service.TableService;
import com.dxh.dgenerator.utils.LogsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/1/22 11:06
 */
@RestController
@Slf4j
public class TableController {

    private static DataSourceConfig instance = DataSourceConfig.getInstance();
    private static DecoratorDbQuery dbQuery = null;


    private final static String SELECT = "SELECT";
    private final static String UPDATE = "UPDATE";
    private final static String DELETE = "DELETE";
    private final static String INSERT = "INSERT";
    private final static String COUNT = "COUNT";

    /**
     * TODO
     * 获取指定表内容
     *
     * @param tableName
     * @return com.dxh.dgenerator.models.ResultVO<java.util.List < com.dxh.dgenerator.models.TableFiled>>
     * @author xuhong.ding
     * @since 2021/1/28 10:00
     */
    @GetMapping("data/tables/{tableId}")
    public ResultVO<List<TableFiled>> table(@PathVariable String tableId) {
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
        return new ResultVO<>(tableFileds);
    }

    @PostMapping("opreation/commentOnColumn")
    public void commentOnColumn(String tableName, String tableColumn, String comment, HttpServletRequest request) {
        dbQuery = new DecoratorDbQuery(instance);
        String sql = dbQuery.commentOnColumn(tableName, tableColumn, comment);
        LogsHandler.instance(request).logs("将执行sql语句：{}", sql);
        log.info("将执行sql语句：{}", sql);
        try (PreparedStatement preparedStatement = dbQuery.getConnection().prepareStatement(sql)) {
            dbQuery.execute(preparedStatement, u -> {
                LogsHandler.instance(request).logs("修改表{}列{}注释为：{}", tableName, tableColumn, comment);
                log.info("修改表{}列{}注释为：{}", tableName, tableColumn, comment);
            });
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }


    @PostMapping("opreation/commentOnTable")
    public void commentOnTable(String tableName, String comment, HttpServletRequest request) {
        dbQuery = new DecoratorDbQuery(instance);
        String sql = dbQuery.commentOnTable(tableName, comment);
        LogsHandler.instance(request).logs("将执行sql语句：{}", sql);
        log.info("将执行sql语句：{}", sql);
        try (PreparedStatement preparedStatement = dbQuery.getConnection().prepareStatement(sql)) {
            dbQuery.execute(preparedStatement, u ->
            {
                LogsHandler.instance(request).logs("修改表{}注释为：{}", tableName, comment);
                log.info("修改表{}注释为：{}", tableName, comment);
            });
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @GetMapping("data/tables")
    public ResultVO<List<String>> getTables() {
        List<String> list = ListUtil.list(false);
        TableService.getTables().forEach(u -> list.add(u.getTableName()));
        return new ResultVO<>(list);
    }

    @GetMapping("sql/execute")
    public ResultVO<List> select(String sql, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int showData, HttpServletRequest request) {
        ResultVO vo = null;
        String trimSql = sql.trim();
        if (trimSql.toUpperCase().startsWith(SELECT)) {
            vo = TableService.select(this.pageDecorate(trimSql, page, showData));
            if (vo.getCode() == ResultCode.SUCCESS.getCode()) {
                ResultVO total = TableService.select(this.countSelectDecorate(sql));
                ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) total.getData();
                vo.setCode(Integer.parseInt(data.get(0).get(COUNT).toString()));
                vo.setMsg(trimSql);
            }
        } else if (trimSql.toUpperCase().startsWith(INSERT)) {
            vo = null;
        } else if (trimSql.toUpperCase().startsWith(UPDATE)) {
            vo = null;
        } else if (trimSql.toUpperCase().startsWith(DELETE)) {
            vo = null;
        }
        return vo;
    }

    /**
    * TODO 测试用 找FEC665FD88的数据
    * @param  
    * @return com.dxh.dgenerator.models.ResultVO 
    * @author xuhong.ding 
    * @since 2021/11/18 13:40 
    **/
    
    @GetMapping("data/find")
    public ResultVO find() {
        List<String> list = ListUtil.list(false);
        TableService.getTables().forEach(u -> {
            dbQuery = new DecoratorDbQuery(instance);
            StringBuilder sql = new StringBuilder("select * from ".concat(u.getTableName()).concat(" where "));
            String tableId = u.getTableId();
            List<String> tableFileds = new ArrayList<>();
            try (PreparedStatement preparedStatement = dbQuery.getConnection().prepareStatement(dbQuery.tableFieldsSql())) {
                preparedStatement.setString(1, tableId);
                dbQuery.query(preparedStatement, u1 -> {
                    tableFileds.add(u1.getStringResult("NAME"));
                    sql.append(" " + u1.getStringResult("NAME").concat("='FEC665FD88' or"));
                });
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            StringBuilder delete = sql.delete(sql.length() - 2, sql.length());
            Object data = TableService.select(sql.toString()).getData();
            if (data instanceof String){
               // list.add(u.getTableName());
            }else {
                ArrayList data1 = (ArrayList) data;
                if (data1.size() > 0) {
                    list.add(u.getTableName());
                }
            }
        });

        return new ResultVO<>(list);
    }


    /**
    * TODO 分页sql
    * @param sql :
    * @param page :
    * @param showData :
    * @return java.lang.String
    * @author xuhong.ding
    * @since 2021/11/18 13:40
    **/
    private String pageDecorate(String sql, int page, int showData) {
        return "select * from (select *,rownum ind from (   "
                .concat(sql)
                .concat(" )) where ind between ")
                .concat((page - 1) * showData + 1 + "")
                .concat(" and ")
                .concat(page * showData + "");
    }

    private String countSelectDecorate(String sql) {
        return "select count(*) count from ( "
                .concat(sql)
                .concat(" )");
    }


}
