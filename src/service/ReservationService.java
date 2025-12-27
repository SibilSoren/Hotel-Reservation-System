package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.util.*;

public class ReservationService {
    // Singleton instance
    private static final ReservationService instance = new ReservationService();
    private static final Map<String, IRoom> rooms = new HashMap<String, IRoom>();
    private static final Map<String, ArrayList<Reservation>> reservations = new HashMap<String, ArrayList<Reservation>>();

    // Private constructor prevents instantiation
    private ReservationService() {
    }

    // Static accessor
    public static ReservationService getInstance() {
        return instance;
    }

    public void addRoom(IRoom room) {
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        addReservationToMap(customer.getEmail(), reservation);
        return reservation;
    }

    private void addReservationToMap(String customerEmail, Reservation reservation) {
        if (reservations.containsKey(customerEmail)) {
            ArrayList<Reservation> userReservations = reservations.get(customerEmail);
            userReservations.add(reservation);
        } else {
            reservations.put(customerEmail, new ArrayList<Reservation>(List.of(reservation)));
        }
    }

    public void printAllReservation() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found");
            return;
        }
        for (String email : reservations.keySet()) {
            System.out.println(reservations.get(email));
        }
    }

    public Collection<Reservation> getCustomerReservations(String customerEmail) {
        ArrayList<Reservation> customerReservations = reservations.get(customerEmail);
        return customerReservations;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate, Boolean filterFreeRooms) {
        Collection<IRoom> availableRooms = new ArrayList<>();
        Set<String> reservedRooms = getReservedRoomNumbers(checkInDate, checkOutDate);

        for (IRoom room : rooms.values()) {
            if (!reservedRooms.contains(room.getRoomNumber())) {
                // Apply filter if specified
                if (filterFreeRooms == null || filterFreeRooms == room.isFree()) {
                    availableRooms.add(room);
                }
            }
        }

        return availableRooms;
    }

    private Set<String> getReservedRoomNumbers(Date checkInDate, Date checkOutDate) {
        Set<String> reservedRooms = new HashSet<>();
        for (ArrayList<Reservation> userReservations : reservations.values()) {
            for (Reservation reservation : userReservations) {
                if (checkInDate.before(reservation.getCheckOutDate())
                        && checkOutDate.after(reservation.getCheckInDate())) {
                    reservedRooms.add(reservation.getRoom().getRoomNumber());
                }
            }
        }
        return reservedRooms;
    }
}
