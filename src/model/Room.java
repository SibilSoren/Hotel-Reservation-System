package model;

public class Room implements IRoom {
    private final String roomNumber;
    private final Double price;
    private final RoomType roomType;

    public Room(String roomNumber, Double price, RoomType roomType) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be null or empty");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Room price cannot be negative");
        }
        if (roomType == null) {
            throw new IllegalArgumentException("Room type cannot be null");
        }
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return price;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean isFree() {
        return false;
    }

    @Override
    public String toString() {
        return "Room Number: " + roomNumber + " , Price: " + price + " ,Room Type: " + roomType;
    }
}
