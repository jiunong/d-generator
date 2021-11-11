package com.dxh.dgenerator.controller;

import com.dxh.dgenerator.config.DataSourceParam;
import com.dxh.dgenerator.models.ResultVO;
import com.dxh.dgenerator.rules.MethodType;
import com.dxh.dgenerator.service.DatabaseXmlService;
import com.dxh.dgenerator.utils.LogsHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/2/5 13:37
 */
@RestController
public class DataSourceController {


    @PostMapping("datasource/update")
    private ResultVO<String> modify(DataSourceParam dataSourceParam, HttpServletRequest request) throws Exception {

        DatabaseXmlService.modify(dataSourceParam, MethodType.UPDATE);

        LogsHandler.instance(request).logs("修改{}成功", dataSourceParam.getDescription());

        return new ResultVO<>("修改".concat(dataSourceParam.getDescription()).concat("成功"));
    }


}
