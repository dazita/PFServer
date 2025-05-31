package model;

public class Vehicle {
    
    private Integer model;
    private String vehicleType;
    private Brand brand;
    private Line line;

    public Vehicle(Brand brand, Line line, Integer model, String vehicleType) {
        this.brand = brand;
        this.line = line;
        this.model = model;
        this.vehicleType = vehicleType;
    }

    public Brand getBrand() {
        return brand;
    }

    public Integer getModel() {
        return model;
    }

    public Line getLine() {
        return line;
    }

    public String getVehicleType() {
        return vehicleType;
    }
}

