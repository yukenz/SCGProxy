package id.co.awan.gwproxy.customize;

import id.co.awan.gwproxy.model.GatewayRecordProxy;
import id.co.awan.gwproxy.model.GatewayRecordProxyList;
import id.co.awan.gwproxy.route.CustomFactoryRoute;
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
    public RouteLocator customRouteLocator(
            RouteLocatorBuilder builder,
            GatewayRecordProxyList gatewayRecordProxyList
    ) {


        List<GatewayRecordProxy> records = gatewayRecordProxyList.getRecords();

        if (records.isEmpty()) {
            throw new IllegalStateException("No Proxy records found");
        }


        RouteLocatorBuilder.Builder routeBuilder = builder.routes();

        log.info("=== Registering Route ===");
        records.forEach(record -> {

            String id = record.getId();
            String predicate = record.getPredicate();
            String uri = record.getUri();

            CustomFactoryRoute customFactoryRoute = new CustomFactoryRoute(predicate, uri);

            log.info("""
                            \n
                                ID : {}
                                PREDICATE: {}
                                URI: {}
                            """,
                    id, predicate, uri);
            // Registering
            routeBuilder.route(id, customFactoryRoute);
        });

        log.info("=== Finish Registering Route ===");
        return routeBuilder.build();

    }

}
