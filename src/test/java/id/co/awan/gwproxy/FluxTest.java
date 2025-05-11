package id.co.awan.gwproxy;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class FluxTest {

    @Test
    void testFluxJoin() throws InterruptedException {

        Flux<String> stringFlux = Flux.just("One", "Two", "Three")
                // Log
                .log()
                .flatMap(s -> Flux.just(s, "FlatMap"))
                .concatMap(s -> Flux.just(s, "Concat"))
                // Add person prefix
                .map(s -> "Person Map " + s)
                // Delay each element
                .delayElements(Duration.ofMillis(100L))
                .defaultIfEmpty("Default");

//        stringFlux.subscribe(System.out::println);

        Flux<List<String>> buffer = stringFlux.buffer();
        Mono<List<String>> listMono = stringFlux.collectList();
        Mono<Void> then = stringFlux.then(Mono.just("OK")).then();

        Thread.sleep(5000L);

        StringBuilder block = stringFlux.collect(
                        StringBuilder::new,
                        (s, s2) -> {
                            s.append(s2);
                        })
                .block();

        System.out.println("Block: " + block);

//        String blockedFirst = stringFlux.blockFirst();
//        System.out.println("Block First: " + blockedFirst);

    }
}
