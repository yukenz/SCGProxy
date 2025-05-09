package id.co.awan.gwproxy.customize;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLException;

@Configuration
public class HttpClientConfig {

    @Bean
    public HttpClientCustomizer myHttpClientCustomizer() throws SSLException {

        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        return httpClient -> httpClient
                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
    }

}
