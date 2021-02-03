//UT-EID=ksj572

import java.util.*;
import java.util.concurrent.*;

public class PSort {

    private static void insertionSort(int[] A, int begin, int end) {
        for (int sortedWall = begin; sortedWall < end - 1; sortedWall++) {
            int i = sortedWall + 1;
            while (i != begin && A[i] < A[i-1]) {
                int tmp = A[i];
                A[i] = A[i-1];
                A[i-1] = tmp;
                i--;
            }
        }
    }

    private static int partition(int[] A, int begin, int end) {
        int pivot = A[end-1]; // Random choice is fine
        int i = begin - 1;
        for (int j = begin; j < end - 1; j++) {
            if (A[j] <= pivot) {
                i++;
                int temp = A[i];
                A[i] = A[j];
                A[j] = temp;
            }
        }
        int temp = A[i+1];
        A[i+1] = A[end-1]; // pivot
        A[end-1] = temp;
        return i + 1;
    }

    private class SortTask extends RecursiveAction {
        final int[] A;
        final int begin;
        final int end;

        SortTask(int[] A, int begin, int end) {
            this.A = A;
            this.begin = begin;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (begin < end) {
                if (end - begin <= 16) {
                    PSort.insertionSort(A, begin, end);
                } else {
                    int pivot = PSort.partition(A, begin, end);
                    invokeAll(new SortTask(A, begin, pivot), new SortTask(A, pivot + 1, end));
                }
            }
        }

    }

    public static void parallelSort(int[] A, int begin, int end) {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        PSort outer = new PSort();
        SortTask task = outer.new SortTask(A, begin, end);
        commonPool.invoke(task);
    }
}
