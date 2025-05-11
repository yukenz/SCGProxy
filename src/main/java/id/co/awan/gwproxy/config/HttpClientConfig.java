package id.co.awan.gwproxy.config;

import id.co.awan.gwproxy.model.GatewayHttpProxy;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.transport.ProxyProvider;

import javax.net.ssl.SSLException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class HttpClientConfig {

    private final GatewayHttpProxy gatewayHttpProxy;

    @Bean
    public HttpClientCustomizer myHttpClientCustomizer() throws SSLException {

        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        return httpClient -> {

            httpClient.secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

            if (gatewayHttpProxy.getEnable()) {
                httpClient
                        .proxy(typeSpec -> {
                            typeSpec.type(ProxyProvider.Proxy.HTTP)
                                    .host(gatewayHttpProxy.getHost())
                                    .port(gatewayHttpProxy.getPort());
                        });
            }


            return httpClient;
        };
    }

}
