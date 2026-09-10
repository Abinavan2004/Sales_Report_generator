import java.io.File;
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
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Could not create directories for destination path: " + filePath);
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.print(content);
        }
        System.out.println("Report successfully written to: " + filePath);
    }
}
