package id.co.awan.gwproxy.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "gw.cors")
@Getter
@Setter
public class GatewayCorsOriginList {
    private List<String> origin;
}
