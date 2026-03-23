/**
 * BookMyStayApp
 * Demonstrates booking request handling using a Queue (FIFO).
 * Requests are stored in arrival order without modifying inventory.
 *
 * @author YourName
 * @version 5.0
 */

import java.util.LinkedList;
import java.util.Queue;

// Reservation class (represents booking request)
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
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// Booking Queue (FIFO)
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // Display all requests (without removing)
    public void displayRequests() {
        System.out.println("\n--- Booking Request Queue ---\n");

        for (Reservation r : queue) {
            r.displayReservation();
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("      Welcome to Book My Stay App");
        System.out.println("         Version: v5.0");
        System.out.println("=====================================");

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate incoming booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        // Display queue (FIFO order)
        bookingQueue.displayRequests();

        // NOTE: No inventory updates or room allocation here
    }
}