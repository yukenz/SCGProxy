package id.co.awan.gwproxy.route;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class MidtransRoute implements Function<PredicateSpec, Buildable<Route>> {

    // CDI
    private final Environment ENV;


    // PROPS
    private String dynamichostPredicate;
    private String dynamichostUri;

    @PostConstruct
    private void init() {
        dynamichostPredicate = "openapi.stg.midtrans.com";
        dynamichostUri = ENV.getProperty("midtransURI");
    }

    @Override
    public Buildable<Route> apply(PredicateSpec predicateSpec) {
        return impl(predicateSpec);
    }

    // Filters
    private final Function<GatewayFilterSpec, UriSpec> MIDTRANS_FILTER = filters -> filters
            .setHostHeader(dynamichostPredicate)
            .addRequestHeader("X-Processed-By", "SpringGateway");
//            .filter(new MidtransGatewayFilter());


    // Route Implementation
    private Buildable<Route> impl(PredicateSpec predicateSpec) {

        String uri = Objects.requireNonNull(dynamichostUri);

        return predicateSpec
                .header("X-Dynamic-Host", dynamichostPredicate)
//                .filters(MIDTRANS_FILTER)
                .uri(URI.create(uri));
    }

}
