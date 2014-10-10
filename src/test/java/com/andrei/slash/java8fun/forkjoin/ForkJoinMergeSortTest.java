package com.andrei.slash.java8fun.forkjoin;

import com.andrei.slash.java8fun.mergesort.ForkJoinMergeSortTask;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertTrue;

/**
 * @author andrei
 */
public class ForkJoinMergeSortTest {

    private ForkJoinPool forkJoinPool = new ForkJoinPool();

    @Test
    public void test_array_should_be_sorted() {
        int[] numbersToSort = getArrayWithRandomNumbers(200_000);

        int[] numbersToSortCopy = copyArray(numbersToSort);
        forkJoinPool.submit(new ForkJoinMergeSortTask(numbersToSortCopy));

        int[] classicSortedArray = copyArray(numbersToSort);
        Arrays.sort(classicSortedArray);

        assertTrue(Arrays.equals(numbersToSortCopy, classicSortedArray));
    }

    @Test
    public void test_sort_with_splitLimit_lower_than_array_length() {
        int[] numbersToSort = getArrayWithRandomNumbers(10_000);

        int[] numbersToSortCopy = copyArray(numbersToSort);
        forkJoinPool.submit(new ForkJoinMergeSortTask(numbersToSortCopy, Runtime.getRuntime().availableProcessors(), 5000));

        int[] classicSortedArray = copyArray(numbersToSort);
        Arrays.sort(classicSortedArray);

        assertTrue(Arrays.equals(numbersToSortCopy, classicSortedArray));
    }

    private int[] getArrayWithRandomNumbers(int arrayLength) {
        int[] numbersToSort = new int[arrayLength];
        Random random = new Random();
        for (int i = 0; i < numbersToSort.length; i++) {
            numbersToSort[i] = random.nextInt(200_000);
        }
        return numbersToSort;
    }

    private int[] copyArray(int[] numbersToSort) {
        int[] copy = new int[numbersToSort.length];
        System.arraycopy(numbersToSort, 0, copy, 0, numbersToSort.length);
        return copy;
    }

}
