package api;

import java.util.Collection;
import java.util.Scanner;

import model.FreeRoom;
import model.IRoom;
import model.Reservation;
import model.Room;
import model.RoomType;

public class AdminMenu {

    HotelResource hotelResource = new HotelResource();

    public void runMenu(Scanner sc) {
        boolean keepRunning = true;
        while (keepRunning) {
            try {
                System.out.println("1. See all reservations");
                System.out.println("2. See all rooms");
                System.out.println("3. Add a room");
                System.out.println("4. Populate test data");
                System.out.println("5. Back to main menu");
                int option = sc.nextInt();
                sc.nextLine(); // Consume the leftover newline after nextInt()
                switch (option) {
                    case 1:
                        seeAllReservations();
                        break;
                    case 2:
                        seeAllRooms();
                        break;
                    case 3:
                        addARoom(sc);
                        break;
                    case 4:
                        populateTestData();
                        break;
                    case 5:
                        keepRunning = false;
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input");
                sc.nextLine(); // Consume invalid input to prevent infinite loop
            }
        }
    }

    public void seeAllReservations() {
        hotelResource.getAllReservations();
    }

    public void seeAllRooms() {
        Collection<IRoom> rooms = hotelResource.getAllRooms();
        for (IRoom room : rooms) {
            System.out.println(room);
        }
    }

    public void addARoom(Scanner sc) {
        // Get room number
        System.out.println("Enter room number:");
        String roomNumber = sc.nextLine();

        // Get price with validation loop
        double price = 0;
        boolean validPrice = false;
        while (!validPrice) {
            try {
                System.out.println("Enter room price:");
                String priceInput = sc.nextLine();
                price = Double.parseDouble(priceInput);
                validPrice = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Please enter a valid number.");
            }
        }

        // Get room type with validation loop
        RoomType roomType = null;
        boolean validType = false;
        while (!validType) {
            try {
                System.out.println("Enter room type (SINGLE/DOUBLE):");
                String typeInput = sc.nextLine().toUpperCase();
                roomType = RoomType.valueOf(typeInput);
                validType = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid room type. Please enter SINGLE or DOUBLE.");
            }
        }

        IRoom room = new Room(roomNumber, price, roomType);
        hotelResource.addARoom(room);
        System.out.println("Room added successfully!");
    }

    public void populateTestData() {
        // Add test customers
        hotelResource.createACustomer("john@test.com", "John", "Doe");
        hotelResource.createACustomer("jane@test.com", "Jane", "Smith");
        hotelResource.createACustomer("bob@test.com", "Bob", "Wilson");

        // Add test rooms (mix of paid and free)
        hotelResource.addARoom(new Room("101", 150.0, RoomType.SINGLE));
        hotelResource.addARoom(new Room("102", 200.0, RoomType.DOUBLE));
        hotelResource.addARoom(new Room("103", 175.0, RoomType.SINGLE));
        hotelResource.addARoom(new FreeRoom("201", 0.0, RoomType.SINGLE));
        hotelResource.addARoom(new FreeRoom("202", 0.0, RoomType.DOUBLE));

        System.out.println("Test data populated successfully!");
        System.out.println("- 3 customers added");
        System.out.println("- 5 rooms added (3 paid, 2 free)");
    }
}
