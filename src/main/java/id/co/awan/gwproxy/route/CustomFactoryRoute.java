package id.co.awan.gwproxy.route;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.*;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

import static id.co.awan.gwproxy.var.DefinitionVariable.*;

@RequiredArgsConstructor
@Slf4j
public class CustomFactoryRoute implements Function<PredicateSpec, Buildable<Route>> {


    // PROPS
    private final String dynamichostPredicate;
    private final String dynamichostUri;


    @Override
    public Buildable<Route> apply(PredicateSpec predicateSpec) {
        return impl(predicateSpec);
    }


    // Route Implementation
    private Buildable<Route> impl(PredicateSpec predicateSpec) {

        // Filters
        Function<GatewayFilterSpec, UriSpec> MIDTRANS_FILTER = filters -> filters
                .setHostHeader(dynamichostPredicate)
                .addRequestHeader(X_PROCESSED_BY, X_PROCESSED_BY_VALUE);


        String uri = Objects.requireNonNull(dynamichostUri);

        // Conditional
        BooleanSpec predicate = predicateSpec
                .header(X_DYNAMIC_HOST, dynamichostPredicate);

        return predicate
                .filters(MIDTRANS_FILTER)
                .uri(URI.create(uri));
    }

}
