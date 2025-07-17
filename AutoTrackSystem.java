// AutoTrack Vehicle Rental System - Full Console Version (With Login & Real-Time Status Tracking)

import java.time.LocalDate;
import java.util.*;

// Class representing a vehicle
class Vehicle {
    int vehicleId;
    String brand;
    String model;
    String type;
    int capacity;
    double dailyRate;
    String status = "Available"; // Tracks if the vehicle is available or booked

    // Constructor to initialize a vehicle object
    Vehicle(int id, String brand, String model, String type, int capacity, double rate) {
        this.vehicleId = id;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.capacity = capacity;
        this.dailyRate = rate;
    }

    // Displays vehicle details
    void display() {
        System.out.println(vehicleId + ". " + brand + " " + model + " (" + type + ", " + capacity + "pax) @ Ksh " + dailyRate + "/day - Status: " + status);
    }
}

// Class representing a booking
class Booking {
    int bookingId;
    int vehicleId;
    String customerName;
    LocalDate bookingDate;
    LocalDate returnDate;
    double totalCost;

    // Constructor that creates a new booking with date and cost
    Booking(int bookingId, int vehicleId, String name, int days, double rate) {
        this.bookingId = bookingId;
        this.vehicleId = vehicleId;
        this.customerName = name;
        this.bookingDate = LocalDate.now();
        this.returnDate = bookingDate.plusDays(days);
        this.totalCost = days * rate;
    }

    // Display booking details
    void display() {
        System.out.println("------------------------------");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Customer: " + customerName);
        System.out.println("Vehicle ID: " + vehicleId);
        System.out.println("Booking Date: " + bookingDate);
        System.out.println("Return Date: " + returnDate);
        System.out.println("Total Cost: Ksh " + totalCost);
        System.out.println("------------------------------");
    }
}

// Class representing a customer
class Customer {
    String name;
    String password;

    // Constructor to initialize customer
    Customer(String name, String password) {
        this.name = name;
        this.password = password;
    }
}

// Main class containing the system logic
public class AutoTrackSystem {
    static Scanner sc = new Scanner(System.in);
    static List<Vehicle> vehicles = new ArrayList<>();       // List to store vehicle records
    static List<Booking> bookings = new ArrayList<>();       // List to store booking records
    static List<Customer> customers = new ArrayList<>();     // List to store registered users
    static int bookingCounter = 1;                           // Counter to generate unique booking IDs
    static Customer loggedInCustomer = null;                 // Currently logged in customer

    // Main method that runs the system
    public static void main(String[] args) {
        seedData(); // Load sample vehicles

        // Main application loop
        while (true) {
            // If no user is logged in, prompt login or registration
            if (loggedInCustomer == null) {
                System.out.println("\n=== AutoTrack Login System ===");
                System.out.println("1. Register\n2. Login\n3. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 -> register();
                    case 2 -> login();
                    case 3 -> System.exit(0);
                    default -> System.out.println("Invalid choice!");
                }
            } else {
                // If logged in, show user menu
                System.out.println("\n=== Welcome, " + loggedInCustomer.name + " ===");
                System.out.println("1. View Available Vehicles");
                System.out.println("2. Book Vehicle");
                System.out.println("3. Return Vehicle");
                System.out.println("4. View Booking History");
                System.out.println("5. View Rental Charges Summary");
                System.out.println("6. Logout");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 -> viewVehicles();
                    case 2 -> bookVehicle();
                    case 3 -> returnVehicle();
                    case 4 -> viewBookings();
                    case 5 -> viewChargesSummary();
                    case 6 -> {
                        loggedInCustomer = null; // Clear session
                        System.out.println("Logged out.");
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
        }
    }

    // Register a new customer
    static void register() {
        System.out.print("Enter your name: ");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        customers.add(new Customer(name, pass));
        System.out.println("✅ Registered successfully. You can now log in.");
    }

    // Authenticate user login
    static void login() {
        System.out.print("Enter your name: ");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        for (Customer c : customers) {
            if (c.name.equals(name) && c.password.equals(pass)) {
                loggedInCustomer = c; // Login success
                System.out.println("✅ Login successful.");
                return;
            }
        }
        System.out.println("❌ Invalid credentials.");
    }

    // Preload sample vehicle data
    static void seedData() {
        vehicles.add(new Vehicle(1, "Toyota", "Axio", "Sedan", 5, 3000));
        vehicles.add(new Vehicle(2, "Nissan", "X-Trail", "SUV", 7, 5000));
        vehicles.add(new Vehicle(3, "Mazda", "Demio", "Hatchback", 4, 2500));
        vehicles.add(new Vehicle(4, "Isuzu", "NQR", "Van", 14, 7000));
    }

    // Show all vehicles with current status
    static void viewVehicles() {
        System.out.println("\n--- Available Vehicles ---");
        for (Vehicle v : vehicles) {
            v.display();
        }
    }

    // Book a vehicle if available
    static void bookVehicle() {
        viewVehicles();
        System.out.print("Enter vehicle ID to book: ");
        int id = sc.nextInt();
        Vehicle v = getVehicleById(id);

        if (v == null || !v.status.equals("Available")) {
            System.out.println("\n❌ Vehicle is not available for booking.");
            return;
        }

        System.out.print("Enter number of rental days: ");
        int days = sc.nextInt();

        Booking b = new Booking(bookingCounter++, v.vehicleId, loggedInCustomer.name, days, v.dailyRate);
        bookings.add(b);
        v.status = "Booked"; // Update status

        System.out.println("\n✅ Booking successful!");
        b.display();
    }

    // Return a booked vehicle and mark it as available
    static void returnVehicle() {
        System.out.print("\nEnter vehicle ID to return: ");
        int id = sc.nextInt();
        Vehicle v = getVehicleById(id);

        if (v == null || !v.status.equals("Booked")) {
            System.out.println("\n❌ This vehicle is not currently booked.");
            return;
        }

        v.status = "Available"; // Update status
        System.out.println("\n✅ Vehicle returned and marked as available.");
    }

    // Show all bookings made by the logged-in user
    static void viewBookings() {
        boolean found = false;
        for (Booking b : bookings) {
            if (b.customerName.equals(loggedInCustomer.name)) {
                b.display();
                found = true;
            }
        }
        if (!found) System.out.println("\nNo bookings found for you.");
    }

    // Show the total charges of all bookings for the user
    static void viewChargesSummary() {
        double total = 0;
        for (Booking b : bookings) {
            if (b.customerName.equals(loggedInCustomer.name)) {
                total += b.totalCost;
            }
        }
        System.out.println("\n💰 Your Total Rental Charges: Ksh " + total);
    }

    // Helper method to get a vehicle by its ID
    static Vehicle getVehicleById(int id) {
        for (Vehicle v : vehicles) {
            if (v.vehicleId == id) return v;
        }
        return null;
    }
}
