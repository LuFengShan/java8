package com.java8.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class HttpClientTest {
    private static final String SAMPLE_URL = "http://testbeidou.cupid.autohome.com.cn/web/api/v1/resource/pool/product/info/biz/type?bizType=1";

    private CloseableHttpClient instance;

    private CloseableHttpResponse response;

    @Before
    public final void before() {
        instance = HttpClientBuilder.create().build();
    }

    // tests

    // simple request - response

    @Test
    public final void whenExecutingBasicGetRequest_thenNoExceptions() throws ClientProtocolException, IOException {
        response = instance.execute(new HttpGet(SAMPLE_URL));
    }

    @Test
    public final void givenGetRequestExecuted_whenAnalyzingTheResponse_thenCorrectStatusCode() throws ClientProtocolException, IOException {
        response = instance.execute(new HttpGet(SAMPLE_URL));
        final int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
    }

    @Test
    public final void givenGetRequestExecuted_whenAnalyzingTheResponse_thenCorrectMimeType() throws ClientProtocolException, IOException {
        response = instance.execute(new HttpGet(SAMPLE_URL));
        final String contentMimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        System.out.println(contentMimeType);
    }

    @Test
    public final void givenGetRequestExecuted_whenAnalyzingTheResponse_thenCorrectBody() throws ClientProtocolException, IOException, InterruptedException {
        HttpGet getMethod = new HttpGet(SAMPLE_URL);
        response = instance.execute(getMethod);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (getMethod != null) {
                    getMethod.abort();
                    System.out.println(LocalDateTime.now());
                }
            }
        };

        new Timer(true).schedule(task, 3);
        final String bodyAsString = EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
        System.out.println(String.format("HTTP Status of response: %d, body: %s", response.getStatusLine().getStatusCode(), bodyAsString));
    }

}
