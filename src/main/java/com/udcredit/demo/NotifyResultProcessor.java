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
     * TODO 获取商户开户的pub_key
     */
    static final String PUB_KEY = "3d0fcd26-bb37-49e8-84bd-e5ecdd8ba9cf";
    /**
     * TODO 获取商户开户的security_key
     */
    static final String SECURITY_KEY = "cab06b8b-cb25-4074-b90c-32ffd478cc70";

    static final String CHARSET_UTF_8 = "UTF-8";
    static final boolean IS_DEBUG = true;

    /**
     * 生成MD5签名
     *
     * @param pub_key          商户公钥
     * @param partner_order_id 商户订单号
     * @param sign_time        签名时间
     * @param security_key     商户私钥
     */
    public static String getMD5Sign(String pub_key, String partner_order_id, String sign_time, String security_key) throws UnsupportedEncodingException {
        String signStr = String.format("pub_key=%s|partner_order_id=%s|sign_time=%s|security_key=%s", pub_key, partner_order_id, sign_time, security_key);
        System.out.println("signField：" + signStr + SECURITY_KEY);
        return MD5Utils.MD5Encrpytion(signStr.getBytes("UTF-8"));
    }

    /**
     * 接收实名认证异步通知
     */
    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("收到通知");
        final JSONObject reqJson = getRequestJson(request);
        //通用请求时间
        JSONObject header = reqJson.getJSONObject("header");
        String sign_time = header.getString("sign_time");

        JSONObject respJson = new JSONObject();
        //验签
        String sign = header.getString("sign");
        String partner_order_id = header.getString("partner_order_id");
        System.out.println("sign：" + sign);
        String signMD5 = getMD5Sign(PUB_KEY, partner_order_id, sign_time, SECURITY_KEY);
        System.out.println("signMD5：" + signMD5);
        if (!sign.equals(signMD5)) {
            System.err.println("异步通知签名错误");
            respJson.put("code", "0");
            respJson.put("message", "签名错误");
        } else {
            respJson.put("code", "1");
            respJson.put("message", "收到通知");
            //TODO 异步执行商户自己的业务逻辑(以免阻塞接口返回，导致通知多次)
            Thread asyncThread = new Thread("asyncThread") {
                public void run() {
                    System.out.println("异步执行商户自己的业务逻辑...");
                    try {
                        JSONObject body = reqJson.getJSONObject("body");
                        System.out.println(body.toJSONString());
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
