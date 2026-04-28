package problem.solving;

import static problem.solving.BinarySeriesSum.getTestResults;
import static problem.solving.BinarySeriesSum.getTestSuite;

public class Runner {
    public static void main(String[] args) {
        /*---------------------------Binary Series Sum Runner-------------------------------*/

        Integer[] inputs   = BinarySeriesSum.getTestSuite();
        Integer[] expected = BinarySeriesSum.getTestResults();

        int passed = 0, failed = 0;
        System.out.println("=".repeat(60));
        System.out.println("  BinarySeriesSum Test Runner");
        System.out.println("=".repeat(60));
        System.out.printf("%n%-8s %-14s %-14s %-8s%n", "N", "Expected", "DP Result", "Pass?");
        System.out.println("-".repeat(48));

        for (int i = 0; i < inputs.length; i++) {
            int n   = inputs[i];
            int exp = expected[i];
            int got = new BinarySeriesSum(n).solution();
            boolean pass = got == exp;
            if (pass) passed++; else failed++;
            System.out.printf("%-8d %-14d %-14d %-8s%n",
                    n, exp, got, pass ? "PASS ✓" : "FAIL ✗");
        }

        System.out.println("-".repeat(48));
        System.out.printf("%nResults: %d passed, %d failed%n", passed, failed);
        System.out.println("=".repeat(60));

        /*---------------------------Implement Max Stack Runner-------------------------------*/

        MaxStack.TestCase[] suite = MaxStack.getTestSuite();

        passed = 0;
        failed = 0;
        System.out.println("=".repeat(60));
        System.out.println("  MaxStack Test Runner");
        System.out.println("=".repeat(60));

        for (int t = 0; t < suite.length; t++) {
            MaxStack.TestCase tc = suite[t];
            MaxStack stack = new MaxStack();

            boolean testPassed = true;
            StringBuilder log = new StringBuilder();

            for (int i = 0; i < tc.ops.length; i++) {
                String op  = tc.ops[i];
                int    arg = tc.args[i];
                Integer exp = tc.expected[i];

                Integer actual = null;
                switch (op) {
                    case "push"      -> stack.push(arg);
                    case "pop"       -> actual = stack.pop();
                    case "peek"      -> actual = stack.peek();
                    case "peekMax"   -> actual = stack.peekMax();
                    case "removeMax" -> actual = stack.removeMax();
                    default          -> throw new IllegalArgumentException("Unknown op: " + op);
                }

                if (exp != null) {
                    boolean match = exp.equals(actual);
                    if (!match) {
                        testPassed = false;
                        log.append(String.format(
                                "  [FAIL] step %d: %s(%s) → expected %d, got %d%n",
                                i, op, op.equals("push") ? String.valueOf(arg) : "",
                                exp, actual));
                    } else {
                        log.append(String.format(
                                "  [OK]   step %d: %s → %d%n", i, op, actual));
                    }
                } else {
                    log.append(String.format(
                            "  [OK]   step %d: push(%d)%n", i, arg));
                }
            }

            String status = testPassed ? "PASS ✓" : "FAIL ✗";
            System.out.printf("%nTest %d: %-44s [%s]%n", t + 1, tc.label, status);
            if (!testPassed) {
                System.out.print(log);
                failed++;
            } else {
                passed++;
            }
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.printf("  Results: %d passed, %d failed%n", passed, failed);
        System.out.println("=".repeat(60));
    }
}
