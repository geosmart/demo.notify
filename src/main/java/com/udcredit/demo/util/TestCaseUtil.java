package demo.idsafe.api.util;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * 单元测试辅助类
 *
 * @author geosmart
 * @date 2017/01/04
 */
public class TestCaseUtil {
    static final String CHASET_UTF_8 = "UTF-8";

    /**
     * 格式化日期字符串 yyyyMMddHHmmss
     */
    public static String getStringDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date);
    }

    /**
     * Http请求
     */
    public static JSONObject doHttpRequest(String url, JSONObject reqJson) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        //设置传入参数
        StringEntity entity = new StringEntity(reqJson.toJSONString(), CHASET_UTF_8);
        entity.setContentEncoding(CHASET_UTF_8);
        entity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);

        //调用异步通知接收接口
        HttpResponse resp = client.execute(httpPost);
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            String respContent = EntityUtils.toString(he, CHASET_UTF_8);
            return (JSONObject) JSONObject.parse(respContent);
        }
        return null;
    }

//    public static String getFileBase64Str(String fileName) throws IOException {
//        String filePath = System.getProperty("user.dir") + "//src//test//resources//idcard//" + fileName;
//        System.out.println("测试照片文件：" + filePath);
//        File file = new File(filePath);
//        byte[] front = FileUtils.readFileToByteArray(file);
//        return Base64.getEncoder().encodeToString(front);
//    }
}
