package com.udcredit.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.udcredit.demo.util.MD5Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 异步通知结果接收接口
 *
 * @author geosmart
 * @date 2017-01-06
 */
public class NotifyResultProcessor {
    /**
     * TODO 获取商户开户的SecurityKey
     */
    static final String SECURITY_KEY = "";
    static final String CHARSET_UTF_8 = "UTF-8";
    static final boolean IS_DEBUG = true;

    /**
     * 接收实名认证异步通知
     */
    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject reqObject = getRequestJson(request);

        JSONObject respJson = new JSONObject();
        //验签
        String sign = reqObject.getString("sign");
        String signMD5 = getSignMD5(reqObject);
        System.err.println("signMD5：" + signMD5);
        if (!sign.equals(signMD5)) {
            System.err.println("异步通知签名错误");
            respJson.put("code", "0");
            respJson.put("message", "签名错误");
        } else {
            System.out.print("收到商户异步通知");
            respJson.put("code", "1");
            respJson.put("message", "收到通知");

            //TODO 异步执行商户自己的业务逻辑(以免阻塞返回导致通知多次)
            Thread asyncThread = new Thread("asyncDataHandlerThread") {
                public void run() {
                    System.out.println("异步执行商户自己的业务逻辑...");
                    try {
                        String id_name = reqObject.getString("id_name");
                        String id_number = reqObject.getString("id_no");
                        System.out.println(id_name + "：" + id_number);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            asyncThread.start();
        }

        System.out.println("返回结果：" + respJson.toJSONString());

        //返回结果
        response.setCharacterEncoding(CHARSET_UTF_8);
        response.setContentType("application/json; charset=utf-8");
        response.getOutputStream().write(respJson.toJSONString().getBytes(CHARSET_UTF_8));
    }

    /**
     * 根据sign_field签名域和对应的字段值生成MD5签名
     *
     * @param reqObject 请求对象
     */
    private String getSignMD5(JSONObject reqObject) throws UnsupportedEncodingException {
        String[] signFields = reqObject.getString("sign_field").split("\\|");
        java.util.List<String> signKeyValues = new java.util.ArrayList<String>(signFields.length + 1);
        for (String signField : signFields) {
            signKeyValues.add(signField + "=" + reqObject.getString(signField));
        }
        String signStr = "";
        for (int i = 0; i < signKeyValues.size(); i++) {
            signStr = signStr + signKeyValues.get(i);
            if (i != signKeyValues.size() - 1) {
                signStr += "|";
            }
        }
        System.out.println("signField：" + signStr + SECURITY_KEY);
        return MD5Utils.MD5Encrpytion((signStr + SECURITY_KEY).getBytes(CHARSET_UTF_8));
    }

    /**
     * 获取请求json对象
     */
    private JSONObject getRequestJson(HttpServletRequest request) throws IOException {
        InputStream in = request.getInputStream();
        byte[] b = new byte[10240];
        int len;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = in.read(b)) > 0) {
            baos.write(b, 0, len);
        }
        String bodyText = new String(baos.toByteArray(), CHARSET_UTF_8);
        JSONObject json = (JSONObject) JSONObject.parse(bodyText);
        if (IS_DEBUG) {
            System.out.println("received notify message:");
            System.out.println(JSON.toJSONString(json, true));
        }
        return json;
    }
}
