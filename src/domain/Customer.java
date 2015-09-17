package domain;

import java.util.Vector;

public class Customer extends User {

	private static final long serialVersionUID = -8096371620178349112L;
	private Vector<Booking> bookings;

	public Customer(long id, String name) {
		super(id, name);
		bookings = new Vector<Booking>();
	}

    public Vector<Booking> getBookings() {
        return bookings;
    }

    public void addBooking(Booking b) {
        bookings.add(b);
    }

    public void removeBooking(Booking b) {
        bookings.remove(b);
    }
}
