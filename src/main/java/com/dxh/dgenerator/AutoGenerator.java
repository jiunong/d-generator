package com.dxh.dgenerator;

import cn.hutool.core.io.FileUtil;
import com.dxh.dgenerator.config.ConfigBuilder;
import com.dxh.dgenerator.config.ConstVal;
import com.dxh.dgenerator.engine.AbstractTemplateEngine;
import com.dxh.dgenerator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/1/22 9:55
 */
public class AutoGenerator {


    private AbstractTemplateEngine templateEngine;


    public AutoGenerator setTemplateEngine(AbstractTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        return this;
    }

    /**
     * TODO 执行
     *
     * @author xuhong.ding
     * @since 2021/1/22 10:00
     */
    public void excute() {
        if (null == templateEngine) {
            // 为了兼容之前逻辑，采用 Velocity 引擎 【 默认 】
            templateEngine = new FreemarkerTemplateEngine();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("test1", "test1");
        data.put("test", false);
        ConfigBuilder configBuilder = new ConfigBuilder();
        File touch = FileUtil.touch(ConstVal.PROJECT_PATH.concat("test/test.jsp"));
        try {
            templateEngine.init(configBuilder).writer(data, ConstVal.TEMPLATE_JSP_LIST, touch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
