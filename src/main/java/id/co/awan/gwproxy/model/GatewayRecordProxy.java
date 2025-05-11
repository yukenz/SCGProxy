package id.co.awan.gwproxy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class GatewayRecordProxy {
    private String id;
    private String predicate;
    private String uri;
}
