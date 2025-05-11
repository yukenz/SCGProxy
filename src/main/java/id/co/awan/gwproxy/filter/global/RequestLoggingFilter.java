package id.co.awan.gwproxy.filter.global;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

@Component
@Log4j2
public class RequestLoggingFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // Req and Res
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();

        // Decorator
        ServerHttpRequest decoratedRequest = getDecoratedRequest(request);
        ServerHttpResponseDecorator decoratedResponse = getDecoratedResponse(response, request);

        // Execute
        return chain.filter(
                exchange.mutate()
                        .request(decoratedRequest)
                        .response(decoratedResponse)
                        .build()
        );
    }

    private ServerHttpResponseDecorator getDecoratedResponse(
            ServerHttpResponse response,
            ServerHttpRequest request
    ) {

//        CorsUtils.removeVary(response);
//        CorsUtils.allowInsecureCors(response);

        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(final Publisher<? extends DataBuffer> body) {

                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

                    Flux<DataBuffer> newBuffer = fluxBody.buffer()
                            .map(dataBuffers -> {

                                // Join Data Buffer
                                DefaultDataBuffer joinedBuffers = new DefaultDataBufferFactory()
                                        .join(dataBuffers);

                                // Buffer read content
                                byte[] contentBytes = new byte[joinedBuffers.readableByteCount()];
                                joinedBuffers.read(contentBytes);

                                String responseBody = new String(contentBytes, StandardCharsets.UTF_8);
                                log.info("requestId: {}, method: {}, url: {}, response body :{}", request.getId(), request.getMethod(), request.getURI(), responseBody);

                                // Rebuild Stream
                                DataBufferFactory dataBufferFactory = response.bufferFactory();
                                return dataBufferFactory.wrap(contentBytes);
                            });

                    return super
                            .writeWith(newBuffer)
                            .onErrorResume(err -> {
                                log.info("error while decorating Response: {}", err.getMessage());
                                return Mono.empty();
                            });

                }

                return super.writeWith(body);
            }
        };
    }

    private ServerHttpRequest getDecoratedRequest(
            ServerHttpRequest request
    ) {

        return new ServerHttpRequestDecorator(request) {
            @Override
            public Flux<DataBuffer> getBody() {

                log.info("requestId: {}, method: {} , url: {}", request.getId(), request.getMethod(), request.getURI());

                // Will not log if body empty
                return super.getBody()
                        .publishOn(Schedulers.boundedElastic())
                        .doOnNext(dataBuffer -> {

                            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

                                // Consume Stream
                                Channels.newChannel(byteArrayOutputStream)
                                        .write(dataBuffer.asByteBuffer()
                                                .asReadOnlyBuffer()
                                        );

                                // Build Stream to String
                                String requestBody = IOUtils.toString(
                                        byteArrayOutputStream.toByteArray(),
                                        StandardCharsets.UTF_8.toString()
                                );

                                log.info("for requestId: {}, request body :{}", request.getId(), requestBody);
                            } catch (Exception e) {
                                log.info(e.getMessage());
                            }
                        });
            }
        };
    }

    @Override
    public int getOrder() {
        return -2;
    }
}