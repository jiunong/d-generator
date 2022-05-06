package com.dxh.dgenerator.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.XmlUtil;
import com.dxh.dgenerator.config.ConstVal;
import com.dxh.dgenerator.config.DataSourceParam;
import com.dxh.dgenerator.rules.MethodType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/2/3 9:47
 */
public class DatabaseXmlService {

    private final static String NODE_NAME = "DataSourceParam";
    private final static String DESCRIPTION = "description";
    private final static String URL = "url";
    private final static String DATABASE = "database";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String SCHEMA_NAME = "schemaName";


    private static final Resource resource = new ClassPathResource("database.xml");

    private static final String DATABASE_XML_PATH = ConstVal.PROJECT_PATH+"conf/database.xml";

    public static List<DataSourceParam> getDBs() {

        List<DataSourceParam> list = ListUtil.list(false);
        try {
            Document document = XmlUtil.readXML(FileUtil.getInputStream(DATABASE_XML_PATH));
            XmlUtil.getElements(XmlUtil.getRootElement(document), NODE_NAME).forEach(u -> {
                list.add(XmlUtil.xmlToBean(u, DataSourceParam.class));
            });
        } catch (Exception e) {
            DataSourceParam dataSourceParam = new DataSourceParam();
            String s = "请检查" + DATABASE_XML_PATH + "文件是否存在";
            dataSourceParam.setDescription(s);
            dataSourceParam.setDriverName(s);
            dataSourceParam.setUrl(s);
            dataSourceParam.setDatabase(s);
            dataSourceParam.setSchemaName(s);
            dataSourceParam.setUsername(s);
            dataSourceParam.setPassword(s);
            list.add(dataSourceParam);
        }

        return list;
    }


    public static void modify(DataSourceParam dataSourceParam, String methodType) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilder documentBuilder = XmlUtil.createDocumentBuilderFactory().newDocumentBuilder();
        Document parse = documentBuilder.parse(resource.getInputStream());
        switch (methodType) {
            case MethodType.ADD:
                add(parse, dataSourceParam);
                break;
            case MethodType.REMOVE:
                remove(parse, dataSourceParam);
                break;
            default:
                update(parse, dataSourceParam);
                break;
        }
    }

    private static void add(Document document, DataSourceParam add) {
        Element rootElement = XmlUtil.getRootElement(document);
        Element element = XmlUtil.appendChild(rootElement, NODE_NAME);
        XmlUtil.appendChild(element, DESCRIPTION).setTextContent(add.getDescription());
        XmlUtil.appendChild(element, URL).setTextContent(add.getUrl());
        XmlUtil.appendChild(element, USERNAME).setTextContent(add.getUsername());
        XmlUtil.appendChild(element, PASSWORD).setTextContent(add.getPassword());
        XmlUtil.appendChild(element, DATABASE).setTextContent(add.getDatabase());
        XmlUtil.appendChild(element, SCHEMA_NAME).setTextContent(add.getSchemaName());

        XmlUtil.toFile(document, DATABASE_XML_PATH, ConstVal.UTF8);
    }

    private static void remove(Document document, DataSourceParam remove) {
        XmlUtil.getElement(XmlUtil.getElements(XmlUtil.getRootElement(document), NODE_NAME).get(0), SCHEMA_NAME).getTextContent();
        List<Element> dbs = XmlUtil.getElements(XmlUtil.getRootElement(document), NODE_NAME);
        dbs.forEach(u -> {
            if (XmlUtil.getElement(u, SCHEMA_NAME).getTextContent().equals(remove.getSchemaName())) {
                u.getParentNode().removeChild(u);
            }
        });
        XmlUtil.toFile(document, DATABASE_XML_PATH, ConstVal.UTF8);
    }


    private static void update(Document document, DataSourceParam update) {
        remove(document, update);
        add(document, update);
    }


}
