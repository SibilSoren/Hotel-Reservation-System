package api;

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
            checkInDate.set(Calendar.HOUR_OF_DAY, 0);
            checkInDate.set(Calendar.MINUTE, 0);
            checkInDate.set(Calendar.SECOND, 0);
            checkInDate.set(Calendar.MILLISECOND, 0);

            // Validate check-in date is not in the past
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            if (checkInDate.before(today)) {
                throw new IllegalArgumentException("Check-in date cannot be in the past");
            }

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
            checkOutDate.set(Calendar.HOUR_OF_DAY, 0);
            checkOutDate.set(Calendar.MINUTE, 0);
            checkOutDate.set(Calendar.SECOND, 0);
            checkOutDate.set(Calendar.MILLISECOND, 0);

            // Validate checkout date is after check-in date
            if (!checkOutDate.after(checkInDate)) {
                throw new IllegalArgumentException("Check-out date must be after check-in date");
            }

            // Ask for room type preference
            System.out.println("What type of room? (1) All  (2) Free only  (3) Paid only:");
            String roomTypeChoice = sc.nextLine().trim();
            Boolean filterFreeRooms = null; // default: all rooms
            if (roomTypeChoice.equals("2")) {
                filterFreeRooms = true; // free only
            } else if (roomTypeChoice.equals("3")) {
                filterFreeRooms = false; // paid only
            }

            Collection<IRoom> availableRooms = hotelResource.findARoom(checkInDate.getTime(),
                    checkOutDate.getTime(), filterFreeRooms);

            if (availableRooms.isEmpty()) {
                // Try +7 days recommendation
                System.out.println("No rooms available for the given dates. Searching for rooms 7 days later...");

                Calendar recommendedCheckIn = (Calendar) checkInDate.clone();
                Calendar recommendedCheckOut = (Calendar) checkOutDate.clone();
                recommendedCheckIn.add(Calendar.DAY_OF_MONTH, 7);
                recommendedCheckOut.add(Calendar.DAY_OF_MONTH, 7);

                Collection<IRoom> recommendedRooms = hotelResource.findARoom(
                        recommendedCheckIn.getTime(), recommendedCheckOut.getTime(), filterFreeRooms);

                if (recommendedRooms.isEmpty()) {
                    System.out.println("No rooms available for the recommended dates (+7 days) either.");
                    return;
                }

                // Format dates for display
                String recCheckIn = String.format("%02d/%02d/%04d",
                        recommendedCheckIn.get(Calendar.DAY_OF_MONTH),
                        recommendedCheckIn.get(Calendar.MONTH) + 1,
                        recommendedCheckIn.get(Calendar.YEAR));
                String recCheckOut = String.format("%02d/%02d/%04d",
                        recommendedCheckOut.get(Calendar.DAY_OF_MONTH),
                        recommendedCheckOut.get(Calendar.MONTH) + 1,
                        recommendedCheckOut.get(Calendar.YEAR));

                System.out.println("Recommended rooms available from " + recCheckIn + " to " + recCheckOut + ":");
                for (IRoom room : recommendedRooms) {
                    System.out.println(room);
                }

                System.out.println("Would you like to book for these recommended dates? (Y/N):");
                String bookRecommended = sc.nextLine().trim().toUpperCase();
                if (!bookRecommended.equals("Y")) {
                    System.out.println("Returning to main menu.");
                    return;
                }

                // Update dates to recommended dates for booking
                checkInDate = recommendedCheckIn;
                checkOutDate = recommendedCheckOut;
                availableRooms = recommendedRooms;
            }

            // Display available rooms (either original or recommended)
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
