import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileReportExporter implements ReportExporter {
    private final String filePath;

    public FileReportExporter(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void export(String content) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.print(content);
        }
        System.out.println("Report successfully written to: " + filePath);
    }
}
