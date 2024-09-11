// Classes and Objects
class Car {
    private String make;
    private String model;
    private int year;

    // constructor
    public Car(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }

    // getter methods
    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    // method
    public void startEngine() {
        System.out.println("The " + year + " " + make + " " + model + " engine starts.");
    }
}

// Inheritance
class ElectricCar extends Car {
    private int batteryCapacity;

    public ElectricCar(String make, String model, int year, int batteryCapacity) {
        super(make, model, year); // calling the supercalss constructor
        this.batteryCapacity = batteryCapacity;
    }

    // Override method
    @Override
    public void startEngine() {
        System.out.println("The " + getYear() + " " + getMake() + " " + getModel() + " silently powers up its "
                + batteryCapacity + "kWh battery.");
    }

    // new method specific to the electric car
    public void charge() {
        System.out.println("Charging the " + getMake() + " " + getModel() + "...");
    }
}

// Interfaces
interface Drivable {
    void accelerate(int speed);

    void brake();
}

interface Recyclable {
    void recycle();
}

// Classes implementing interfaces
class BicycleSharing implements Drivable, Recyclable {
    private String brand;

    public BicycleSharing(String brand) {
        this.brand = brand;
    }

    @Override
    public void accelerate(int speed) {
        System.out.println("Pedaling the " + brand + " bicycle to " + speed + " km/h");
    }

    @Override
    public void brake() {
        System.out.println("Applying brakes on the " + brand + " bicycle");
    }

    @Override
    public void recycle() {
        System.out.println("Recycling the " + brand + " bicycle's parts");
    }
}

// Abstract class
abstract class Vehicle {
    protected String type;

    public Vehicle(String type) {
        this.type = type;
    }

    // Abstract Method - no implementation
    public abstract void move();

    // Concrete method
    public void DisplayType() {
        System.out.println("This is a " + type + " vehicle.");
    }

}

// Concrete class extending abstract class
class Boat extends Vehicle {
    public Boat() {
        super("water");
    }

    @Override
    public void move() {
        System.out.println("The boat sails through the water.");
    }
}

// Bringing it all together
public class OOPExample {
    public static void main(String[] args) {
        // Classes and objects
        Car myCar = new Car("Toyota", "Corolla", 2022);
        myCar.startEngine();

        // inheritance and polymorphism
        ElectricCar myTesla = new ElectricCar("Tesla", "Model 3", 2023, 75);
        myTesla.startEngine();
        myTesla.charge();

        // Using interfaces
        BicycleSharing cityBike = new BicycleSharing("CityRide");
        cityBike.accelerate(10);
        cityBike.brake();
        cityBike.recycle();

        // using abstract class
        Boat myBoat = new Boat();
        myBoat.DisplayType();
        myBoat.move();
    }
}
