import java.io.IOException;
import java.util.List;

/**
 * Main application entry point for the SalesReporter CLI tool.
 * Handles command-line arguments, orchestrates reader, analyzer,
 * generator, and exporter components, and implements global exception handling.
 */
public class SalesReporter {

    public static void main(String[] args) {
        try {
            run(args);
        } catch (InvalidInputException e) {
            System.err.println("Input Error: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Executes the sales reporting pipeline.
     *
     * @param args Command-line arguments.
     * @throws InvalidInputException if CLI arguments or input paths are invalid.
     * @throws IOException           if reading the CSV or writing the report fails.
     */
    public static void run(String[] args) throws InvalidInputException, IOException {
        // 1. Parse and validate CLI arguments
        CLIArguments arguments = CLIArgumentParser.parse(args);

        // 2. Read sales data using SalesReader interface
        SalesReader reader = new FileSalesReader();
        List<Product> products = reader.readSales(arguments.getCsvFilePath());

        // 3. Analyze sales metrics
        SalesAnalyzer analyzer = new SalesAnalyzer(products);
        SalesSummary summary = analyzer.analyze();

        // 4. Generate formatted report
        SalesReportGenerator generator = new SalesReportGenerator();
        String report = generator.generateReport(products, summary);

        // 5. Export report using appropriate ReportExporter strategy
        ReportExporter exporter = createExporter(arguments);
        exporter.export(report);
    }

    /**
     * Factory method creating the appropriate ReportExporter based on user choice.
     */
    private static ReportExporter createExporter(CLIArguments arguments) {
        if (arguments.isFileOutput()) {
            return new FileReportExporter(arguments.getOutputFilePath());
        } else {
            return new ConsoleReportExporter();
        }
    }
}
