package com.evampsanga.assignment.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicConfiguration {
    private String fileNamePattern;
    private List<DynamicConfigurationField> fields;
    private Map<String, Map<String, String>> mappings;

    public boolean validateFilename(String input) {
        return input.matches(fileNamePattern);
    }

    public String tryMap(String mappingKey, String input) {
        if (!mappings.containsKey(mappingKey)) {
            return null;
        }
        return mappings.get(mappingKey).getOrDefault(input, null);
    }

}
