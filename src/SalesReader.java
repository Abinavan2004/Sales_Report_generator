import java.io.IOException;
import java.util.List;



public interface SalesReader {
    List<Product> readSales(String source) throws IOException;
}



