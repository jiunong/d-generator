package com.dxh.dgenerator.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.dxh.dgenerator.IDbQuery;
import lombok.Builder;
import lombok.Data;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/1/29 14:25
 */
@Data
public class DataSourceParam {
     private IDbQuery dbQuery;
     private DbType dbType;
     private String schemaName;
     private String database;
     private String url;
     private String driverName;
     private String username;
     private String password;
     private String description;
}
