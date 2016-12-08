# demo.notify
异步通知接收示例程序：一个纯Java Servlet实现的REST服务（POST接口），包含HttpClient调用的单元测试）

# 异步通知接收代码片段

``` java
/**
 * 异步通知结果接收示例
 * "TODO 商户补充" 标记表示此处需要商户自行实现
 */
public class NotifyProcessorDemo {
    /**
     * 接收实名认证异步通知
     */
    public static void process() throws java.io.IOException {
        //TODO 商户补充：HttpServletRequest中获取请求JSON字符串:编码为UTF-8，建议使用 IOUtils.toString(request,"UTF-8")
        String reqJsonStr = "";

        com.alibaba.fastjson.JSONObject reqObject = com.alibaba.fastjson.JSON.parseObject(reqJsonStr);
        com.alibaba.fastjson.JSONObject respJson = new com.alibaba.fastjson.JSONObject();

        //验签
        String sign = reqObject.getString("sign");
        String signMD5 = getSignMD5(reqObject);
        if (!sign.equals(signMD5)) {
            //异步通知签名错误;
            respJson.put("code", "0");
            respJson.put("message", "签名错误");
        } else {
            //收到商户异步通知;
            //TODO 商户补充：使用数据的业务逻辑
            respJson.put("code", "1");
            respJson.put("message", "接收成功");
        }
        //TODO 商户补充：结果写入HttpServletResponse返回
    } 
    
    /**
     * 根据sign_field签名域和对应的字段值生成MD5签名
     *
     * @param reqObject 请求对象
     */
    private static String getSignMD5(com.alibaba.fastjson.JSONObject reqObject) {
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
        //TODO 商户补充：获取开户的pubkey
        String pubkey = "";
        return MD5Utils.MD5Encrpytion((signStr + pubkey).getBytes("UTF-8"));
    }
}

```

