import java.util.Collections;
import java.util.Map;

public class SalesSummary {
        private final double grandTotal;
        private final Map<String, Double> categoryRevenues;
        private final Product bestSellingProduct;
        private final Product highestRevenueProduct;

        public SalesSummary(double grandTotal, Map<String, Double> categoryRevenues,
                            Product bestSellingProduct, Product highestRevenueProduct) {
            this.grandTotal = grandTotal;
            this.categoryRevenues = Collections.unmodifiableMap(categoryRevenues);
            this.bestSellingProduct = bestSellingProduct;
            this.highestRevenueProduct = highestRevenueProduct;
        }

        public double getGrandTotal() { return grandTotal; }
        public Map<String, Double> getCategoryRevenues() { return categoryRevenues; }
        public Product getBestSellingProduct() { return bestSellingProduct; }
        public Product getHighestRevenueProduct() { return highestRevenueProduct; }
    }


