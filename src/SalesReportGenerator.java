
import java.util.List;
import java.util.Map;

public class SalesReportGenerator {

    public String generateReport(List<Product> products, SalesSummary summary) {
        StringBuilder report = new StringBuilder();
        report.append("============================================\n");
        report.append(" PRODUCT SALES SUMMARY REPORT\n");
        report.append("============================================\n\n");

        report.append("--- Revenue Per Product ---\n");
        for (Product p : products) {
            report.append(String.format("%-5s %-18s %-13s $%7.2f\n",
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    p.getRevenue()));
        }

        report.append("\n--- Revenue Per Category ---\n");
        for (Map.Entry<String, Double> entry : summary.getCategoryRevenues().entrySet()) {
            report.append(String.format("%-12s : $%.2f\n", entry.getKey(), entry.getValue()));
        }

        report.append("\n--- Highlights ---\n");
        if (summary.getBestSellingProduct() != null) {
            report.append(String.format("Best-Selling Product : %s (%d units)\n",
                    summary.getBestSellingProduct().getName(),
                    summary.getBestSellingProduct().getQuantity()));
        }
        if (summary.getHighestRevenueProduct() != null) {
            report.append(String.format("Highest Revenue      : %s ($%.2f)\n",
                    summary.getHighestRevenueProduct().getName(),
                    summary.getHighestRevenueProduct().getRevenue()));
        }
        report.append(String.format("Grand Total Revenue  : $%.2f\n", summary.getGrandTotal()));
        report.append("============================================\n");

        return report.toString();
    }
}



