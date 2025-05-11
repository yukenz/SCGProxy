package id.co.awan.gwproxy;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    void testInt() {

        List<String> collect = IntStream.of(1, 5)
                .peek(System.out::println)
                .mapToObj(value -> "Customer" + value)
                .peek(System.out::println)
                .collect(Collectors.toList());



    }
}
