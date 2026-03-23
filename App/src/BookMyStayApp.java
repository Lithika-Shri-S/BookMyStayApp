/**
 * BookMyStayApp
 * Demonstrates Booking Cancellation & Inventory Rollback.
 * Supports safe undo of confirmed reservations while maintaining inventory consistency.
 *
 * @author YourName
 * @version 10.0
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
        System.out.println("Reservation: " + guestName + " | Room Type: " + roomType + " | Room ID: " + roomId);
    }
}

// Inventory class with rollback support
class Inventory {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public Inventory() {
        roomAvailability.put("Single Room", 5);
        roomAvailability.put("Double Room", 3);
        roomAvailability.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    public void allocateRoom(String roomType) {
        roomAvailability.put(roomType, roomAvailability.get(roomType) - 1);
    }

    public void releaseRoom(String roomType) {
        roomAvailability.put(roomType, roomAvailability.get(roomType) + 1);
    }

    public void displayAvailability() {
        System.out.println("\nCurrent Room Availability:");
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Booking History with cancellation support
class BookingHistory {
    private List<Reservation> confirmedReservations = new ArrayList<>();
    private Stack<String> releasedRoomIds = new Stack<>();

    public void addReservation(Reservation r) {
        confirmedReservations.add(r);
        System.out.println("Added reservation: " + r.getRoomId());
    }

    public boolean cancelReservation(String roomId, Inventory inventory) {
        Iterator<Reservation> it = confirmedReservations.iterator();
        while (it.hasNext()) {
            Reservation r = it.next();
            if (r.getRoomId().equals(roomId)) {
                // Remove from history
                it.remove();
                // Restore inventory
                inventory.releaseRoom(r.getRoomType());
                // Track released room for rollback
                releasedRoomIds.push(r.getRoomId());
                System.out.println("Cancelled reservation: " + roomId);
                return true;
            }
        }
        System.out.println("Cancellation failed: Reservation ID " + roomId + " not found.");
        return false;
    }

    public void displayHistory() {
        System.out.println("\n--- Current Booking History ---");
        if (confirmedReservations.isEmpty()) {
            System.out.println("No confirmed reservations.");
            return;
        }
        for (Reservation r : confirmedReservations) {
            r.displayReservation();
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("      Welcome to Book My Stay App");
        System.out.println("         Version: v10.0");
        System.out.println("=====================================");

        Inventory inventory = new Inventory();
        BookingHistory history = new BookingHistory();

        // Sample reservations
        Reservation r1 = new Reservation("Alice", "Single Room", "SI101");
        Reservation r2 = new Reservation("Bob", "Double Room", "DO102");
        Reservation r3 = new Reservation("Charlie", "Suite Room", "SU103");

        // Add reservations
        history.addReservation(r1);
        inventory.allocateRoom(r1.getRoomType());

        history.addReservation(r2);
        inventory.allocateRoom(r2.getRoomType());

        history.addReservation(r3);
        inventory.allocateRoom(r3.getRoomType());

        // Display current booking and availability
        history.displayHistory();
        inventory.displayAvailability();

        // Perform cancellations
        System.out.println("\n--- Processing Cancellations ---");
        history.cancelReservation("DO102", inventory);  // Valid cancellation
        history.cancelReservation("SU999", inventory);  // Invalid cancellation

        // Display updated booking and availability
        history.displayHistory();
        inventory.displayAvailability();
    }
}