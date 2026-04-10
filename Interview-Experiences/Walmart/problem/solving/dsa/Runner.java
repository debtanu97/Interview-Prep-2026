package problem.solving.dsa;

import java.util.Arrays;
import java.util.Objects;

public class Runner {
    public static void main(String[] args) {

        // -------- Rotten Oranges --------
        System.out.println("==== Rotten Oranges Tests ====");
        int[][][] roTests = RottenOranges.getTestSuite();
        Integer[] roRes = RottenOranges.getTestRes();

        for (int i = 0; i < roTests.length; i++) {
            int[][] inputCopy = deepCopy(roTests[i]);
            RottenOranges sol = new RottenOranges(inputCopy);

            int result = sol.solution();

            if (Objects.equals(result, roRes[i])) {
                System.out.println("PASSED");
            } else {
                System.out.println("FAILED");
                System.out.println("Expected: " + roRes[i] + " Actual: " + result);
            }
        }

        // -------- House Robber --------
        System.out.println("\n==== House Robber Tests ====");
        int[][] hrTests = HouseRobber.getTestSuite();
        Integer[] hrRes = HouseRobber.getTestRes();

        for (int i = 0; i < hrTests.length; i++) {
            HouseRobber sol = new HouseRobber(hrTests[i]);

            int result = sol.solution();

            if (Objects.equals(result, hrRes[i])) {
                System.out.println("PASSED");
            } else {
                System.out.println("FAILED");
                System.out.println("Expected: " + hrRes[i] + " Actual: " + result);
            }
        }
    }

    private static int[][] deepCopy(int[][] grid) {
        return Arrays.stream(grid)
                .map(int[]::clone)
                .toArray(int[][]::new);
    }
}
