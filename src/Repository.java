import java.io.FileInputStream;
import java.util.*;
import java.sql.*;

public class Repository {

    Properties properties = new Properties();
    private final String connection;
    private final String name;
    private final String password;

    public Repository() {
        try {
            properties.load(new FileInputStream("src/config.properties"));
        } catch (Exception err) {
            err.printStackTrace();
        }
        this.connection = properties.getProperty("connectionString");
        this.name = properties.getProperty("name");
        this.password = properties.getProperty("password");
    }

    public List<Shoe> getShoes() {
        final List<Shoe> shoes = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(connection, name, password);
            PreparedStatement statement = conn.prepareStatement(
                    "select s.id, s.name, s.size, s.colour, s.price, s.amount_available, s.brand_id, b.name as brand_name " +
                            "from shoe s join brand b on s.brand_id = b.id")
        ) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                shoes.add(new Shoe(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getInt("size"),
                        result.getString("colour"),
                        result.getDouble("price"),
                        result.getInt("amount_available"),
                        new Brand(result.getInt("brand_id"), result.getString("brand_name"))
                ));
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return shoes;
    }

    public int addToCart(int customer, int order, int shoe) throws SQLException {
        try(Connection conn = DriverManager.getConnection(connection, name, password);
        CallableStatement statement = conn.prepareCall("call AddToCart(?, ?, ?)")
        ) {
            statement.setInt(1, customer);
            statement.registerOutParameter(2, Types.INTEGER);
            statement.setInt(2, order);
            statement.setInt(3, shoe);
            statement.executeQuery();
            return  statement.getInt(2);
        } catch (Exception err) {
            System.out.println(err.getMessage());
            throw err;
        }
    }

    public List<Customer> getCustomers() {
        final List<Customer> customers = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(connection, name, password);
            PreparedStatement statement = conn.prepareStatement("select id, first_name, last_name, email, pw, street, postal_code, city " +
                                                                "from customer")
        ) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                customers.add(new Customer(
                        result.getInt("id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("email"),
                        result.getString("pw"),
                        result.getString("street"),
                        result.getInt("postal_code"),
                        result.getString("city")
                ));
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return customers;
    }

    public List<PurchaseOrder> getOrders() {
        final List<PurchaseOrder> orders = new ArrayList<>();
        final Map<Integer, Customer> customers = new HashMap<>();
        try(Connection conn = DriverManager.getConnection(connection, name, password);
            PreparedStatement statement = conn.prepareStatement("select *, o.id as order_id from purchase_order o join customer c on o.customer_id = c.id")
        ){
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                int customerId = result.getInt("customer_id");
                Customer customer = customers.get(customerId);
                if (customer == null) {
                    customer = new Customer(customerId,
                                            result.getString("first_name"),
                                            result.getString("last_name"),
                                            result.getString("email"),
                                            result.getString("pw"),
                                            result.getString("street"),
                                            result.getInt("postal_code"),
                                            result.getString("city"));
                    customers.put(customerId, customer);
                }
                orders.add(new PurchaseOrder(result.getInt("order_id"), customer,
                        result.getDouble("total"),  result.getDate("order_date")));
            }

        } catch (Exception err) {
            err.printStackTrace();
        }
        return orders;
    }

    public List<OrderItem> getOrderItems() {
        final List<OrderItem> orderItems = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(connection, name, password);
            PreparedStatement statement = conn.prepareStatement("select o.id as item_id, s.id as shoe_id, s.name, s.size, s.colour, s.price, s.amount_available, b.id as brand_id, b.name as brand_name,\n" +
                    "o.amount, p.id as purchase_order_id, p.total, p.order_date, c.id as customer_id, c.first_name, c.last_name, c.email,\n" +
                    "c.pw, c.street, c.postal_code, c.city\n" +
                    "from order_item o\n" +
                    "join shoe s on o.shoe_id = s.id\n" +
                    "join brand b on s.brand_id = b.id\n" +
                    "join purchase_order p on o.purchase_order_id = p.id\n" +
                    "join customer c on p.customer_id = c.id;")
        ) {
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                orderItems.add(new OrderItem(result.getInt("item_id"),
                        new Shoe(result.getInt("shoe_id"),
                                result.getString("name"),
                                result.getInt("size"),
                                result.getString("colour"),
                                result.getDouble("price"),
                                result.getInt("amount_available"),
                                new Brand(result.getInt("brand_id"), result.getString("brand_name"))),
                        new PurchaseOrder(result.getInt("purchase_order_id"),
                                new Customer(result.getInt("customer_id"),
                                        result.getString("first_name"),
                                        result.getString("last_name"),
                                        result.getString("email"),
                                        result.getString("pw"),
                                        result.getString("street"),
                                        result.getInt("postal_code"),
                                        result.getString("city")
                                        ), result.getDouble("total"), result.getDate("order_date")
                        ), result.getInt("amount")
                ));
            }

        } catch (Exception err) {
            err.printStackTrace();
        }
        return orderItems;
    }
}
