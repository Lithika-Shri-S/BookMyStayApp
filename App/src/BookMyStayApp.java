import java.util.HashMap;
import java.util.Map;

// Inventory Class
class RoomInventory {

    // HashMap to store room type and availability
    private Map<String, Integer> inventory;

    // Constructor initializes inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability of a specific room
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, count);
        } else {
            System.out.println("Room type not found.");
        }
    }

    // Method to display full inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---\n");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("      Welcome to Book My Stay App");
        System.out.println("         Version: v3.1");
        System.out.println("=====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display current inventory
        inventory.displayInventory();

        // Example update
        System.out.println("\nUpdating Single Room availability...\n");
        inventory.updateAvailability("Single Room", 4);

        // Display updated inventory
        inventory.displayInventory();
    }
}