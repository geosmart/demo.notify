package com.udcredit.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.udcredit.demo.util.MD5Utils;
import com.udcredit.demo.util.TestCaseUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 订单查询接口测试用例
 *
 * @author wanglr
 * @date 2017-5-17
 */
public class OrderQueryAPITest {
    //TODO 线上地址见对接文档中的服务描述，商户公钥，商户私钥，商户开户时会下发到商户邮件
    //商户公钥
    final static String pub_key = "4ad2c7c4-f9fa-456b-92cd-056d5e5bcd59";

    //订单查询接口地址
    static final String Order_Query = "http://10.10.0.230:8080/idsafe-front/front/4.3/api/order_query/pub_key/" + pub_key;

    //商户私钥
    final static String security_key = "2e6b6da8-77b9-4268-a8ba-8ff47ca7e6b6";


    public static String getMD5Sign(String pub_key, String partner_order_id, String sign_time, String security_key) throws UnsupportedEncodingException {
        String signStr = String.format("pub_key=%s|partner_order_id=%s|sign_time=%s|security_key=%s", pub_key, partner_order_id, sign_time, security_key);
        System.out.println("测试输入签名signField：" + signStr);
        return MD5Utils.MD5Encrpytion(signStr.getBytes("UTF-8"));
    }

    @Before
    public void setup() throws IOException {
        System.out.println("setup...");
    }

    @Test
    public void OrderQueryTest() throws Exception {
        orderQuery("194339067293335552");
    }


    /**
     * 订单查询
     *
     * @param partner_order_id 商户唯一订单号
     */
    private JSONObject orderQuery(String partner_order_id) throws Exception {
        JSONObject reqJson = new JSONObject();
        JSONObject header = new JSONObject();
        String sign_time = TestCaseUtil.getStringDate(new Date());
        String sign = getMD5Sign(pub_key, partner_order_id, sign_time, security_key);
        System.out.println(sign);
        header.put("partner_order_id", partner_order_id);
        header.put("sign", sign);
        header.put("sign_time", sign_time);
        reqJson.put("header", header);
        System.out.println("订单查询接口-输入参数：" + JSON.toJSONString(reqJson, true));

        JSONObject resJson = TestCaseUtil.doHttpRequest(Order_Query, reqJson);
        System.out.println("订单查询接口-输出结果：" + JSON.toJSONString(resJson, true));
        return resJson;
    }

    @After
    public void teardown() throws IOException {
        System.out.println("teardown...");
    }
}
