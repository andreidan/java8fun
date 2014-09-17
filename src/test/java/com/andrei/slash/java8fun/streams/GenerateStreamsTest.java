package com.andrei.slash.java8fun.streams;

import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author andrei
 */
public class GenerateStreamsTest {

    @Test
    public void generate_pythagorean_triples() {
        Stream<int[]> triples = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                                .filter(b -> Math.sqrt(a * a + b * b) % 1.0 == 0)
                                .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
                );

        List<int[]> triplesList = triples.limit(2).collect(toList());

        assertEquals(3, triplesList.get(0)[0]);
        assertEquals(4, triplesList.get(0)[1]);
        assertEquals(5, triplesList.get(0)[2]);

        List<double[]> tuplesAsDouble = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                                .mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)})
                                .filter(e -> e[2] % 1.0 == 0)
                )
                .limit(2)
                .collect(toList());

        assertEquals(3, tuplesAsDouble.get(0)[0], 0.001);
        assertEquals(4, tuplesAsDouble.get(0)[1], 0.001);
        assertEquals(5, tuplesAsDouble.get(0)[2], 0.001);
    }

}
