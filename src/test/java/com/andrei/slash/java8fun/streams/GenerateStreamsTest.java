package com.andrei.slash.java8fun.streams;

import org.junit.Test;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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

        assertThat(triplesList.get(0)[0], is(3));
        assertThat(triplesList.get(0)[1], is(4));
        assertThat(triplesList.get(0)[2], is(5));

        List<double[]> tuplesAsDouble = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                                .mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)})
                                .filter(e -> e[2] % 1.0 == 0)
                )
                .limit(2)
                .collect(toList());

        assertThat(tuplesAsDouble.get(0)[0], is(3.0));
        assertThat(tuplesAsDouble.get(0)[1], is(4.0));
        assertThat(tuplesAsDouble.get(0)[2], is(5.0));
    }

    private int[] firstTenFib = new int[]{0, 1, 1, 2, 3, 5, 8, 13, 21, 34};

    @Test
    public void generate_fib() {
        List<int[]> fibPairs = Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
//                .forEach(t -> System.out.println(t[0] + " - " + t[1] + " / "));
                .collect(toList());

        IntStream.range(0, 9).forEach(i -> {
            assertThat(fibPairs.get(i)[0], is(firstTenFib[i]));
            assertThat(fibPairs.get(i)[1], is(firstTenFib[i + 1]));
        });

        List<Integer> fibList = Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .map(t -> t[0])
//                .forEach(t -> System.out.println(t));
                .collect(toList());

        IntStream.range(0, 10).forEach(i -> assertThat("Expecting element " + i + " in the fib sequence to be " + firstTenFib[i], fibList.get(i), is(firstTenFib[i])));

        IntSupplier fibSupplier = new IntSupplier() {
            private int firstSeed = 0;
            private int secondSeed = 1;

            @Override
            public int getAsInt() {
                int current = firstSeed;
                int next = firstSeed + secondSeed;
                this.firstSeed = this.secondSeed;
                this.secondSeed = next;
                return current;
            }
        };

        List<Integer> generatedFib = IntStream.generate(fibSupplier).limit(10)
//                .forEach(System.out::println);
                .mapToObj(t -> new Integer(t))
                .collect(toList());

        IntStream.range(0, 10).forEach(i -> assertThat("Expecting element " + i + " in the fib sequence to be " + firstTenFib[i], generatedFib.get(i), is(firstTenFib[i])));
    }
}
