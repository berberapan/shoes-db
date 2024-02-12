import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReportCompiler {
    Scanner scanner = new Scanner(System.in);
    Repository repository = new Repository();
    ShoePropertySearch sizeSearch = (s, i) -> String.valueOf(s.getShoe().getSize()).equals(i);
    ShoePropertySearch colourSearch  = (s, i) -> s.getShoe().getColour().equalsIgnoreCase(i);
    ShoePropertySearch brandSearch = (s, i) -> s.getShoe().getBrand().getName().equalsIgnoreCase(i);

    void shoesByAttribute(String input, ShoePropertySearch shoeSearch) {
        final List<OrderItem> orderItems = repository.getOrderItems();
        orderItems.stream().filter(o -> shoeSearch.search(o, input))
                .map(o -> o.getOrder().getCustomer().getNameAndAddress()).distinct()
                .forEach(n -> {
                    System.out.println(n);
                });
    }

    void attributeChoice() {
        System.out.print("1. Size\n2. Colour\n3. Brand\nWhat attribute do you want to search for?: ");
        final int userInput = scanner.nextInt();
        scanner.nextLine();
        switch (userInput) {
            case 1:
                System.out.print("What size do you want to search?: ");
                final String size = scanner.next();
                System.out.println("\n");
                shoesByAttribute(size, sizeSearch);
                break;
            case 2:
                System.out.print("What colour do you want to search?: ");
                final String colour = scanner.next();
                System.out.println("\n");
                shoesByAttribute(colour, colourSearch);
                break;
            case 3:
                System.out.println("What brand do you want to search?: ");
                final String brand = scanner.next();
                System.out.println("\n");
                shoesByAttribute(brand, brandSearch);
                break;
            default:
                System.out.println("\nInvalid choice. Please select 1-3");
        }
    }

    void customerNumOrders() {
        final List<PurchaseOrder> orders = repository.getOrders();
        final List<Customer> customers = repository.getCustomers();
        final Map<Customer, Long> mapOrder = orders.stream().collect(Collectors.groupingBy(
                o -> o.getCustomer(), Collectors.counting()));
        customers.stream().filter(customer -> orders.stream()
                .noneMatch(order -> customer.getId() == order.getCustomer().getId()))
                .forEach(customer -> mapOrder.put(customer, 0L));
        mapOrder.forEach((k,v) -> System.out.println(k.getFirstName() + " " + k.getLastName() + " " + v + " order(s)"));
    }

    void customerTotalOrders() {
        final List<PurchaseOrder> orders = repository.getOrders();
        final List<Customer> customers = repository.getCustomers();
        final Map<Customer, Double> mapOrder = orders.stream().collect(Collectors.groupingBy(
                o -> o.getCustomer(), Collectors.summingDouble(o -> o.getTotal())));
        customers.stream().filter(customer -> orders.stream()
                .noneMatch(order -> customer.getId() == order.getCustomer().getId()))
                .forEach(customer -> mapOrder.put(customer, 0.0));
        mapOrder.forEach((k,v) -> System.out.println(k.getFirstName() + " " + k.getLastName() + " " + v + " SEK worth of orders"));
    }

    void cityTotalOrders() {
        final List<PurchaseOrder> orders = repository.getOrders();
        final Map<String, Double> mapOrder = orders.stream().collect(Collectors.groupingBy(
                o -> o.getCustomer().getCity(), Collectors.summingDouble(o -> o.getTotal())));
        mapOrder.forEach((k,v) -> System.out.println(k + " " + v + " SEK worth of orders"));
    }

    void topList() {
        final List<OrderItem> orderItems = repository.getOrderItems();
        final Map<String, Integer> mapItem = orderItems.stream().collect(Collectors.groupingBy(
                i -> i.getShoe().getName(), Collectors.summingInt(i -> i.getAmount())));
        mapItem.forEach((k,v) -> System.out.println(k + " " + v));
    }

    void userSelection() {
        final List<String> reportTypes = new ArrayList<>(Arrays.asList("Customer purchases filtered on attribute",
                "Number of orders per customer", "Total spent per customer", "Total spent per city", "Top list, most popular items"));
        while(true){
            IntStream.range(0, reportTypes.size()).forEach(n -> System.out.println(n+1 + ". " + reportTypes.get(n)));
            System.out.println("\nPress '0' to exit");
            System.out.println("Choose report you want to compile: ");
            int userInput = scanner.nextInt();
            System.out.println("\n");
            switch (userInput) {
                case 0:
                    System.exit(0);
                case 1:
                    attributeChoice();
                    break;
                case 2:
                    customerNumOrders();
                    break;
                case 3:
                    customerTotalOrders();
                    break;
                case 4:
                    cityTotalOrders();
                    break;
                case 5:
                    topList();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
            System.out.println("\n");
        }
    }

    public static void main(String[] args) {
        ReportCompiler rp = new ReportCompiler();
        rp.userSelection();
    }
}
