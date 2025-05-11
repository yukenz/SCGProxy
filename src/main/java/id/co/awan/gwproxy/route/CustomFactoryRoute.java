package id.co.awan.gwproxy.route;

import id.co.awan.gwproxy.model.GatewayRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.util.unit.DataSize;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

import static id.co.awan.gwproxy.var.DefinitionVariable.*;

@RequiredArgsConstructor
@Slf4j
public class CustomFactoryRoute implements Function<PredicateSpec, Buildable<Route>> {


    // PROPS
    private final GatewayRecord gatewayRecord;


    @Override
    public Buildable<Route> apply(PredicateSpec predicateSpec) {
        return impl(predicateSpec);
    }


    // Route Implementation
    private Buildable<Route> impl(PredicateSpec predicateSpec) {


        String predicate = gatewayRecord.getPredicate();
        String prefixPath = gatewayRecord.getPrefixPath();
        String cacheConfig = gatewayRecord.getCacheConfig();
        String uri = Objects.requireNonNull(gatewayRecord.getUri());

        // Filters
        Function<GatewayFilterSpec, UriSpec> MIDTRANS_FILTER = filters -> {

            GatewayFilterSpec temporaryFilter;

            temporaryFilter = filters
                    .setHostHeader(predicate)
                    .addRequestHeader(X_PROCESSED_BY, X_PROCESSED_BY_VALUE);


            if (prefixPath != null) {
                temporaryFilter = temporaryFilter.prefixPath(prefixPath);
            }


            if (cacheConfig != null) {
                String[] cacheCfgToken = cacheConfig.split("\\|");
                long cacheDuration = Long.parseLong(cacheCfgToken[0]);
                long dataSize = Long.parseLong(cacheCfgToken[1]);

                temporaryFilter = temporaryFilter.localResponseCache(
                        Duration.ofSeconds(cacheDuration),
                        DataSize.ofMegabytes(dataSize)
                );

            }


            return temporaryFilter;

        };


        // Conditional
        BooleanSpec predicateBooleanSpec = predicateSpec
                .header(X_DYNAMIC_HOST, predicate);


        return predicateBooleanSpec
                .filters(MIDTRANS_FILTER)
                .uri(URI.create(uri));
    }

}
