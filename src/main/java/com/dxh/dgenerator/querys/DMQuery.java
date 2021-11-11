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

/**
 * DM 表数据查询
 *
 * @author halower
 * @since 2019-06-27
 */
public class DMQuery extends AbstractDbQuery {

    @Override
    public String tablesSql() {
        return "SELECT ID,NAME,RESVD5 COMMENT  FROM  DATABASE.SYSDBA.SYSTABLES WHERE TYPE = 'U' AND SCHID=(SELECT SCHID FROM DATABASE.SYSDBA.SYSSCHEMAS WHERE NAME = ? AND ROWNUM = 1) " +
            "ORDER BY SUBSTR(NAME,0,1) ASC";
    }

    @Override
    public String tableFieldsSql() {
        return  "SELECT * FROM  DATABASE.SYSDBA.SYSCOLUMNS WHERE ID = ?";
    }

    @Override
    public String tableName() {
        return "TABLE_NAME";
    }
    @Override
    public String tableComment() {
        return "TABLE_COMMENT";
    }

    @Override
    public String fieldName() {
        return "COLUMN_NAME";
    }

    @Override
    public String fieldType() {
        return "DATA_TYPE";
    }

    @Override
    public String fieldComment() {
        return "COMMENTS";
    }

    @Override
    public String fieldKey() {
        return "KEY";
    }
}
