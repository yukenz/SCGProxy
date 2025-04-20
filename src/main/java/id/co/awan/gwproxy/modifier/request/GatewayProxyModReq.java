package id.co.awan.gwproxy.modifier.request;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class GatewayProxyModReq implements RewriteFunction<String, String> {


    @Override
    public Publisher<String> apply(ServerWebExchange serverWebExchange, String payload) {


        return Mono.just(payload);
    }
}
