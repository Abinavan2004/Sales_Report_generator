import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        String csvFilePath = (args.length > 0) ? args[0] : "src\\sales.csv";
        String outputMethod = (args.length > 1) ? args[1] : "console";
        String outputFilePath = (args.length > 2) ? args[2] : "report.txt";

        // 1. Connect to FileSalesReader using the SalesReader interface object
        SalesReader salesReader = new FileSalesReader();
        List<Product> products = salesReader.readSales(csvFilePath);

        // 2. Analyze the products data
        SalesAnalyzer analyzer = new SalesAnalyzer(products);
        SalesSummary summary = analyzer.analyze();

        // 3. Generate the summary report
        SalesReportGenerator generator = new SalesReportGenerator();
        String report = generator.generateReport(products, summary);

        // 4. Output the report (File or Console)
        ReportExporter exporter = "file".equalsIgnoreCase(outputMethod)
                ? new FileReportExporter(outputFilePath)
                : new ConsoleReportExporter();

        exporter.export(report);
    }
}
