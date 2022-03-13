package com.xhtt.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if(httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName("utf-8"));
                break;
            }
        }
//        SimpleClientHttpRequestFactory reqfac = new SimpleClientHttpRequestFactory();
//        reqfac.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("218.27.136.169", 8085)));
//        restTemplate.setRequestFactory(reqfac);
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(5000);

        return factory;
    }

//    @Bean
//    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
//        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
//
//        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
//                .loadTrustMaterial(null, acceptingTrustStrategy)
//                .build();
//
//        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
//
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setSSLSocketFactory(csf)
//                .build();
//
//        HttpComponentsClientHttpRequestFactory requestFactory =
//                new HttpComponentsClientHttpRequestFactory();
//
//        requestFactory.setHttpClient(httpClient);
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//        SimpleClientHttpRequestFactory reqfac = new SimpleClientHttpRequestFactory();
//        reqfac.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("218.27.136.169", 8085)));
//        restTemplate.setRequestFactory(reqfac);
//        return restTemplate;
//    }
}
