import java.sql.Date;

public class PurchaseOrder {
    private final int id;
    private final Customer customer;
    private final double total;
    private final Date orderDate;

    public PurchaseOrder(int id, Customer customer, double total, Date orderDate) {
        this.id = id;
        this.customer = customer;
        this.total = total;
        this.orderDate = orderDate;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTotal() {
        return total;
    }

    public Date getOrderDate() {
        return orderDate;
    }
}
