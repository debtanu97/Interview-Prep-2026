package problem.solving;

import java.util.*;

/**
 * Problem:
 * Given a number N, find the minimum number of terms from the "binary-only decimal"
 * series [1, 10, 11, 100, 101, 110, 111, ...] whose sum equals N.
 * These are decimal numbers that contain only the digits 0 and 1.
 *
 * Example: N=18 → 11 + 1 + 1 + 1 + 1 + 1 + 1 + 1 = 8 terms (minimum)
 */
public class BinarySeriesSum {

    private final int N;

    public BinarySeriesSum(int N) {
        this.N = N;
    }

    // Generates all binary-decimal numbers <= limit in sorted order.
    // Uses BFS: root=1, children of x are x*10 and x*10+1.
    private List<Integer> generateTerms(int limit) {
        List<Integer> terms = new ArrayList<>();
        Queue<Long> queue = new LinkedList<>();
        queue.add(1L);
        while (!queue.isEmpty()) {
            long curr = queue.poll();
            if (curr > limit) continue;
            terms.add((int) curr);
            queue.add(curr * 10);
            queue.add(curr * 10 + 1);
        }
        Collections.sort(terms);
        return terms;
    }

    public int solution() {
        List<Integer> terms = generateTerms(N);

        int[] dp = new int[N + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i <= N; i++) {
            for (int term : terms) {
                if (term > i) break;
                if (dp[i - term] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], dp[i - term] + 1);
                }
            }
        }

        return dp[N];
    }

    public static Integer[] getTestSuite() {
        return new Integer[]{
                1,    // 1                    → 1
                2,    // 1+1                  → 2
                10,   // 10                   → 1
                11,   // 11                   → 1
                18,   // 11+1*7               → 8
                20,   // 10+10                → 2
                22,   // 11+11                → 2
                99,   // 11*9                 → 9
                100,  // 100                  → 1
                123,  // 111+11+1             → 3
                999,  // 111*9                → 9
        };
    }

    public static Integer[] getTestResults() {
        return new Integer[]{
                1,  // N=1
                2,  // N=2
                1,  // N=10
                1,  // N=11
                8,  // N=18
                2,  // N=20
                2,  // N=22
                9,  // N=99
                1,  // N=100
                3,  // N=123
                9,  // N=999
        };
    }
}

