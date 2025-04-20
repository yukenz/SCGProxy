package id.co.awan.gwproxy.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class GatewayProxyRecord {
    private String id;
    private String predicate;
    private String uri;
}
