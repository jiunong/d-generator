package com.dxh.dgenerator;

import com.dxh.dgenerator.utils.BrowserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DGeneratorApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(DGeneratorApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.warn("打开浏览器输入网址：{} 即可访问本锤子系统","http://localhost:9527/init");
        logger.error("{}:注意注意！！！本系统支持最大并发量为1！！！！谨慎使用！！！出问题概不负责！！！","xuhong.ding");
        logger.info("github: {}","github.com/jiunong");
        BrowserUtil.browse1("http://localhost:9527/init");
    }
}
