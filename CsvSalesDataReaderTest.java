package salesreporter.io.reader;

import model.Product;
import salesreporter.io.exception.SalesDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvSalesDataReaderTest {

    private final CsvSalesDataReader reader = new CsvSalesDataReader();

    @TempDir
    Path tempDir;

    private Path writeCsv(String content) throws IOException {
        Path file = tempDir.resolve("sales.csv");
        Files.writeString(file, content);
        return file;
    }

    @Test
    void readsAllValidRowsAndSkipsTheHeader() throws Exception {
        Path csv = writeCsv(
                "product_id, product_name, category, quantity_sold, unit_price\n" +
                        "P001, Wireless Mouse, Electronics, 12, 25.50\n" +
                        "P002, Notebook, Stationery, 35, 3.75\n"
        );

        List<Product> products = reader.readAll(csv.toString());

        assertEquals(2, products.size());
        assertEquals("P001", products.get(0).getId());
        assertEquals("Wireless Mouse", products.get(0).getName());
        assertEquals(12, products.get(0).getQuantitySold());
        assertEquals(25.50, products.get(0).getUnitPrice(), 0.001);
    }

    @Test
    void skipsBlankLinesBetweenRows() throws Exception {
        Path csv = writeCsv(
                "header\n" +
                        "P001, Wireless Mouse, Electronics, 12, 25.50\n" +
                        "\n" +
                        "P002, Notebook, Stationery, 35, 3.75\n"
        );

        List<Product> products = reader.readAll(csv.toString());

        assertEquals(2, products.size());
    }

    @Test
    void throwsAClearErrorWhenTheFileDoesNotExist() {
        SalesDataException ex = assertThrows(SalesDataException.class,
                () -> reader.readAll(tempDir.resolve("does-not-exist.csv").toString()));

        assertTrue(ex.getMessage().contains("not found"),
                "Message should clearly say the file was not found: " + ex.getMessage());
    }

    @Test
    void throwsAClearErrorForARowWithMissingColumns() throws Exception {
        Path csv = writeCsv(
                "header\n" +
                        "P001, Wireless Mouse, Electronics, 12\n" // missing unit_price
        );

        SalesDataException ex = assertThrows(SalesDataException.class, () -> reader.readAll(csv.toString()));

        assertTrue(ex.getMessage().contains("line 2"),
                "Message should point at the offending line: " + ex.getMessage());
    }

    @Test
    void throwsAClearErrorForNonNumericQuantityOrPrice() throws Exception {
        Path csv = writeCsv(
                "header\n" +
                        "P001, Wireless Mouse, Electronics, twelve, 25.50\n"
        );

        assertThrows(SalesDataException.class, () -> reader.readAll(csv.toString()));
    }

    @Test
    void throwsAClearErrorWhenTheFileHasNoDataRows() throws Exception {
        Path csv = writeCsv("product_id, product_name, category, quantity_sold, unit_price\n");

        assertThrows(SalesDataException.class, () -> reader.readAll(csv.toString()));
    }
}
