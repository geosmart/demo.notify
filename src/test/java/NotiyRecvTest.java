import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * 测试异步通知接收接口
 *
 * @author geosmart
 * @date 2016-10-05
 */
public class NotiyRecvTest {
    static final String url = "http://localhost:9090/demo/notify";
    static final String CHASET_UTF_8 = "UTF-8";

    //测试异步通知json字符串
    static String requestJson;

    @Before
    public void setup() throws IOException {
        //获取测试数据
        java.io.File f = new java.io.File(System.getProperty("user.dir") + "/src/test/resources/notify/notify.json");
        requestJson = FileUtils.readFileToString(f, CHASET_UTF_8);
        System.out.println("通知数据...");
        System.out.println(requestJson);
    }

    @Test
    public void test_notify() throws java.io.IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        //设置传入参数
        StringEntity entity = new StringEntity(requestJson, CHASET_UTF_8);
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);

        //调用异步通知接收接口
        HttpResponse resp = client.execute(httpPost);
        if (resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            String respContent = EntityUtils.toString(he, CHASET_UTF_8);
            printJson(respContent);
        }
    }

    private void printJson(String respContent) {
        JSONObject returnObject = (JSONObject) JSONObject.parse(respContent);
        System.out.println("接收结果...");
        System.out.println(JSON.toJSONString(returnObject, true));
    }
}
