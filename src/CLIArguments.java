/**
 * Immutable data holder representing validated command-line arguments.
 */
public class CLIArguments {
    private final String csvFilePath;
    private final String outputMethod;
    private final String outputFilePath;

    public CLIArguments(String csvFilePath, String outputMethod, String outputFilePath) {
        this.csvFilePath = csvFilePath;
        this.outputMethod = outputMethod.toLowerCase();
        this.outputFilePath = outputFilePath;
    }

    public String getCsvFilePath() {
        return csvFilePath;
    }

    public String getOutputMethod() {
        return outputMethod;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public boolean isFileOutput() {
        return "file".equalsIgnoreCase(outputMethod);
    }

    public boolean isConsoleOutput() {
        return "console".equalsIgnoreCase(outputMethod);
    }
}
