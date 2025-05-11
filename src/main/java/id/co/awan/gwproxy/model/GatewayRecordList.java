package id.co.awan.gwproxy.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "gw")
@Getter
@Setter
public class GatewayRecordList {
    private List<GatewayRecord> records;
}
