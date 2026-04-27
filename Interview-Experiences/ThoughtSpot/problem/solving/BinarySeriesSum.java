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

    // ------------------------------------------------------------------
    // Approach 1 (Brute Force / Naive DP):
    //   Generate all binary-decimal numbers <= N via BFS expansion,
    //   then run classic coin-change DP.
    //   Time:  O(N * K) where K = number of binary-decimal terms <= N
    //   Space: O(N + K)
    // ------------------------------------------------------------------
    public int solutionDP() {
        List<Integer> terms = generateTerms(N);

        int[] dp = new int[N + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i <= N; i++) {
            for (int term : terms) {
                if (term > i) break; // terms are sorted
                if (dp[i - term] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], dp[i - term] + 1);
                }
            }
        }

        return dp[N];
    }

    // Generates all binary-decimal numbers <= N in sorted order.
    // Uses BFS: start from 1, expand by appending '0' or '1'.
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

    // ------------------------------------------------------------------
    // Approach 2 (Greedy / Optimal):
    //   Key insight: Each decimal digit d of N requires exactly d binary-decimal
    //   terms to cover it at that positional value (e.g., digit 7 → seven 1s,
    //   or seven 10s, etc.). The minimum total count is simply the digit sum of N.
    //
    //   Why it works: Any binary-decimal number contributes a single 1-digit to
    //   exactly one (or more) decimal positions. The tightest packing aligns one
    //   term per unit of each digit. Therefore sum-of-digits is both achievable
    //   and minimal.
    //
    //   Time:  O(log N)  — one pass over digits
    //   Space: O(1)
    // ------------------------------------------------------------------
    public int solutionGreedy() {
        int digitSum = 0;
        int n = N;
        while (n > 0) {
            digitSum += n % 10;
            n /= 10;
        }
        return digitSum;
    }

    // ------------------------------------------------------------------
    // Test suite
    // ------------------------------------------------------------------
    public static Integer[] getTestSuite() {
        // Each entry is an N value to test
        return new Integer[]{
                1,    // 1 → 1
                2,    // 1+1 → 2
                10,   // 10 → 1
                11,   // 11 → 2
                18,   // 11+1+1+1+1+1+1+1 → 8
                20,   // 11+1+1+...  → 2+0 = 2
                99,   // 9+9 → 18
                100,  // 100 → 1
                123,  // 1+2+3 → 6
                999,  // 9+9+9 → 27
        };
    }

    public static Integer[] getTestResults() {
        return new Integer[]{
                1,   // N=1
                2,   // N=2
                1,   // N=10
                2,   // N=11
                8,   // N=18
                2,   // N=20
                18,  // N=99
                1,   // N=100
                6,   // N=123
                27,  // N=999
        };
    }
}

