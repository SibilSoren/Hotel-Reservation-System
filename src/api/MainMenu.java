package api;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Pattern;

import common.Utils;
import model.IRoom;
import model.Reservation;

public class MainMenu {
    HotelResource hotelResource = new HotelResource();
    AdminMenu adminMenu = new AdminMenu();
    Utils utils = new Utils();

    private static final String dateRegex = "^(\\d{2})/(\\d{2})/(\\d{4})$";
    private static final Pattern pattern = Pattern.compile(dateRegex);

    public void runMenu() {
        boolean keepRunning = true;
        try (Scanner sc = new Scanner(System.in)) {
            while (keepRunning) {
                System.out.println("1. Find and reserve a room");
                System.out.println("2. See my reservations");
                System.out.println("3. Create an account");
                System.out.println("4. Admin");
                System.out.println("5. Exit");
                try {
                    int option = sc.nextInt();
                    sc.nextLine(); // Consume the leftover newline after nextInt()
                    switch (option) {
                        case 1:
                            findAndReserveRoom(sc);
                            break;
                        case 2:
                            seeMyReservations(sc);
                            break;
                        case 3:
                            createAnAccount(sc);
                            break;
                        case 4:
                            adminMenu.runMenu(sc);
                            break;
                        case 5:
                            keepRunning = false;
                            break;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("\nInvalid input\n");
                    sc.nextLine(); // Consume the invalid input to prevent infinite loop
                }
            }
        }
    }

    public void findAndReserveRoom(Scanner sc) {
        try {
            System.out.println("Provide your check in date: (DD/MM/YYYY)");
            Calendar checkInDate = Calendar.getInstance();
            String dateInput = sc.nextLine();
            String[] dateSplit = dateInput.split("/");
            if (!pattern.matcher(dateInput).matches()) {
                throw new IllegalArgumentException("Invalid date format");
            }
            if (!utils.isValidDate(dateSplit)) {
                throw new IllegalArgumentException("Invalid date");
            }
            int day = utils.convertStringToNumber(dateSplit[0]);
            int month = utils.convertStringToNumber(dateSplit[1]) - 1; // Months are 0-indexed
            int year = utils.convertStringToNumber(dateSplit[2]);
            checkInDate.set(year, month, day);

            System.out.println("Provide your check out date: (DD/MM/YYYY)");
            Calendar checkOutDate = Calendar.getInstance();
            dateInput = sc.nextLine();
            dateSplit = dateInput.split("/");
            if (!pattern.matcher(dateInput).matches()) {
                throw new IllegalArgumentException("Invalid date format");
            }
            if (!utils.isValidDate(dateSplit)) {
                throw new IllegalArgumentException("Invalid date");
            }
            day = utils.convertStringToNumber(dateSplit[0]);
            month = utils.convertStringToNumber(dateSplit[1]) - 1; // Months are 0-indexed
            year = utils.convertStringToNumber(dateSplit[2]);
            checkOutDate.set(year, month, day);

            Collection<IRoom> availableRooms = hotelResource.findARoom(checkInDate.getTime(),
                    checkOutDate.getTime());

            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for the given dates");
            } else {
                System.out.println("Available rooms:");
                for (IRoom room : availableRooms) {
                    System.out.println(room);
                }

                // Ask if user has an account
                System.out.println("Do you have an account? (Y/N):");
                String hasAccount = sc.nextLine().trim().toUpperCase();

                if (hasAccount.equals("N")) {
                    System.out.println("Please create an account first.");
                    createAnAccount(sc);
                    System.out.println("Account created! Now let's complete your booking.");
                }

                System.out.println("Please select a room:");
                String roomNumber = sc.nextLine();
                IRoom room = hotelResource.getRoom(roomNumber);

                System.out.println("Provide your email:");
                String customerEmail = sc.nextLine();

                // Check if customer exists
                if (hotelResource.getCustomer(customerEmail) == null) {
                    System.out.println("No account found with this email. Please create an account first.");
                    return;
                }

                Reservation reservation = hotelResource.bookARoom(customerEmail, room, checkInDate.getTime(),
                        checkOutDate.getTime());
                System.out.println("Room booked successfully: " + reservation);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void seeMyReservations(Scanner sc) {
        try {
            System.out.println("Provide your email:");
            String email = sc.nextLine();
            Collection<Reservation> reservations = hotelResource.getCustomerReservations(email);
            if (reservations == null || reservations.isEmpty()) {
                System.out.println("No reservations found for this email");
            } else {
                System.out.println("Your reservations:");
                for (Reservation reservation : reservations) {
                    System.out.println(reservation);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Returns to main menu automatically
    }

    public void createAnAccount(Scanner sc) {
        try {
            System.out.println("Provide your email:");
            String email = sc.nextLine();
            System.out.println("Provide your first name:");
            String firstName = sc.nextLine();
            System.out.println("Provide your last name:");
            String lastName = sc.nextLine();
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Returns to main menu automatically
    }
}
