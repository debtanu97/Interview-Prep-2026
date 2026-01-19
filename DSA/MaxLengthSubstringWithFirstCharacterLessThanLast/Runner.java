import java.util.Objects;

public class Runner {
    public static void main(String[] args) {
        String[] testSuite = MaxLengthSubstringWithFirstCharacterLessThanLast.getTestSuite();
        Integer[] testSuiteRes = MaxLengthSubstringWithFirstCharacterLessThanLast.getTestRes();

        for(int i=0; i<testSuite.length; i++) {
            MaxLengthSubstringWithFirstCharacterLessThanLast sol =
                    new MaxLengthSubstringWithFirstCharacterLessThanLast(testSuite[i]);

            if(Objects.equals(sol.solution(), testSuiteRes[i])) {
                System.out.println("PASSED");
            } else {
                System.out.println("FAILED");
                System.out.println("Expected : " + testSuiteRes[i] + " Actual : " + sol.solution());
            }
        }
    }
}
