import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SequentialLogAnalyzer {
    private static final List<String> LOG_LEVELS = Arrays.asList("TRACE", "DEBUG", "INFO", "WARN", "ERROR");

    public static void main(String[] args) {
        String logDirectory = "/Users/michaelhansler/Library/CloudStorage/OneDrive-HTLLeoben/HTL/Schuljahr 2024-2025/POS/POS Projekte/mt-executerservice-loglevelanalyzer-michi-haensler/"; // Ordner mit Logdateien
        File folder = new File(logDirectory);
        File[] logFiles = folder.listFiles((dir, name) -> name.endsWith(".log"));

        if (logFiles == null || logFiles.length == 0) {
            System.out.println("Keine Logdateien gefunden.");
            return;
        }

        long startTime = System.currentTimeMillis();
        Map<String, Integer> totalCounts = new HashMap<>();
        LOG_LEVELS.forEach(level -> totalCounts.put(level, 0));

        for (File file : logFiles) {
            Map<String, Integer> fileCounts = analyzeLogFile(file);
            System.out.println("Ergebnisse für Datei: " + file.getName());
            fileCounts.forEach((level, count) -> System.out.println(level + ": " + count));
            System.out.println();

            // Gesamtzähler aktualisieren
            fileCounts.forEach((level, count) -> totalCounts.put(level, totalCounts.get(level) + count));
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Gesamtzusammenfassung:");
        totalCounts.forEach((level, count) -> System.out.println(level + ": " + count));
        System.out.println("Ausführungszeit (ms): " + (endTime - startTime));
    }

    private static Map<String, Integer> analyzeLogFile(File file) {
        Map<String, Integer> counts = new HashMap<>();
        LOG_LEVELS.forEach(level -> counts.put(level, 0));

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String level : LOG_LEVELS) {
                    if (line.contains(level)) {
                        counts.put(level, counts.get(level) + 1);
                        break; // Ein Level pro Zeile zählen
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Datei " + file.getName() + ": " + e.getMessage());
        }
        return counts;
    }
}
