package id.co.awan.gwproxy;

import id.co.awan.gwproxy.model.GatewayCorsOriginList;
import id.co.awan.gwproxy.model.GatewayHttpProxy;
import id.co.awan.gwproxy.model.GatewayRecordList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties({
        GatewayRecordList.class,
        GatewayCorsOriginList.class,
        GatewayHttpProxy.class
})
public class GwproxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GwproxyApplication.class, args);
    }


}
