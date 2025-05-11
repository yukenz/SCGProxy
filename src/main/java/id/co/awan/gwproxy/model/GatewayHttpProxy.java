package id.co.awan.gwproxy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "gw.proxy.http")
@Getter
@Setter
public class GatewayHttpProxy {
    private Boolean enable;
    private String host;
    private Integer port;
}
