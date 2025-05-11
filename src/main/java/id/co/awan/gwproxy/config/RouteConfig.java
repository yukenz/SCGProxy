package id.co.awan.gwproxy.config;

import id.co.awan.gwproxy.model.GatewayRecord;
import id.co.awan.gwproxy.model.GatewayRecordList;
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
            GatewayRecordList gatewayRecordList
    ) {


        List<GatewayRecord> records = gatewayRecordList.getRecords();

        if (records.isEmpty()) {
            throw new IllegalStateException("No Proxy records found");
        }


        RouteLocatorBuilder.Builder routeBuilder = builder.routes();

        log.info("=== Registering Route ===");
        records.forEach(record -> {

            String id = record.getId();
            String predicate = record.getPredicate();
            String uri = record.getUri();
            String prefixPath = record.getPrefixPath();

            CustomFactoryRoute customFactoryRoute = new CustomFactoryRoute(predicate, uri, prefixPath);

            log.info(record.toString());

            // Registering
            routeBuilder.route(id, customFactoryRoute);
        });

        log.info("=== Finish Registering Route ===");
        return routeBuilder.build();

    }

}
