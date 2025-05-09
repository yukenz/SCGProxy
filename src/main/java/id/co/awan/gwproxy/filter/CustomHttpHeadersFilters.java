package id.co.awan.gwproxy.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.headers.RemoveHopByHopHeadersFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
@Slf4j
public class CustomHttpHeadersFilters extends RemoveHopByHopHeadersFilter {

    @Override
    public HttpHeaders filter(HttpHeaders originalHeaders, ServerWebExchange exchange) {

        log.info("Custom header filter executed");

        return super.filter(originalHeaders, exchange);
    }

}
