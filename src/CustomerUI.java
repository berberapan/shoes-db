import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

public class CustomerUI {
    Repository repository = new Repository();
    Scanner scanner = new Scanner(System.in);

    public void store(int id) {
        int order = 0;
        final List<Shoe> orderList = new ArrayList<>();
        while(true) {
            try {
                final List<Shoe> shoes = repository.getShoes();
                System.out.println("# |  Brand  |  Model  | Size | Colour | Price | Available");
                IntStream.range(0, shoes.size()).filter(i -> shoes.get(i).getAmount() > 0)
                        .forEach(i -> System.out.println(
                                i + 1 + " | " + shoes.get(i).getBrand().getName() + " | " + shoes.get(i).getName() +
                                        " | " + shoes.get(i).getSize() + " | " + shoes.get(i).getColour() + " | " +
                                        shoes.get(i).getPrice() + " SEK | " + shoes.get(i).getAmount()));
                System.out.print("\nChoose item to add to order or press '0' to log out: ");
                final int userInput = scanner.nextInt();
                if (userInput == 0) {
                    System.out.println("See you later!");
                    break;
                }
                if (userInput > shoes.size()) {
                    System.out.println("Invalid selection\n");
                } else {
                    order = repository.addToCart(id, order, shoes.get(userInput - 1).getId());
                    orderList.add(shoes.get(userInput-1));
                    System.out.println("\n".repeat(20));
                    System.out.println("Your order:");
                    orderList.forEach(item -> System.out.println(item.getBrand().getName() +
                                        " " + item.getName()+ " " + item.getColour() + " size " + item.getSize()));
                    System.out.println("Total " + orderList.stream().mapToDouble(shoe -> shoe.getPrice()).sum() + "SEK");
                    System.out.println("\n");
                }
            } catch (Exception err) {
                scanner.next();
                System.out.println("Invalid selection\n");
            }
        }
    }

    public int login() {
        final List<Customer> customers = repository.getCustomers();
        System.out.println("Welcome to the shoe shop. Sign in below.");
        while(true) {
            System.out.print("Email: ");
            final String emailInput = scanner.next().strip();
            System.out.print("Password: ");
            final String passwordInput = scanner.next().strip();
            Optional<Customer> validCheck = customers.stream().filter(c -> c.getEmail().equalsIgnoreCase(emailInput))
                                            .filter(c -> c.getPassword().equals(passwordInput))
                                            .findFirst();
            if (validCheck.isPresent()) {
                Customer customer = validCheck.get();
                System.out.println("\n".repeat(20));
                return customer.getId();
            }
            System.out.println("Incorrect log in details.\n");
        }
    }
    public static void main(String[] args) {
        CustomerUI start = new CustomerUI();
        final int customer = start.login();
        start.store(customer);
    }
}
