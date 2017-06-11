# demo.notify
异步通知接收示例程序：一个纯Java Servlet实现的REST服务（POST接口），包含HttpClient调用的单元测试）

# 异步通知接收代码片段

``` java 

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
                        if (body.containsKey("product_code")) {
                            EnumProductCode enumProductCode = EnumProductCode.newInstance(body.getString("product_code"));
                            switch (enumProductCode) {//按产品接收结果数据
                                case OCR_FRONT:
                                    //证面OCR
                                    break;
                                case OCR_BACK:
                                    //反面OCR
                                    break;
                                case VERIFY_SIMPLE:
                                case VERIFY_RETURN_PHOTO:
                                    //实名验证
                                    break;
                                case LIVING_DETECT:
                                    //活体检测
                                    break;
                                case FACE_COMPARE:
                                    //人脸比对
                                    break;
                                case VIDEO_AUTH:
                                    //视频存证
                                    break;
                            }
                        }
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

```

