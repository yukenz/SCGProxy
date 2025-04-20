package id.co.awan.gwproxy.config;

import id.co.awan.gwproxy.record.GatewayProxyRecord;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "gw.proxy")
@Getter
@Setter
public class GatewayRecordProxyConfig {
    private List<GatewayProxyRecord> records;
}
