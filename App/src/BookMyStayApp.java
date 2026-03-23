/**
 * BookMyStayApp
 * Demonstrates Error Handling & Validation for hotel bookings.
 * Validates room types, inventory, and reservation inputs before processing.
 *
 * @author YourName
 * @version 9.0
 */

import java.util.*;

// Custom exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Inventory class with validation
class Inventory {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public Inventory() {
        // Initialize room types
        roomAvailability.put("Single Room", 5);
        roomAvailability.put("Double Room", 3);
        roomAvailability.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        return roomAvailability.get(roomType);
    }

    public void allocateRoom(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        int available = roomAvailability.get(roomType);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
        roomAvailability.put(roomType, available - 1);
    }

    private void validateRoomType(String roomType) throws InvalidBookingException {
        if (!roomAvailability.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    public void displayAvailability() {
        System.out.println("\nCurrent Room Availability:");
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Reservation: " + guestName + " | Room Type: " + roomType);
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("      Welcome to Book My Stay App");
        System.out.println("         Version: v9.0");
        System.out.println("=====================================");

        Inventory inventory = new Inventory();

        // Sample reservations (some invalid)
        List<Reservation> bookings = Arrays.asList(
                new Reservation("Alice", "Single Room"),
                new Reservation("Bob", "Penthouse Suite"), // Invalid type
                new Reservation("Charlie", "Suite Room"),
                new Reservation("David", "Double Room"),
                new Reservation("Eve", "Suite Room") // May cause insufficient availability
        );

        for (Reservation r : bookings) {
            try {
                System.out.println("\nProcessing booking for " + r.getGuestName() + "...");
                inventory.allocateRoom(r.getRoomType());
                System.out.println("Booking confirmed!");
                r.displayReservation();
            } catch (InvalidBookingException ex) {
                System.out.println("Booking failed: " + ex.getMessage());
            }
        }

        // Display remaining availability
        inventory.displayAvailability();
    }
}