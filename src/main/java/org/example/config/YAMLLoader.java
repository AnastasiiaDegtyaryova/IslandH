package org.example.config;

import org.example.animals.AnimalData;
import org.example.animals.AnimalType;
import org.yaml.snakeyaml.Yaml;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class YAMLLoader {

    public List<AnimalData> loadAnimalData(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Yaml yaml = new Yaml();
            List<LinkedHashMap<?, ?>> rawList = yaml.load(reader);

            return rawList.stream()
                    .map(this::mapToAnimalData)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load animal data from YAML file: " + filePath, e);
        } catch (ClassCastException e) {
            throw new ConfigurationException("Invalid format in animal data YAML file: " + filePath, e);
        }
    }

    public Map<String, Map<String, Integer>> loadEatingChances(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Yaml yaml = new Yaml();
            Map<String, Object> rawMap = yaml.load(reader);

            return rawMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> ((Map<?, ?>) entry.getValue()).entrySet().stream()
                                    .collect(Collectors.toMap(
                                            e -> (String) e.getKey(),
                                            e -> ((Number) e.getValue()).intValue()
                                    ))
                    ));
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load eating chances from YAML file: " + filePath, e);
        } catch (ClassCastException e) {
            throw new ConfigurationException("Invalid format in eating chances YAML file: " + filePath, e);
        }
    }

    private AnimalData mapToAnimalData(LinkedHashMap<?, ?> map) {
        return new AnimalData(
                AnimalType.valueOf((String) map.get("type")),
                (String) map.get("name"),
                ((Number) map.get("weight")).doubleValue(),
                ((Number) map.get("maxPopulation")).intValue(),
                ((Number) map.get("maxSpeed")).intValue(),
                ((Number) map.get("foodRequirement")).doubleValue()
        );
    }
}
