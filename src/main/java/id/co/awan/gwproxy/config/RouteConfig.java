package id.co.awan.gwproxy.config;

import id.co.awan.gwproxy.record.GatewayProxyRecord;
import id.co.awan.gwproxy.route.GatewayProxyRouteFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class RouteConfig {

    @Bean
    public RouteLocator routeLocator(
            RouteLocatorBuilder builder,
            GatewayRecordProxyConfig gatewayRecordProxyConfig
    ) {
        List<GatewayProxyRecord> records = gatewayRecordProxyConfig.getRecords();
        RouteLocatorBuilder.Builder routeBuilder = builder.routes();

        // Record must not be empty
        if (records.isEmpty()) {
            throw new IllegalStateException("No Proxy records found");
        }

        // Start Registerin Proxy Route to the Gateway
        log.info("=== Registering Gateway Proxy Route ===");
        records.forEach(record -> {

            // Destruct record properties
            var id = record.getId();
            var predicate = record.getPredicate();
            var uri = record.getUri();

            // Registering
            routeBuilder.route(id, new GatewayProxyRouteFactory(predicate, uri));
            log.info("""
                            \n
                                ID : {}
                                PREDICATE: {}
                                URI: {}
                            """,
                    id, predicate, uri);
        });
        log.info("=== Finish Registering Gateway Proxy Route ===");

        return routeBuilder.build();
    }

}
