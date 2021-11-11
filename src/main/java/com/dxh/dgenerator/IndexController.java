package com.dxh.dgenerator;

import cn.hutool.Hutool;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.XML;
import com.dxh.dgenerator.config.DataSourceConfig;
import com.dxh.dgenerator.config.DataSourceParam;
import com.dxh.dgenerator.models.TableInfo;
import com.dxh.dgenerator.querys.DecoratorDbQuery;
import com.dxh.dgenerator.service.DatabaseXmlService;
import com.dxh.dgenerator.service.TableService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/1/22 10:16
 */
@Controller
public class IndexController {

    private static DataSourceConfig instance = DataSourceConfig.getInstance();


    @RequestMapping("/toIndexP")
    public String index(ModelMap model, DataSourceParam dataSourceParam) throws SQLException {
        instance.reFill(dataSourceParam);
        model.addAttribute("data", TableService.getTables());
        return "index";
    }

    @RequestMapping("/init")
    public String tables(ModelMap model) throws IOException {
        List<DataSourceParam> dbs = DatabaseXmlService.getDBs();
        model.addAttribute("dbs", dbs);
        return "init";
    }

    @RequestMapping("/sql")
    public String sql(ModelMap model) {
        return "sql";
    }
}
