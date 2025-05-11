package id.co.awan.gwproxy.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "gw.proxy.http")
@Getter
@Setter
public class GatewayHttpProxy {
    private Boolean enable;
    private String host;
    private Integer port;

    @Override
    public String toString() {
        return String.format("""
                \n
                ======= HTTP PROXY CONFIGURATION =======
                IS ENABLED: %s
                HOST: %s
                PORT: %s
                """, getEnable(), getHost(), getPort());
    }
}
