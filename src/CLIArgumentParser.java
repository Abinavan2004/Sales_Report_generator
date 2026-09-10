import java.io.File;

/**
 * Handles command-line argument parsing and input validation for SalesReporter.
 * Follows the Single Responsibility Principle (SRP).
 */
public class CLIArgumentParser {

    public static CLIArguments parse(String[] args) throws InvalidInputException {
        if (args == null || args.length < 2) {
            throw new InvalidInputException(
                    "Missing or insufficient command-line arguments.\n" + getUsageInstructions()
            );
        }

        String csvFilePath = args[0].trim();
        if (csvFilePath.isEmpty()) {
            throw new InvalidInputException("Input CSV file path cannot be empty.");
        }

        // Validate input CSV file existence and readability
        File csvFile = new File(csvFilePath);
        if (!csvFile.exists()) {
            throw new InvalidInputException("Input CSV file does not exist: " + csvFilePath);
        }
        if (csvFile.isDirectory()) {
            throw new InvalidInputException("Input path is a directory, not a file: " + csvFilePath);
        }
        if (!csvFile.canRead()) {
            throw new InvalidInputException("Input CSV file cannot be read (permission denied): " + csvFilePath);
        }

        // Validate output method
        String outputMethod = args[1].trim();
        if (!"console".equalsIgnoreCase(outputMethod) && !"file".equalsIgnoreCase(outputMethod)) {
            throw new InvalidInputException(
                    "Invalid output method '" + outputMethod + "'. Supported values are 'console' or 'file' (case-insensitive)."
            );
        }

        // Validate output file path if output method is 'file'
        String outputFilePath = null;
        if ("file".equalsIgnoreCase(outputMethod)) {
            if (args.length < 3 || args[2].trim().isEmpty()) {
                throw new InvalidInputException(
                        "Output file path is mandatory when output method is 'file'.\n" +
                        "Usage: java SalesReporter <csv-file-path> file <output-file-path>"
                );
            }
            outputFilePath = args[2].trim();
        }

        return new CLIArguments(csvFilePath, outputMethod, outputFilePath);
    }

    public static String getUsageInstructions() {
        return "Usage: java SalesReporter <csv-file-path> <output-method> [output-file-path]\n" +
               "  <csv-file-path>    : Path to the sales CSV data file (must exist and be readable)\n" +
               "  <output-method>    : Output destination ('console' or 'file', case-insensitive)\n" +
               "  [output-file-path] : Path for the generated report (mandatory when output-method is 'file')";
    }
}
