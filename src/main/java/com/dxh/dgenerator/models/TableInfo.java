package com.dxh.dgenerator.models;

import lombok.Builder;
import lombok.Data;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/1/26 9:58
 */
@Data
@Builder
public class TableInfo {
    private String tableId;
    private String tableName;
    private String comment;
}
