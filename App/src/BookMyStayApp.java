/**
 * BookMyStayApp
 * Demonstrates Booking History & Reporting functionality.
 * Tracks confirmed reservations and generates reports without modifying historical data.
 *
 * @author YourName
 * @version 8.0
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

// Booking History (maintains confirmed reservations)
class BookingHistory {
    private List<Reservation> confirmedReservations = new ArrayList<>();

    // Add confirmed reservation
    public void addReservation(Reservation r) {
        confirmedReservations.add(r);
        System.out.println("Added reservation to history: " + r.getRoomId());
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(confirmedReservations);
    }
}

// Booking Report Service (generates reports)
class BookingReportService {

    // Generate a simple summary report
    public void generateReport(BookingHistory history) {
        System.out.println("\n--- Booking History Report ---\n");
        List<Reservation> reservations = history.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            r.displayReservation();
        }

        // Optional: show summary counts per room type
        Map<String, Integer> countMap = new HashMap<>();
        for (Reservation r : reservations) {
            countMap.put(r.getRoomType(), countMap.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\n--- Summary ---");
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Booked: " + entry.getValue());
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("      Welcome to Book My Stay App");
        System.out.println("         Version: v8.0");
        System.out.println("=====================================");

        // Initialize booking history and report service
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Sample confirmed reservations
        Reservation res1 = new Reservation("Alice", "Single Room", "SI101");
        Reservation res2 = new Reservation("Bob", "Double Room", "DO102");
        Reservation res3 = new Reservation("Charlie", "Suite Room", "SU103");

        // Add to booking history
        history.addReservation(res1);
        history.addReservation(res2);
        history.addReservation(res3);

        // Generate report
        reportService.generateReport(history);
    }
}