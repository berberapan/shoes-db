public class Shoe {
    private final int id;
    private final String name;
    private final int size;
    private final String colour;
    private final double price;
    private final int amount;
    private final Brand brand;

    public Shoe(int id, String name, int size, String colour, double price, int amount, Brand brand) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.colour = colour;
        this.price = price;
        this.amount = amount;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public String getColour() {
        return colour;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public Brand getBrand() {
        return brand;
    }
}