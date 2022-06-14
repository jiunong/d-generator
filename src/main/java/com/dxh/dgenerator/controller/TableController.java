package com.dxh.dgenerator.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.dxh.dgenerator.config.ConstVal;
import com.dxh.dgenerator.config.DataSourceConfig;
import com.dxh.dgenerator.config.ExcelFillCellMergeStrategy;
import com.dxh.dgenerator.models.ExportTableModel;
import com.dxh.dgenerator.models.ResultCode;
import com.dxh.dgenerator.models.ResultVO;
import com.dxh.dgenerator.models.TableFiled;
import com.dxh.dgenerator.querys.DecoratorDbQuery;
import com.dxh.dgenerator.service.TableService;
import com.dxh.dgenerator.utils.LogsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
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
    private final static String COMMENT = "COMMENT";
    private final static String COUNT = "COUNT";
    private static final String EXPORT_PATH = ConstVal.PROJECT_PATH.concat("/export/");

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
        return new ResultVO<>(TableService.getTableFiled(tableId));
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
            dbQuery.execute(preparedStatement, u -> {
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
    public ResultVO<List> execute(String sql, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int showData, HttpServletRequest request) {
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
            vo =  TableService.update(sql);
        } else if (trimSql.toUpperCase().startsWith(DELETE)) {
            vo = null;
        } else if (trimSql.toUpperCase().startsWith(COMMENT)) {
            List<String> list = ListUtil.list(false);
            Arrays.stream(sql.split(";")).forEach(u->{
                TableService.update(u);
                list.add(u);
            });
            vo = new ResultVO<>(list);
        }
        return vo;
    }

    /**
     * TODO 测试用 找FEC665FD88的数据
     *
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
            if (data instanceof String) {
                // list.add(u.getTableName());
            } else {
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
     *
     * @param sql      :
     * @param page     :
     * @param showData :
     * @return java.lang.String
     * @author xuhong.ding
     * @since 2021/11/18 13:40
     **/
    private String pageDecorate(String sql, int page, int showData) {
        return "select * from (select *,rownum ind from (   ".concat(sql).concat(" )) where ind between ").concat((page - 1) * showData + 1 + "").concat(" and ").concat(page * showData + "");
    }

    private String countSelectDecorate(String sql) {
        return "select count(*) count from ( ".concat(sql).concat(" )");
    }


    /**
     * TODO 以Excel格式导出数据库，返回给前端
     *
     * @return
     * @author xuhong.ding
     * @since 2022/5/5 10:14
     **/
    @GetMapping("data/export/{tableId}")
    private void exportTables(@PathVariable String tableId, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        //如果EXPORT_PATH不存在，则创建
        FileUtil.mkdir(EXPORT_PATH);
        String filename = EXPORT_PATH + instance.getSchemaName() + DateUtil.current() + ".xlsx";
        String encode = URLEncoder.encode(filename, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + encode);
        EasyExcel.write(response.getOutputStream(), ExportTableModel.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerWriteHandler(new ExcelFillCellMergeStrategy(1, new int[]{0, 1}))
                .sheet(tableId).doWrite(() -> {
                    return TableService.getTableDataForDownload(tableId);
                });
    }

    @GetMapping("comment/export/{tableId}")
    private void exportTablesComment(@PathVariable String tableId, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String filename = instance.getSchemaName() + ConstVal.COMMENT + DateUtil.current() + ".sql";
        File file = FileUtil.file(filename);
        FileUtil.writeLines(TableService.getTableCommentForDownload(tableId), file, StandardCharsets.UTF_8);
        String encode = URLEncoder.encode(filename, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + encode);
        //生成文件并返回前端
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(FileUtil.readBytes(file));
        outputStream.flush();
        outputStream.close();
    }


}
