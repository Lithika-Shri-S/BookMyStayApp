/**
 * BookMyStayApp
 * Demonstrates data persistence and system recovery.
 * Inventory and booking history are saved to a file and restored on startup.
 *
 * @author YourName
 * @version 12.0
 */

import java.io.*;
import java.util.*;

// Reservation class must be Serializable for persistence
class Reservation implements Serializable {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }

    public void displayReservation() {
        System.out.println("Reservation: " + guestName + " | Room Type: " + roomType + " | Room ID: " + roomId);
    }
}

// Serializable Inventory
class Inventory implements Serializable {
    private Map<String, Integer> roomAvailability;

    public Inventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single Room", 5);
        roomAvailability.put("Double Room", 3);
        roomAvailability.put("Suite Room", 2);
    }

    public synchronized boolean allocateRoom(String roomType) {
        int available = roomAvailability.getOrDefault(roomType, 0);
        if (available > 0) {
            roomAvailability.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void releaseRoom(String roomType) {
        roomAvailability.put(roomType, roomAvailability.getOrDefault(roomType, 0) + 1);
    }

    public void displayAvailability() {
        System.out.println("\nCurrent Room Availability:");
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Booking History
class BookingHistory implements Serializable {
    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    public synchronized void addReservation(Reservation r) {
        confirmedReservations.add(r);
        System.out.println("Confirmed: " + r.getGuestName() + " booked " + r.getRoomType() + " [" + r.getRoomId() + "]");
    }

    public void displayHistory() {
        System.out.println("\n--- Booking History ---");
        if (confirmedReservations.isEmpty()) {
            System.out.println("No confirmed reservations.");
            return;
        }
        for (Reservation r : confirmedReservations) {
            r.displayReservation();
        }
    }

    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "bookMyStayData.ser";

    public static void saveState(Inventory inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving system state: " + e.getMessage());
        }
    }

    public static Object[] loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No saved data found. Starting fresh.");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Inventory inventory = (Inventory) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("System state restored successfully.");
            return new Object[]{inventory, history};
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading system state: " + e.getMessage());
            return null;
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("      Welcome to Book My Stay App");
        System.out.println("         Version: v12.0");
        System.out.println("=====================================");

        // Load persisted state if available
        Object[] state = PersistenceService.loadState();
        Inventory inventory = state != null ? (Inventory) state[0] : new Inventory();
        BookingHistory history = state != null ? (BookingHistory) state[1] : new BookingHistory();

        // Example: simulate bookings
        if (inventory.allocateRoom("Single Room")) {
            history.addReservation(new Reservation("Alice", "Single Room", "SR101"));
        }
        if (inventory.allocateRoom("Double Room")) {
            history.addReservation(new Reservation("Bob", "Double Room", "DR201"));
        }

        // Display current state
        history.displayHistory();
        inventory.displayAvailability();

        // Save system state before exit
        PersistenceService.saveState(inventory, history);
    }
}