package com.udcredit.demo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.udcredit.demo.util.MD5Utils;
import com.udcredit.demo.util.TestCaseUtil;
/**
 * Created by windows10 on 2017/5/17.
 * @author wanglr
 * @date 2017-5-17
 */
public class OrderQuery {
    //TODO 线上地址见对接文档中的服务描述，商户公钥，商户私钥，商户开户时会下发到商户邮件
    //商户公钥
    final static String pub_key = "4ad2c7c4-f9fa-456b-92cd-056d5e5bcd59";

    //订单查询接口地址
    static final String Order_Query = "http://10.1.30.51:8000/idsafe-front/frontserver/4.2/api/order_query/pub_key/" + pub_key;

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
        OrderQueryTest("O_20170517130503");
    }

        JSONObject OrderQueryTest(String partner_order_id)throws Exception{
        JSONObject renJson = new JSONObject();
        String sign_time = TestCaseUtil.getStringDate(new Date());
        String sign = getMD5Sign(pub_key, partner_order_id ,sign_time, security_key);
        System.out.println(sign);
        renJson.put("partner_order_id",partner_order_id);
        renJson.put("sign",sign);
        renJson.put("sign_time",sign_time);

        System.out.println("查询接口参数：" + JSON.toJSONString(renJson,true));

        JSONObject order_query = TestCaseUtil.doHttpRequest(Order_Query, renJson);
        System.out.println("查询接口查询结果：" + JSON.toJSONString(order_query,true));
        return order_query;
    }

    @After
    public void teardown() throws IOException {
        System.out.println("teardown...");
    }
}
