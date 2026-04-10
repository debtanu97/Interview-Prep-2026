package problem.solving.dsa;

import java.util.*;

public class RottenOranges {
    private final int[][] grid;

    public RottenOranges(int[][] grid) {
        this.grid = grid;
    }

    public int solution() {
        int m = grid.length;
        int n = grid[0].length;

        Queue<int[]> q = new LinkedList<>();
        int fresh = 0;

        // Step 1: initialize queue + count fresh oranges
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 2) {
                    q.add(new int[]{i, j});
                } else if (grid[i][j] == 1) {
                    fresh++;
                }
            }
        }

        int time = 0;
        int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        // Step 2: BFS
        while (!q.isEmpty() && fresh > 0) {
            int size = q.size();

            while (size-- > 0) {
                int[] cur = q.poll();

                for (int[] d : dirs) {
                    int ni = cur[0] + d[0];
                    int nj = cur[1] + d[1];

                    if (ni >= 0 && nj >= 0 && ni < m && nj < n && grid[ni][nj] == 1) {
                        grid[ni][nj] = 2;
                        fresh--;
                        q.add(new int[]{ni, nj});
                    }
                }
            }
            time++;
        }

        return fresh == 0 ? time : -1;
    }

    public static int[][][] getTestSuite() {
        return new int[][][]{
                {{2,1,1},{1,1,0},{0,1,1}}, // 0 -> 4
                {{2,1,1},{0,1,1},{1,0,1}}, // 1 -> -1
                {{0,2}},                   // 2 -> 0
                {{1,1},{1,1}},             // 3 -> -1
                {{2,2},{2,2}},             // 4 -> 0
        };
    }

    public static Integer[] getTestRes() {
        return new Integer[]{
                4,
                -1,
                0,
                -1,
                0
        };
    }
}
