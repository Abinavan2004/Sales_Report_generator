import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileSalesReader implements SalesReader {

    @Override
    public List<Product> readSales(String filePath) throws IOException {
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine(); // Header row
            if (header == null) {
                throw new IOException("CSV file is empty: " + filePath);
            }

            int lineNumber = 1;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] tokens = line.split(",");
                if (tokens.length < 5) {
                    System.err.println("Warning: Skipping line " + lineNumber +
                            " due to missing columns (expected 5, found " + tokens.length + "): " + line);
                    continue;
                }

                try {
                    String id = tokens[0].trim();
                    String name = tokens[1].trim();
                    String category = tokens[2].trim();
                    int qty = Integer.parseInt(tokens[3].trim());
                    double price = Double.parseDouble(tokens[4].trim());

                    if (qty < 0 || price < 0) {
                        System.err.println("Warning: Skipping line " + lineNumber +
                                " due to negative quantity or price: " + line);
                        continue;
                    }

                    products.add(new Product(id, name, category, qty, price));
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Skipping line " + lineNumber +
                            " due to invalid numeric value: " + line);
                }
            }
        }

        if (products.isEmpty()) {
            throw new IOException("No valid sales records found in CSV file: " + filePath);
        }

        return products;
    }
}
