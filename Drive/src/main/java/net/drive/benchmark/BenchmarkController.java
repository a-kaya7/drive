package net.drive.benchmark;

import net.drive.benchmark.aspect.LayerTimingAspectFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class BenchmarkController {

    private static final String OUTPUT_FILE = "benchmark_and_metrics.txt";
    private static final long DEFAULT_LAYER_THRESHOLD_MS = 100;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/lasttest")
    public String runBenchmark() {
        String abPath = "ab";
        String url = "http://localhost:8080/api/fahrschuelerneuanlage";

        try {
            // ApacheBench
            ProcessBuilder pb = new ProcessBuilder(abPath, "-n", "500", "-c", "50", url);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            double meanTime = -1;

            Pattern pattern = Pattern.compile("Time per request:\\s+([0-9\\.]+) \\[ms\\] \\(mean\\)");

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    meanTime = Double.parseDouble(matcher.group(1));
                    System.out.println("Parsed meanTime: " + meanTime);
                }
            }
            process.waitFor();

        
            try (FileWriter writer = new FileWriter(OUTPUT_FILE, true)) {
                writer.write("=== Apache Bench Output ===\n");
                writer.write(output.toString());
                writer.write("\n");
            }

            // wenn Endpunkt langsam ist.
            if (meanTime > DEFAULT_LAYER_THRESHOLD_MS) {
                LayerTimingAspectFile.startMeasurement();

                // Endpunt wird aufgerufen.
                restTemplate.getForObject(url, String.class);

                // Messung ist fertig...
                LayerTimingAspectFile.stopMeasurement();
            }else{
                try (FileWriter writer = new FileWriter(OUTPUT_FILE, true)){
                    writer.write("Mean Time ist "+ meanTime + "ms, also wird kein Layer Benchmark durchgeführt.\n");
                }
            }
            

            return "Benchmark wurde erfolgreich abgeschlossen!! Outputs wurden in die Datei geschrieben";

        } catch (Exception e) {
            e.printStackTrace();
            return "Benchmark ist fehlgeschlagen " + e.getMessage();
        }
    }
}
