package com.proinnova.b;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
public class Config {

//    @Value("${server.ssl.trust-store}")
//    private String trustStorePath;
//
//    @Value("${server.ssl.trust-store-password}")
//    private String trustStorePassword;

    @Bean
    public SSLContext sslContext() throws Exception {
        return SSLContextBuilder.create()
//                .loadTrustMaterial(ResourceUtils.getFile("classpath:client.keystore"), "123456".toCharArray())
                .loadTrustMaterial((chain, authType) -> true)
                .build();
    }

    @LoadBalanced
    @Bean(name = "restTemplate")
    public RestTemplate restTemplate(SSLContext sslContext) {
        // NoopHostnameVerifier.INSTANCE - 禁用主机名验证（绕过证书CN/SAN检查，不然restTemplate调用其它服务时，会验证主机名，导致调用失败）
        TlsSocketStrategy tlsStrategy = new DefaultClientTlsStrategy(sslContext, NoopHostnameVerifier.INSTANCE);
        // 创建带连接池的HTTP连接管理器：
        // 1. 默认支持HTTP/1.1和HTTP/2
        // 2. 自动管理连接生命周期
        HttpClientConnectionManager httpClientConnectionManager = PoolingHttpClientConnectionManagerBuilder.create().setTlsSocketStrategy(tlsStrategy).build();
        // 构建可关闭的HttpClient实例：
        // 1. 使用连接池提升性能
        // 2. 默认启用响应压缩
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpClientConnectionManager).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }
}
