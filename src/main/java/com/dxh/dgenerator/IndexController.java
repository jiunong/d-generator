package com.dxh.dgenerator;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dxh.dgenerator.config.DataSourceConfig;
import com.dxh.dgenerator.config.DataSourceParam;
import com.dxh.dgenerator.models.Byq;
import com.dxh.dgenerator.service.DatabaseXmlService;
import com.dxh.dgenerator.service.TableService;
import com.dxh.dgenerator.utils.OkHttpUtil;
import com.sict.elsearch.IndexTypeEnum;
import com.sict.elsearch.tool.SictSearchDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/1/22 10:16
 */
@Controller
public class IndexController {

    private static DataSourceConfig instance = DataSourceConfig.getInstance();


    @Autowired
    private SictSearchDocumentService sictSearchDocument;


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

    @GetMapping("/search/{data}")
    @ResponseBody()
    public String sql(@PathVariable("data") String data) throws IOException {
        List<Byq> lists = sictSearchDocument.indexType(IndexTypeEnum.EQUIPMENT_INDEX).queryData(data).searchDoc(Byq.class);
        List<Object> lists2 = sictSearchDocument.indexType(IndexTypeEnum.EQUIPMENT_INDEX).queryData(data).searchDoc(Object.class);
        System.out.println();
        return "sql";
    }

    @GetMapping("/diff")
    public String diff() {
        return "diff";
    }

    @PostMapping("/diffData")
    @ResponseBody
    public String diffData(String text1, String text2, String token) {
        JSONObject res = new JSONObject();
        res.put("text1", text1);
        res.put("text2", text2);
        res.put("token", token);
       return OkHttpUtil.post("http://192.168.139.4:10087/api/compare/v1.0/tasks", res);
    }


}
