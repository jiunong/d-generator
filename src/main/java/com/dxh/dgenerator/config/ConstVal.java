package com.dxh.dgenerator.config;

import java.nio.charset.StandardCharsets;

/**
 * TODO 常量接口
 *
 * @author xuhong.ding
 * @since 2021/1/22 9:49
 */
public interface ConstVal {

    String PROJECT_PATH = System.getProperty("user.dir").concat("/");

    String UTF8 = StandardCharsets.UTF_8.name();

    String TABLE = "TABLE";
    String COLUMN = "COLUMN";
    String COMMENT = "COMMENT";


}
