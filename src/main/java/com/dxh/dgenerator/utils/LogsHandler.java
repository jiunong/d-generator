package com.dxh.dgenerator.utils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import com.dxh.dgenerator.config.ConstVal;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;


/**
 * TODO
 *
 * @author xuhong.ding
 * @since 2021/1/15 14:51
 */
public class LogsHandler {

    private static LogsHandler logsHandler;

    private HttpServletRequest httpServletRequest;


    private static final String logPath = FileUtil.getWebRoot().getPath().concat(FileUtil.isWindows() ? "\\logs\\db.out" : "/logs/db.out");

    private static final File logOutFile;

    static {
        logOutFile = new File(logPath);
        if (!FileUtil.exist(logOutFile)) {
            FileUtil.touch(logOutFile);
        }
    }

    public static LogsHandler instance(HttpServletRequest request) {
        if (logsHandler == null) {
            logsHandler = new LogsHandler(request);
        } else {
            logsHandler.httpServletRequest = request;
        }
        return logsHandler;
    }

    public LogsHandler(HttpServletRequest request) {
        this.httpServletRequest = request;
    }

    /**
     * TODO 添加日志
     *
     * @param log 日志
     * @author xuhong.ding
     * @since 2021/1/19 10:04
     */
    public void logs(String log, Object... var2) {
        String ipAddr = this.getIpAddr(this.httpServletRequest);
        AtomicReference<String> logNew = new AtomicReference<>(log);

        Arrays.asList(var2).forEach(u -> logNew.set(logNew.get().replaceFirst("\\{}", (String) u)));
        FileUtil.appendUtf8Lines(ListUtil.toList(DateTime.now().toString()
                .concat(":[")
                .concat(ipAddr)
                .concat("]")
                .concat(logNew.get())), logOutFile);
    }


    /**
     * TODO
     *
     * @param request
     * @return java.lang.String
     * @author xuhong.ding
     * @since 2021/2/4 9:17
     */
    private String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

}
