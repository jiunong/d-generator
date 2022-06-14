package com.dxh.dgenerator.models;

import com.dxh.dgenerator.rules.IColumnType;
import lombok.Builder;
import lombok.Data;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/1/28 9:54
 */
@Data
@Builder
public class TableFiled {

    /**
    * 是否主键
    */
    private boolean keyFlag;
    private String nullAble;
    private String name;
    private String type;
    private String length;
    private IColumnType columnType;
    private String comment;
    private String defaultValue;


}
