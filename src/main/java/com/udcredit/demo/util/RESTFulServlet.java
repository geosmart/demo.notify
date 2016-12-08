package com.udcredit.demo.util;

import com.udcredit.demo.NotifyResultProcessor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST服务入口
 *
 * @author geosmart
 * @date 2016-10-05
 */
public class RESTFulServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    public RESTFulServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGet method is runing...");
        System.out.println("received id = " + getID(request));
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost method is runing...");
        String pathId = getID(request);
        System.out.println("received id = " + pathId);
        if ("notify".equals(pathId)) {
            new NotifyResultProcessor().process(request, response);
        }
    }

    protected void doPut(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        super.doPut(request, response);
        System.out.println("doPut method is runing...");
        System.out.println("received id = " + getID(request));
    }

    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException {
        super.doDelete(request, response);
        System.out.println("doDelete method is runing...");
        System.out.println("received id = " + getID(request));
    }

    public void init() throws ServletException {
        System.out.println("init RESTFulServlet...");
        super.init();
    }

    //只是作为演示使用，不处理异常
    private String getID(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String id;
        if (url.endsWith("/"))
            url = url.substring(0, url.length() - 1);
        id = url.substring(url.lastIndexOf('/') + 1);
        return id;
    }
}
