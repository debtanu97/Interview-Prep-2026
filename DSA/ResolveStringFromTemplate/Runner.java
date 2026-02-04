package prb2;

import java.util.Objects;

public class Runner {
    public static void main(String[] args) {
        ResolveStringTemplateInput[] testSuite = ResolveStringTemplate.getTestSuite();
        String[] testSuiteRes = ResolveStringTemplate.getTestRes();

        for(int i=0; i<testSuite.length; i++) {
            ResolveStringTemplate sol =
                    new ResolveStringTemplate(testSuite[i]);

            if(Objects.equals(sol.solution(), testSuiteRes[i])) {
                System.out.println("PASSED");
            } else {
                System.out.println("FAILED");
                System.out.println("Expected : " + testSuiteRes[i] + " Actual : " + sol.solution());
            }
        }
    }
}
