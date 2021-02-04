//UT-EID=ksj572, bb37665

import java.util.*;
import java.util.concurrent.*;

public class PMerge extends RecursiveAction {
    final int pos;
    final int val;
    final boolean precedence;
    final int[] other;
    final int[] C;

    PMerge(int pos, int val, boolean precedence, int[] other, int[] C) {
        this.pos = pos;
        this.val = val;
        this.precedence = precedence;
        this.other = other;
        this.C = C;
    }


    public static void parallelMerge(int[] A, int[] B, int[] C, int numThreads) {
        ForkJoinPool pool = new ForkJoinPool(numThreads);
        for (int i = 0; i < A.length; i++) {
            pool.invoke(new PMerge(i, A[i], true, B, C));
        }
        for (int i = 0; i < B.length; i++) {
            pool.invoke(new PMerge(i, B[i], false, A, C));
        }
    }

    @Override
    protected void compute() {
        int left = 0;
        int right = this.other.length;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if ((this.precedence && this.val < this.other[mid]) || (!this.precedence && this.val <= this.other[mid])) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        // left is now pointing to the first number bigger than value
        int finalIndex = this.C.length - (this.pos + left) - 1;
        this.C[finalIndex] = this.val;
    }
}
