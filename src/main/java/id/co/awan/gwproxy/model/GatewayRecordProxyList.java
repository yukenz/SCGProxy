package id.co.awan.gwproxy.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "gw.proxy")
@Getter
@Setter
public class GatewayRecordProxyList {
    private List<GatewayRecordProxy> records;
}
