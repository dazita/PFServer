package model;


public class Listing {
    
    private Vehicle vehicle;
    private int kms;
    private int price;
    private String base64Image;
    private String description;
    private String location;
    private int id;

    public Listing(Vehicle vehicle, int price, String base64Image, String description, String location, int id, int kms) {
        this.vehicle = vehicle;
        this.price = price;
        this.base64Image = base64Image;
        this.description = description;
        this.location = location;
        this.id = id;
        this.kms = kms;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public int getKms() {
        return kms;
    }
}
