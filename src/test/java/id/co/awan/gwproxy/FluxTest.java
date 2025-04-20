package id.co.awan.gwproxy;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Stream;

public class FluxTest {

    @Test
    void testFluxJoin() {

        Flux<String> just1 = Flux.just("A", "B", "C");
        Flux<CharSequence> just2 = just1
                .flatMap(s -> Flux.just((CharSequence) s,"flatmap"));

        Flux<Integer> just3 = just2.map(CharSequence::length);

        just3.subscribe(System.out::println);

    }
}
