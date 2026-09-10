package report;

import model.Product;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SalesReportCalculatorTest {

    private final SalesReportCalculator calculator = new SalesReportCalculator();

    // Same five rows as the assignment brief, so the expected numbers below
    // can be checked directly against the sample output in the PDF.
    private List<Product> sampleProducts() {
        return List.of(
                new Product("P001", "Wireless Mouse", "Electronics", 12, 25.50),
                new Product("P002", "Notebook", "Stationery", 35, 3.75),
                new Product("P003", "USB Hub", "Electronics", 8, 18.00),
                new Product("P004", "Ballpoint Pen", "Stationery", 100, 0.50),
                new Product("P005", "HDMI Cable", "Electronics", 20, 12.00)
        );
    }

    @Test
    void calculatesRevenuePerProduct() {
        SalesSummary summary = calculator.calculate(sampleProducts());

        assertEquals(306.00, summary.getProducts().get(0).getRevenue(), 0.001, "Wireless Mouse revenue");
        assertEquals(131.25, summary.getProducts().get(1).getRevenue(), 0.001, "Notebook revenue");
        assertEquals(144.00, summary.getProducts().get(2).getRevenue(), 0.001, "USB Hub revenue");
        assertEquals(50.00, summary.getProducts().get(3).getRevenue(), 0.001, "Ballpoint Pen revenue");
        assertEquals(240.00, summary.getProducts().get(4).getRevenue(), 0.001, "HDMI Cable revenue");
    }

    @Test
    void calculatesRevenuePerCategory() {
        SalesSummary summary = calculator.calculate(sampleProducts());
        Map<String, Double> byCategory = summary.getRevenueByCategory();

        assertEquals(690.00, byCategory.get("Electronics"), 0.001, "Electronics category total");
        assertEquals(181.25, byCategory.get("Stationery"), 0.001, "Stationery category total");
        assertEquals(2, byCategory.size(), "Should only have the two categories present in the data");
    }

    @Test
    void calculatesGrandTotalRevenue() {
        SalesSummary summary = calculator.calculate(sampleProducts());
        assertEquals(871.25, summary.getGrandTotalRevenue(), 0.001);
    }

    @Test
    void findsBestSellingProductByQuantity() {
        SalesSummary summary = calculator.calculate(sampleProducts());
        assertEquals("Ballpoint Pen", summary.getBestSellingProduct().getName());
        assertEquals(100, summary.getBestSellingProduct().getQuantitySold());
    }

    @Test
    void findsHighestRevenueProduct() {
        SalesSummary summary = calculator.calculate(sampleProducts());
        assertEquals("Wireless Mouse", summary.getHighestRevenueProduct().getName());
        assertEquals(306.00, summary.getHighestRevenueProduct().getRevenue(), 0.001);
    }

    @Test
    void bestSellerTieGoesToTheEarlierRowInTheFile() {
        List<Product> tiedQuantities = List.of(
                new Product("P001", "First Item", "Misc", 50, 1.00),
                new Product("P002", "Second Item", "Misc", 50, 1.00)
        );

        SalesSummary summary = calculator.calculate(tiedQuantities);

        assertEquals("First Item", summary.getBestSellingProduct().getName(),
                "On a tie, the first product encountered should win");
    }

    @Test
    void highestRevenueTieGoesToTheEarlierRowInTheFile() {
        List<Product> tiedRevenue = List.of(
                new Product("P001", "First Item", "Misc", 10, 5.00),
                new Product("P002", "Second Item", "Misc", 5, 10.00)
        );

        SalesSummary summary = calculator.calculate(tiedRevenue);

        assertEquals("First Item", summary.getHighestRevenueProduct().getName(),
                "On a revenue tie, the first product encountered should win");
    }

    @Test
    void handlesASingleProductWithoutError() {
        List<Product> single = List.of(new Product("P001", "Only Item", "Misc", 3, 9.99));

        SalesSummary summary = calculator.calculate(single);

        assertEquals("Only Item", summary.getBestSellingProduct().getName());
        assertEquals("Only Item", summary.getHighestRevenueProduct().getName());
        assertEquals(29.97, summary.getGrandTotalRevenue(), 0.001);
    }

    @Test
    void rejectsAnEmptyProductList() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(List.of()));
    }

    @Test
    void rejectsANullProductList() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(null));
    }
}
