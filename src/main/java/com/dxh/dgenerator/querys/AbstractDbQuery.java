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
import cn.hutool.db.meta.Column;
import com.dxh.dgenerator.IDbQuery;
import com.dxh.dgenerator.models.TableFiled;

import java.util.List;
import java.util.Optional;

/**
 * 表数据查询抽象类
 *
 * @author Xuhong.Ding
 * @since 2018-01-16
 */
public abstract class AbstractDbQuery implements IDbQuery {

    private static final String TABLE_NAME = "TABLE_NAME";
    private static final String TABLE_COLUMN = "TABLE_COLUMN";
    private static final String TABLE_COLUMN_NEW = "TABLE_COLUMN_NEW";
    private static final String COMMENT = "COMMENT";

    public String renameColumn(String tableName, String tableColumn, String tableColumnNew) {
        return "alter table TABLE_NAME rename column TABLE_COLUMN to TABLE_COLUMN_NEW"
                .replace(TABLE_NAME, tableName)
                .replace(TABLE_COLUMN_NEW, tableColumnNew)
                .replace(TABLE_COLUMN, tableColumn);
    }

    /**
     * TODO
     *
     * @param tableName 表
     * @param comment   备注
     * @return java.lang.String
     * @author xuhong.ding
     * @since 2021/2/2 11:08
     */
    public String commentOnTable(String tableName, String comment) {
        return "comment on table TABLE_NAME is 'COMMENT'"
                .replace(TABLE_NAME, tableName)
                .replace(COMMENT, Optional.ofNullable(comment).orElse(""));
    }

    /**
     * TODO
     *
     * @param tableName   表
     * @param tableColumn 列
     * @param comment     备注
     * @return java.lang.String
     * @author xuhong.ding
     * @since 2021/2/2 11:08
     */
    public String commentOnColumn(String tableName, String tableColumn, String comment) {
        return "comment on column TABLE_NAME.TABLE_COLUMN is 'COMMENT'"
                .replace(TABLE_NAME, Optional.ofNullable(tableName).orElse(""))
                .replace(TABLE_COLUMN, tableColumn)
                .replace(COMMENT, Optional.ofNullable(comment).orElse(""));
    }


    public String creatTable(String tableName, String tableColumn, String comment) {
        String a = "create table TABLE_NAME\n" +
                "(\n" +
                "    COLUMN_NAME varchar(50) default 'defau3123' not null  constraint TABLE_NAME_PK primary key\n" +
                "\n" +
                ");\n" +
                "comment on table TABLE_NAME_1 is '222';\n" +
                "comment on column TABLE_NAME_1.COLUMN_NAME is '222';";
        return "create table TABLE_NAME (TABLE_COLUMN)"
                .replace(TABLE_NAME, tableName)
                .replace(TABLE_COLUMN, tableColumn)
                .replace(COMMENT, Optional.ofNullable(comment).orElse(""));
    }

    public String dropTable(String tableName, List<TableFiled> columns) {
        String column = createColumn(columns, tableName);
        return "drop table TABLE_NAME".replace(TABLE_NAME, tableName);
    }

    public String createColumn(List<TableFiled> columns, String tableName) {
        StringBuilder sb = new StringBuilder();
        String base = "COLUMN_NAME VARCHAR(LENGTH) default 'DEFAULT_VALUE ' NOT_NULL  PRIMARY_KEY";
        columns.forEach(column -> {
            sb.append(base.replace("COLUMN_NAME", column.getName())
                    .replace("VARCHAR", column.getType())
                    .replace("LENGTH", column.getLength())
                    .replace("DEFAULT_VALUE", StrUtil.isEmpty(column.getDefaultValue()) ? "" : column.getDefaultValue())
                    .replace("NOT_NULL", Boolean.getBoolean(column.getNullAble()) ? "" : "NOT NULL")
                    .replace("PRIMARY_KEY", column.isKeyFlag()
                            ? "CONSTRAINT TABLE_NAME_PK PRIMARY KEY".replace("TABLE_NAME", tableName) : "")
                    .concat(",\n"));
        });
        //删除最后一个逗号
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
