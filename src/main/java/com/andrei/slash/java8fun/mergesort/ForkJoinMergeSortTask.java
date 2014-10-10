package com.andrei.slash.java8fun.mergesort;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

/**
 * @author andrei
 */
public class ForkJoinMergeSortTask extends RecursiveTask<int[]> {

    public static final int DEFAULT_SPLIT_LIMIT = 10_000;
    private final int[] numbersToSort;
    private final int forkFactor;
    private final int splitLimit;

    public ForkJoinMergeSortTask(int[] numbersToSort) {
        this(numbersToSort, Runtime.getRuntime().availableProcessors(), DEFAULT_SPLIT_LIMIT);
    }

    public ForkJoinMergeSortTask(int[] numbersToSort, int forkFactor, int splitLimit) {
        this.numbersToSort = numbersToSort;
        this.forkFactor = forkFactor;
        if (splitLimit < numbersToSort.length) {
            this.splitLimit = numbersToSort.length;
        } else {
            this.splitLimit = splitLimit;
        }
    }

    @Override
    protected int[] compute() {
        if (this.numbersToSort.length <= splitLimit) {
            Arrays.sort(numbersToSort);
            return numbersToSort;
        }

        int[] left = new int[numbersToSort.length / forkFactor];
        System.arraycopy(numbersToSort, 0, left, 0, left.length);
        ForkJoinMergeSortTask leftTask = new ForkJoinMergeSortTask(left);
        leftTask.fork(); // async

        int[] right = new int[numbersToSort.length - left.length];
        System.arraycopy(numbersToSort, left.length, right, 0, right.length);
        ForkJoinMergeSortTask rightTask = new ForkJoinMergeSortTask(right);
        int[] rightResult = rightTask.compute(); // blocking
        int[] leftResult = leftTask.join();

        merge(leftResult, rightResult, numbersToSort);
        return numbersToSort;
    }

    private int[] merge(int[] left, int[] second, int[] result) {
        int leftArrayIndex = 0;
        int secondArrayIndex = 0;

        int resultIndex = 0;
        // move forward until either left or right is processed completely
        while (leftArrayIndex < left.length && secondArrayIndex < second.length) {
            if (left[leftArrayIndex] < second[secondArrayIndex]) {
                result[resultIndex] = left[leftArrayIndex];
                leftArrayIndex++;
            } else {
                result[resultIndex] = second[secondArrayIndex];
                secondArrayIndex++;
            }
            resultIndex++;
        }
        // copy what's left
        if (left.length - leftArrayIndex > 0) {
            System.arraycopy(left, leftArrayIndex, result, resultIndex, left.length - leftArrayIndex);
        }
        if (second.length - secondArrayIndex > 0) {
            System.arraycopy(second, secondArrayIndex, result, resultIndex, second.length - secondArrayIndex);
        }
        return result;
    }
}
