public class Customer {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String street;
    private final int postalCode;
    private final String city;

    public Customer(int id, String firstName, String lastName, String email, String password, String street, int postalCode, String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getStreet() {
        return street;
    }
    public int getPostalCode() {
        return postalCode;
    }
    public String getCity() {
        return city;
    }
    public String getNameAndAddress() {
        return String.format("%s %s %s %d %s", firstName, lastName, street, postalCode, city);
    }
}