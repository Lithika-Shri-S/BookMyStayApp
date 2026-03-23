/**
 * BookMyStayApp
 * Demonstrates concurrent booking simulation with thread safety.
 * Multiple guests can submit requests concurrently without double-booking.
 *
 * @author YourName
 * @version 11.0
 */

import java.util.*;
import java.util.concurrent.*;

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

// Thread-safe Inventory
class Inventory {
    private final Map<String, Integer> roomAvailability = new ConcurrentHashMap<>();

    public Inventory() {
        roomAvailability.put("Single Room", 5);
        roomAvailability.put("Double Room", 3);
        roomAvailability.put("Suite Room", 2);
    }

    public synchronized boolean allocateRoom(String roomType) {
        int available = roomAvailability.getOrDefault(roomType, 0);
        if (available > 0) {
            roomAvailability.put(roomType, available - 1);
            return true;
        } else {
            return false;
        }
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

// Booking History (Thread-safe)
class BookingHistory {
    private final List<Reservation> confirmedReservations = Collections.synchronizedList(new ArrayList<>());

    public void addReservation(Reservation r) {
        confirmedReservations.add(r);
        System.out.println("Confirmed: " + r.getGuestName() + " booked " + r.getRoomType() + " [" + r.getRoomId() + "]");
    }

    public void displayHistory() {
        synchronized (confirmedReservations) {
            System.out.println("\n--- Booking History ---");
            if (confirmedReservations.isEmpty()) {
                System.out.println("No confirmed reservations.");
                return;
            }
            for (Reservation r : confirmedReservations) {
                r.displayReservation();
            }
        }
    }
}

// Booking Processor for concurrent requests
class BookingProcessor implements Runnable {
    private String guestName;
    private String roomType;
    private Inventory inventory;
    private BookingHistory history;

    public BookingProcessor(String guestName, String roomType, Inventory inventory, BookingHistory history) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.inventory = inventory;
        this.history = history;
    }

    @Override
    public void run() {
        // Allocate room in a synchronized method
        boolean success = inventory.allocateRoom(roomType);
        if (success) {
            String roomId = roomType.substring(0, 2).toUpperCase() + new Random().nextInt(1000);
            Reservation reservation = new Reservation(guestName, roomType, roomId);
            history.addReservation(reservation);
        } else {
            System.out.println("Sorry " + guestName + ", no " + roomType + " available.");
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("      Welcome to Book My Stay App");
        System.out.println("         Version: v11.0");
        System.out.println("=====================================");

        Inventory inventory = new Inventory();
        BookingHistory history = new BookingHistory();

        // Simulate multiple guests booking concurrently
        ExecutorService executor = Executors.newFixedThreadPool(6);

        executor.execute(new BookingProcessor("Alice", "Single Room", inventory, history));
        executor.execute(new BookingProcessor("Bob", "Double Room", inventory, history));
        executor.execute(new BookingProcessor("Charlie", "Suite Room", inventory, history));
        executor.execute(new BookingProcessor("Diana", "Single Room", inventory, history));
        executor.execute(new BookingProcessor("Ethan", "Suite Room", inventory, history));
        executor.execute(new BookingProcessor("Fiona", "Double Room", inventory, history));

        // Shutdown executor and wait for tasks to complete
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Display final booking history and availability
        history.displayHistory();
        inventory.displayAvailability();
    }
}