public class MaxLengthSubstringWithFirstCharacterLessThanLast {
    private final String input;

    public MaxLengthSubstringWithFirstCharacterLessThanLast(String input) {
        this.input = input;
    }

    public int solution() {
        if(input == null || input.length() < 2) {
            return 0;
        }

        int n = input.length();
        int[] firstPos = new int[26];
        java.util.Arrays.fill(firstPos, -1);

        int ans = 0;

        for(int r=0; r<n; r++) {
            int c = input.charAt(r) - 'a';

            // Find the earliest index of any smaller character
            int minIndex = Integer.MAX_VALUE;
            for(int ch=0; ch<c; ch++) {
                if(firstPos[ch] != -1) {
                    minIndex = Math.min(minIndex, firstPos[ch]);
                }
            }

            if(minIndex != Integer.MAX_VALUE) {
                ans = Math.max(ans, r - minIndex + 1);
            }

            if(firstPos[c] == -1) {
                firstPos[c] = r;
            }
        }

        return ans;
    }

    public String getInput() {
        return input;
    }

    public static String[] getTestSuite() {
        return new String[]{
                "",                 // 0
                "a",                // 1
                "aaaa",             // 2
                "abcd",             // 3
                "dcba",             // 4
                "ab",               // 5
                "ba",               // 6
                "dbabcb",           // 7
                "zzza",             // 8
                "azzz",             // 9
                "aabbbc",           // 10
                "bca",              // 11
                "cbaac",            // 12
                "abcabc",           // 13
                "zabcdefghijklmnopqrstuvwxy" // 14
        };
    }

    public static Integer[] getTestRes() {
        return new Integer[]{
                0,  // ""
                0,  // "a"
                0,  // "aaaa"
                4,  // "abcd"
                0,  // "dcba"
                2,  // "ab"
                0,  // "ba"
                4,  // "dbabcb"
                0,  // "zzza"
                4,  // "azzz"
                6,  // "aabbbc"
                2,  // "bca"
                4,  // "cbaac"
                6,  // "abcabc"
                25  // long increasing suffix//
        };
    }
}
