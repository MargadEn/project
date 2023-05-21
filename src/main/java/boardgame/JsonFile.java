package boardgame;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.tinylog.Logger;

public class JsonFile {
    private File file = new File("gameStat.json");
    private ObjectMapper mapper = new ObjectMapper();
    private static JsonFile jsonFile = new JsonFile();

    private JsonFile() {
        mapper.registerModule(new JavaTimeModule());
    }

    public static JsonFile getInstance() {
        return jsonFile;
    }

    public void appendToList(GameStatistics info) {
        try {
            List<GameStatistics> statisticsList = new ArrayList<>();
            if (file.exists()) {
                try (FileReader reader = new FileReader(file)) {
                    statisticsList = mapper.readValue(reader, new TypeReference<List<GameStatistics>>() {});
                }
            }
            try (FileWriter writer = new FileWriter(file)) {
                statisticsList.add(info);
                mapper.writeValue(writer, statisticsList);
                Logger.info("Writing to the file");
            }
        } catch (Exception e) {
            Logger.error(e.getStackTrace());
        }
    }

    public List<Map.Entry<String, Integer>> getTopScoreList() {
        Map<String, Integer> statisticsMap = new HashMap<>();
        try (FileReader reader = new FileReader(file)) {
            List<GameStatistics> statisticsList = mapper.readValue(reader, new TypeReference<List<GameStatistics>>() {});
            statisticsList.forEach(element -> {
                statisticsMap.merge(element.getWinner(), 1, Integer::sum);
            });
        } catch (Exception e) {
            Logger.error(e.getStackTrace());
        }
        return sortMap(statisticsMap).stream().limit(5).toList();
    }

    private List<Map.Entry<String, Integer>> sortMap(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (firstElement, secondElement) -> secondElement.getValue().compareTo(firstElement.getValue()));
        return list;
    }
}
