package prb2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResolveStringTemplate {
    private final ResolveStringTemplateInput input;

    public ResolveStringTemplate(ResolveStringTemplateInput input) {
        this.input = input;
    }

    public String solution() {
        Map<String, String> mapping = input.getMapping();

        // Cache for resolved keys
        Map<String, String> resolved = new HashMap<>();

        // Track recursion stack for cycle detection
        Set<String> visiting = new HashSet<>();

        // Resolve the main template
        try {
            return resolveString(input.getTemplate(), resolved, visiting);
        } catch (CycleDetectedException e) {
            return "EXCEPTION";
        }
    }

    private String resolveString(String s, Map<String,String> resolved, Set<String> visited) {
        StringBuilder res = new StringBuilder();
        int i=0;

        while(i < s.length()) {
            if(s.charAt(i) == '%') {
                i++;
                StringBuilder key = new StringBuilder();
                while (i < s.length() && s.charAt(i) != '%') {
                    key.append(s.charAt(i));
                    i++;
                }
                i++; // skip closing %

                res.append(resolveKey(key.toString(), resolved, visited));
            } else {
                res.append(s.charAt(i));
                i++;
            }
        }

        return res.toString();
    }

    private String resolveKey(String key, Map<String,String> resolved, Set<String> visited) {
        Map<String, String> mappings = input.getMapping();

        // Already resolved
        if (resolved.containsKey(key)) {
            return resolved.get(key);
        }

        // Cycle detected
        if (visited.contains(key)) {
            throw new CycleDetectedException("Cycle detected involving key: " + key);
        }

        visited.add(key);

        String value = mappings.getOrDefault(key, "");
        String resolvedValue = resolveString(value, resolved, visited);

        visited.remove(key);
        resolved.put(key, resolvedValue);

        return resolvedValue;
    }

    public ResolveStringTemplateInput getInput() {
        return input;
    }

    public static ResolveStringTemplateInput[] getTestSuite() {
        return new ResolveStringTemplateInput[]{

                // 1. Deep dependency chain
                new ResolveStringTemplateInput(
                        "%A%",
                        java.util.Map.of(
                                "A", "%B%",
                                "B", "%C%",
                                "C", "%D%",
                                "D", "end"
                        )
                ),

                // 2. Multiple placeholders inside a single value
                new ResolveStringTemplateInput(
                        "%A%",
                        java.util.Map.of(
                                "A", "%B%-%C%",
                                "B", "left",
                                "C", "right"
                        )
                ),

                // 3. Multiple placeholders in the template
                new ResolveStringTemplateInput(
                        "Values: %A%, %B%, %C%",
                        java.util.Map.of(
                                "A", "1",
                                "B", "%A%2",
                                "C", "%B%3"
                        )
                ),

                // 4. Shared dependencies
                new ResolveStringTemplateInput(
                        "%X% and %Y%",
                        java.util.Map.of(
                                "X", "%Z%",
                                "Y", "%Z%",
                                "Z", "shared"
                        )
                ),

                // 5. Partial resolution (missing key deep in chain)
                new ResolveStringTemplateInput(
                        "%A%",
                        java.util.Map.of(
                                "A", "%B%",
                                "B", "%C%"
                                // C missing
                        )
                ),

                // 6. Mixed literals and placeholders
                new ResolveStringTemplateInput(
                        "Hello %A%!",
                        java.util.Map.of(
                                "A", "Mr. %B%",
                                "B", "Smith"
                        )
                ),

                // 7. Indirect cycle
                new ResolveStringTemplateInput(
                        "%A%",
                        java.util.Map.of(
                                "A", "%B%",
                                "B", "%C%",
                                "C", "%A%"
                        )
                ),

                // 8. Self cycle
                new ResolveStringTemplateInput(
                        "%A%",
                        java.util.Map.of(
                                "A", "%A%"
                        )
                ),

                // 9. Large fan-out dependency
                new ResolveStringTemplateInput(
                        "%ROOT%",
                        java.util.Map.of(
                                "ROOT", "%A%-%B%-%C%-%D%",
                                "A", "1",
                                "B", "2",
                                "C", "3",
                                "D", "4"
                        )
                ),

                // 10. Repeated placeholders (memoization stress test)
                new ResolveStringTemplateInput(
                        "%A% %A% %A%",
                        java.util.Map.of(
                                "A", "%B%",
                                "B", "%C%",
                                "C", "final"
                        )
                )
        };
    }

    public static String[] getTestRes() {
        return new String[]{
                "end",                    // 1
                "left-right",             // 2
                "Values: 1, 12, 123",     // 3
                "shared and shared",      // 4
                "",                       // 5 (missing C → empty)
                "Hello Mr. Smith!",       // 6
                "EXCEPTION",              // 7 (indirect cycle)
                "EXCEPTION",              // 8 (self cycle)
                "1-2-3-4",                // 9
                "final final final"       // 10
        };
    }
}
