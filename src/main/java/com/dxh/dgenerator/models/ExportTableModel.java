package com.dxh.dgenerator.models;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.util.Date;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2022/5/5 10:05
 */

@Data
@EqualsAndHashCode
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,verticalAlignment = VerticalAlignmentEnum.CENTER)
public class ExportTableModel {

    @ExcelProperty("表名")
    private String tableName;

    @ExcelProperty("表注释")
    private String tableComment;

    @ExcelProperty("字段名字")
    private String columnName;

    @ExcelProperty("字段注释")
    private String columnComment;

    @ExcelProperty("是否主键")
    private boolean keyFlag;

    @ExcelProperty("是否可以为空")
    private String nullAble;

    @ExcelProperty("字段类型")
    private String columnType;

    @ExcelProperty("字段长度")
    private String length;


}
