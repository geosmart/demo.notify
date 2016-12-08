# demo.notify
异步通知接收示例程序：一个纯Java Servlet实现的REST服务（POST接口），包含HttpClient调用的单元测试）

# 异步通知接收代码片段

``` java

/**
 * 异步通知结果接收接口
 *
 * @author geosmart
 * @date 2016-10-05
 */
public class NotifyResultProcessor {
    /**
     * TODO 获取商户开户的pubkey
     */
    static final String PUB_KEY = "";
    static final String CHARSET_UTF_8 = "UTF-8";
    static final boolean IS_DEBUG = true;

    /**
     * 接收实名认证异步通知
     */
    public static void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject reqObject = getRequestJson(request);

        JSONObject respJson = new JSONObject();
        //验签
        String sign = reqObject.getString("sign");
        System.out.println("sign：" + sign);
        String signMD5 = getSignMD5(reqObject);
        System.out.println("signMD5：" + signMD5);
        if (!sign.equals(signMD5)) {
            System.err.println("异步通知签名错误");
            respJson.put("code", "0");
            respJson.put("message", "签名错误");
        } else {
            System.out.print("收到商户异步通知");
            //TODO 商户业务逻辑：处理数据内容
            respJson.put("code", "1");
            respJson.put("message", "收到通知");
        }

        //返回结果
        response.setCharacterEncoding(CHARSET_UTF_8);
        response.setContentType("application/json; charset=utf-8");
        response.getOutputStream().write(respJson.toJSONString().getBytes(CHARSET_UTF_8));
    }

    private static JSONObject getRequestJson(HttpServletRequest request) throws IOException {
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

    /**
     * 根据sign_field签名域和对应的字段值生成MD5签名
     *
     * @param reqObject 请求对象
     */
    private static String getSignMD5(JSONObject reqObject) throws UnsupportedEncodingException {
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
        System.out.println("signField：" + signStr + PUB_KEY);
        return MD5Utils.MD5Encrpytion((signStr + PUB_KEY).getBytes(CHARSET_UTF_8));
    }
}

```

