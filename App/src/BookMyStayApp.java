/**
 * BookMyStayApp
 * Demonstrates Add-On Service Selection for existing reservations.
 * Allows multiple optional services per reservation without affecting booking or inventory.
 *
 * @author YourName
 * @version 7.0
 */

import java.util.*;

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Room Type: " + roomType + " | Room ID: " + roomId);
    }
}

// Add-On Service class
class Service {
    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void displayService() {
        System.out.println("Service: " + serviceName + " | Cost: ₹" + cost);
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map reservationID -> List of selected services
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    // Attach a service to a reservation
    public void addService(Reservation reservation, Service service) {
        String roomId = reservation.getRoomId();
        reservationServices.putIfAbsent(roomId, new ArrayList<>());
        reservationServices.get(roomId).add(service);
        System.out.println("Added service " + service.getServiceName() + " to reservation " + roomId);
    }

    // Calculate total add-on cost for a reservation
    public double getTotalAddOnCost(Reservation reservation) {
        String roomId = reservation.getRoomId();
        List<Service> services = reservationServices.getOrDefault(roomId, Collections.emptyList());
        double total = 0;
        for (Service s : services) {
            total += s.getCost();
        }
        return total;
    }

    // Display all services for a reservation
    public void displayServices(Reservation reservation) {
        String roomId = reservation.getRoomId();
        List<Service> services = reservationServices.getOrDefault(roomId, Collections.emptyList());
        if (services.isEmpty()) {
            System.out.println("No add-on services selected for " + reservation.getGuestName());
        } else {
            System.out.println("Add-On Services for " + reservation.getGuestName() + ":");
            for (Service s : services) {
                s.displayService();
            }
            System.out.println("Total Add-On Cost: ₹" + getTotalAddOnCost(reservation));
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("      Welcome to Book My Stay App");
        System.out.println("         Version: v7.0");
        System.out.println("=====================================");

        // Sample confirmed reservations
        Reservation res1 = new Reservation("Alice", "Single Room", "SI101");
        Reservation res2 = new Reservation("Bob", "Double Room", "DO102");

        // Sample services
        Service breakfast = new Service("Breakfast", 300);
        Service spa = new Service("Spa", 1200);
        Service airportPickup = new Service("Airport Pickup", 500);

        // Manage add-on services
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Alice selects Breakfast and Spa
        serviceManager.addService(res1, breakfast);
        serviceManager.addService(res1, spa);

        // Bob selects Airport Pickup
        serviceManager.addService(res2, airportPickup);

        // Display reservations with selected services
        System.out.println("\n--- Reservation Summary with Add-On Services ---\n");
        res1.displayReservation();
        serviceManager.displayServices(res1);

        System.out.println();
        res2.displayReservation();
        serviceManager.displayServices(res2);
    }
}