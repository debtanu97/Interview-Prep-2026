package problem.solving.dsa;

public class HouseRobber {
    private final int[] nums;

    public HouseRobber(int[] nums) {
        this.nums = nums;
    }

    public int solution() {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];

        int prev2 = nums[0];
        int prev1 = Math.max(nums[0], nums[1]);

        for (int i = 2; i < nums.length; i++) {
            int cur = Math.max(prev1, nums[i] + prev2);
            prev2 = prev1;
            prev1 = cur;
        }

        return prev1;
    }

    public static int[][] getTestSuite() {
        return new int[][]{
                {},             // 0
                {1},            // 1
                {1,2},          // 2
                {2,1,1,2},      // 3
                {2,7,9,3,1},    // 4
                {5,5,10,100,10,5} // 5
        };
    }

    public static Integer[] getTestRes() {
        return new Integer[]{
                0,
                1,
                2,
                4,
                12,
                110
        };
    }
}
