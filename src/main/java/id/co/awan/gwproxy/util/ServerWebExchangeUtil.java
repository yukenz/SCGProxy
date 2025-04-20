package id.co.awan.gwproxy.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class ServerWebExchangeUtil {

    public static String getRequestHeaders(
            ServerWebExchange exchange,
            String headerName
    ) {

        HttpHeaders headers = exchange.getRequest().getHeaders();
        List<String> header = headers.get(headerName);

        if (header == null || header.isEmpty()) {
            return null;
        } else {
            return String.join(";", header);
        }
    }

    public static Mono<Void> requestProcessingByBody(
            ServerWebExchange exchange,
            GatewayFilterChain chain,
            RequestProcessing requestProcessing
    ) {


        Flux<DataBuffer> fluxBody = exchange.getRequest().getBody();
        Mono<DataBuffer> monoBody = DataBufferUtils.join(fluxBody);

        return monoBody.flatMap(dataBuffer -> {

            // Buat byte buffer
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            // Masukan data buffer ke byte buffer
            dataBuffer.read(bytes);
            // Close data buffer
            DataBufferUtils.release(dataBuffer);

            // Final string payload
            String payloadString = new String(bytes, StandardCharsets.UTF_8);

            log.info("Request payload: {}", payloadString);

            // Proses payload
            ServerHttpRequest serverHttpRequest = exchange.getRequest()
                    .mutate()
                    .header("X-HASH", "NULL")
                    .build();

            return chain.filter(
                    exchange.mutate()
                            .request(serverHttpRequest)
                            .build()
            );
        });


    }

    @FunctionalInterface
    public interface RequestProcessing {
        ServerHttpRequest process(
                HttpHeaders headers,
                String content
        );

    }


}
