
    public class Product {
        private final String id;
        private final String name;
        private final String category;
        private final int quantity;
        private final double unitPrice;

        public Product(String id, String name, String category, int quantity, double unitPrice) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public double getRevenue() {
            return quantity * unitPrice;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public int getQuantity() { return quantity; }
        public double getUnitPrice() { return unitPrice; }
    }


