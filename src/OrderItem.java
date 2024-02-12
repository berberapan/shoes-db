public class OrderItem {
    private final int id;
    private final Shoe shoe;
    private final PurchaseOrder order;
    private final int amount;

    public OrderItem(int id, Shoe shoe, PurchaseOrder order, int amount) {
        this.id = id;
        this.shoe = shoe;
        this.order = order;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public Shoe getShoe() {
        return shoe;
    }

    public PurchaseOrder getOrder() {
        return order;
    }

    public int getAmount() {
        return amount;
    }
}
