package id.co.awan.gwproxy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class GatewayRecord {
    private String id;
    private String predicate;
    private String prefixPath;
    private String uri;

    @Override
    public String toString() {
        return String.format("""
                        \n
                            ID : %s
                            PREDICATE: %s
                            URI: %s
                            PREFIX_PATH: %s
                        """,
                id, predicate, uri, prefixPath);
    }
}
