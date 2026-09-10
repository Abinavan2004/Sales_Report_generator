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
            String line = reader.readLine(); // Skip header row

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split(",");
                if (tokens.length < 5) continue;

                String id = tokens[0].trim();
                String name = tokens[1].trim();
                String category = tokens[2].trim();
                int qty = Integer.parseInt(tokens[3].trim());
                double price = Double.parseDouble(tokens[4].trim());

                products.add(new Product(id, name, category, qty, price));
            }
        }
        return products;
    }
}
