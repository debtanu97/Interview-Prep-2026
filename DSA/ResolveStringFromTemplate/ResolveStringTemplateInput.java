package prb2;

import java.util.Map;

public class ResolveStringTemplateInput {
    private String template;

    public ResolveStringTemplateInput(String template, Map<String, String> mapping) {
        this.template = template;
        this.mapping = mapping;
    }

    private Map<String, String> mapping;

    public String getTemplate() {
        return template;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }
}
