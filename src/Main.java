import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {


        public static void main(String[] args) throws Exception {
            // If no arguments provided in IntelliJ, use defaults:
            String csvFilePath = (args.length > 0) ? args[0] : "sales.csv";
            String outputMethod = (args.length > 1) ? args[1] : "console";
            String outputFilePath = (args.length > 2) ? args[2] : "report.txt";


            // Data storage
        List<String> productIds = new ArrayList<>();
        List<String> productNames = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        List<Double> unitPrices = new ArrayList<>();
        List<Double> productRevenues = new ArrayList<>();

        Map<String, Double> categoryRevenues = new LinkedHashMap<>();

        // Read CSV file
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\SAKTHIMICRO\\IdeaProjects\\Sales_Report\\src\\sales.csv"));
        String line = reader.readLine(); // Skip header row

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] tokens = line.split(",");
            String id = tokens[0].trim();
            String name = tokens[1].trim();
            String category = tokens[2].trim();
            int qty = Integer.parseInt(tokens[3].trim());
            double price = Double.parseDouble(tokens[4].trim());

            double revenue = qty * price;

            productIds.add(id);
            productNames.add(name);
            categories.add(category);
            quantities.add(qty);
            unitPrices.add(price);
            productRevenues.add(revenue);

            // Accumulate category revenue
            categoryRevenues.put(category, categoryRevenues.getOrDefault(category, 0.0) + revenue);
        }
        reader.close();

        // Computations
        double grandTotal = 0.0;
        int maxQty = -1;
        String bestSellingProduct = "";
        int bestSellingQty = 0;

        double maxRevenue = -1.0;
        String highestRevenueProduct = "";
        double highestProductRevenue = 0.0;

        for (int i = 0; i < productIds.size(); i++) {
            double rev = productRevenues.get(i);
            int qty = quantities.get(i);
            grandTotal += rev;

            if (qty > maxQty) {
                maxQty = qty;
                bestSellingProduct = productNames.get(i);
                bestSellingQty = qty;
            }

            if (rev > maxRevenue) {
                maxRevenue = rev;
                highestRevenueProduct = productNames.get(i);
                highestProductRevenue = rev;
            }
        }

        // Build Report String
        StringBuilder report = new StringBuilder();
        report.append("============================================\n");
        report.append(" PRODUCT SALES SUMMARY REPORT\n");
        report.append("============================================\n\n");

        report.append("--- Revenue Per Product ---\n");
        for (int i = 0; i < productIds.size(); i++) {
            report.append(String.format("%-5s %-18s %-13s $%7.2f\n",
                    productIds.get(i),
                    productNames.get(i),
                    categories.get(i),
                    productRevenues.get(i)));
        }

        report.append("\n--- Revenue Per Category ---\n");
        for (Map.Entry<String, Double> entry : categoryRevenues.entrySet()) {
            report.append(String.format("%-12s : $%.2f\n", entry.getKey(), entry.getValue()));
        }

        report.append("\n--- Highlights ---\n");
        report.append(String.format("Best-Selling Product : %s (%d units)\n", bestSellingProduct, bestSellingQty));
        report.append(String.format("Highest Revenue      : %s ($%.2f)\n", highestRevenueProduct, highestProductRevenue));
        report.append(String.format("Grand Total Revenue  : $%.2f\n", grandTotal));
        report.append("============================================\n");

        // Output Result
        if (outputMethod.equalsIgnoreCase("file")) {
            PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath));
            writer.print(report.toString());
            writer.close();
        } else {
            System.out.print(report.toString());
        }
    }
}
