import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SalesAnalyzer {
    private final List<Product> products;

    public SalesAnalyzer(List<Product> products) {
        this.products = products;
    }

    public SalesSummary analyze() {
        double grandTotal = 0.0;
        Map<String, Double> categoryMap = new LinkedHashMap<>();
        Product bestSelling = null;
        Product highestRevenue = null;

        for (Product p : products) {
            double revenue = p.getRevenue();
            grandTotal += revenue;

            // Accumulate category revenue
            categoryMap.put(p.getCategory(), categoryMap.getOrDefault(p.getCategory(), 0.0) + revenue);

            // Check best selling by quantity
            if (bestSelling == null || p.getQuantity() > bestSelling.getQuantity()) {
                bestSelling = p;
            }

            // Check highest revenue
            if (highestRevenue == null || revenue > highestRevenue.getRevenue()) {
                highestRevenue = p;
            }
        }

        return new SalesSummary(grandTotal, categoryMap, bestSelling, highestRevenue);
    }
}

