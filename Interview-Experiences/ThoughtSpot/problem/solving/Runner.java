package problem.solving;

import static problem.solving.BinarySeriesSum.getTestResults;
import static problem.solving.BinarySeriesSum.getTestSuite;

public class Runner {
    public static void main(String[] args) {
        Integer[] inputs  = getTestSuite();
        Integer[] expected = getTestResults();

        System.out.printf("%-8s %-12s %-12s %-12s %-8s%n",
                "N", "Expected", "DP", "Greedy", "Pass?");
        System.out.println("-".repeat(56));

        boolean allPassed = true;
        for (int i = 0; i < inputs.length; i++) {
            int n = inputs[i];
            BinarySeriesSum problem = new BinarySeriesSum(n);

            int dp     = problem.solutionDP();
            int greedy = problem.solutionGreedy();
            boolean pass = dp == expected[i] && greedy == expected[i];
            allPassed &= pass;

            System.out.printf("%-8d %-12d %-12d %-12d %-8s%n",
                    n, expected[i], dp, greedy, pass ? "✓" : "✗ FAIL");
        }

        System.out.println("-".repeat(56));
        System.out.println(allPassed ? "All tests passed ✓" : "Some tests FAILED ✗");
    }
}
