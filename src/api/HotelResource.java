package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class HotelResource {
    CustomerService customerService = CustomerService.getInstance();
    ReservationService reservationService = ReservationService.getInstance();

    public Customer getCustomer(String email) {
        return this.customerService.getCustomer(email);
    }

    public Collection<Customer> getAllCustomers() {
        return this.customerService.getAllCustomers();
    }

    public void createACustomer(String email, String firstName, String lastName) {
        this.customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return this.reservationService.getARoom(roomNumber);
    }

    public Collection<IRoom> getAllRooms() {
        return this.reservationService.getAllRooms();
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        Customer customer = getCustomer(customerEmail);
        return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomerReservations(String customerEmail) {
        return reservationService.getCustomerReservations(customerEmail);
    }

    public void getAllReservations() {
        reservationService.printAllReservation();
    }

    public void addARoom(IRoom room) {
        this.reservationService.addRoom(room);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut, Boolean filterFreeRooms) {
        return reservationService.findRooms(checkIn, checkOut, filterFreeRooms);
    }
}
