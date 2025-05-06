public class InvoiceItem {
    private int productId;
    private int quantity;
    private double productPrice;  // Unit price for the product

    public InvoiceItem(int productId, int quantity, double productPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.productPrice = productPrice;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    // Returns the subtotal for this item (unit price * quantity)
    public double getSubtotal() {
        return productPrice * quantity;
    }
}