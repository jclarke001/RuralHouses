package dataAccess;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

import domain.Account;
import domain.Booking;
import domain.BookingStatus;
import domain.Customer;
import domain.District;
import domain.DomainObject;
import domain.Offer;
import domain.Owner;
import domain.RuralHouse;
import domain.User;
import domain.UserType;
import exceptions.CorruptedCache;
import exceptions.CorruptedDatabase;
import exceptions.NoDigestAlgorithm;
import exceptions.OfferAlreadyBooked;
import exceptions.RemoteDesyncException;
import exceptions.UsernameExists;

public interface DataAccessInterface {

	// ==== MANAGEMENT ====

	public void close() throws RemoteException;

	public void reset() throws RemoteException, NoDigestAlgorithm;

	// ==== QUERYING ====

	public DomainObject get(DomainObject domainObject);

	public DomainObject get(long id);

	public Account getAccount(String username) throws RemoteException,
			CorruptedCache;

	public Account getAccount(User user) throws RemoteException, CorruptedCache;

	public List<Account> getAccounts(UserType type) throws RemoteException;

	public List<RuralHouse> getRuralHouses() throws RemoteException;

	public List<RuralHouse> getRuralHouses(Owner owner) throws RemoteException;

	public List<Owner> getOwners() throws RemoteException;

	public List<RuralHouse> getRuralHouses(District district)
			throws RemoteException;

	public List<RuralHouse> searchRuralHouses(Owner owner, String description,
			District district, int nBedrooms, int nBathrooms, int nKitchens,
			int nDiningrooms, int nParkingSpaces) throws RemoteException,
			RemoteDesyncException;

	public Offer getOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay)
			throws RemoteException, RemoteDesyncException, CorruptedCache;

	public List<Offer> getOffers(RuralHouse ruralHouse) throws RemoteException,
			RemoteDesyncException;

	public List<Offer> getOffers(Owner owner) throws RemoteException,
			RemoteDesyncException;

	public List<Offer> getOffers(Owner owner, Date lastDate)
			throws RemoteException, RemoteDesyncException;

	public List<Offer> getOffers(RuralHouse ruralHouse, Date firstDay,
			Date lastDay) throws RemoteException, RemoteDesyncException;

	public List<Booking> getBookings(Customer customer) throws RemoteException;

	public List<Booking> getBookings(BookingStatus status, Owner owner)
			throws RemoteException;

	// ==== ADDING OBJECTS ====
	public Account addAccount(UserType type, String username, String password,
			String name) throws NoDigestAlgorithm, UsernameExists;

	public RuralHouse addRuralHouse(Owner owner, String description,
			District district, int nBedrooms, int nBathrooms, int nKitchens,
			int nDiningrooms, int nParkingSpaces) throws RemoteException,
			RemoteDesyncException;

	public Offer addOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay,
			float price) throws RemoteException, RemoteDesyncException;

	public Booking addBooking(Offer offer, Customer customer, String telephone)
			throws RemoteException, RemoteDesyncException, OfferAlreadyBooked;

	public Booking addBooking(Offer offer, String telephone)
			throws RemoteException, RemoteDesyncException, OfferAlreadyBooked;

	// ==== UPDATING OBJECTS ====

	public Account update(Account account, String newPassword)
			throws RemoteException, RemoteDesyncException, NoDigestAlgorithm;

	public Account update(User user, String newPassword)
			throws RemoteException, RemoteDesyncException, NoDigestAlgorithm;

	public Account update(Customer customer, String name)
			throws RemoteException, RemoteDesyncException, CorruptedCache;

	public Account update(Owner owner, String name, String bankAccount)
			throws RemoteException, RemoteDesyncException, CorruptedCache;

	public RuralHouse update(RuralHouse ruralHouse, Owner owner,
			String description, District district, int nBedrooms,
			int nBathrooms, int nKitchens, int nDiningrooms, int nParkingSpaces)
			throws RemoteException, RemoteDesyncException;

	public Booking update(Booking booking, BookingStatus status)
			throws RemoteException, RemoteDesyncException;

	// ==== REMOVING OBJECTS ====

	public void remove(Customer customer) throws RemoteException,
			RemoteDesyncException, CorruptedDatabase, CorruptedCache;

	public List<String> remove(Owner owner) throws RemoteException,
			RemoteDesyncException, CorruptedDatabase, CorruptedCache;

	public List<String> remove(RuralHouse ruralHouse) throws RemoteException,
			RemoteDesyncException;

	public String remove(Offer offer) throws RemoteException,
			RemoteDesyncException;

	public void remove(Booking booking) throws RemoteException,
			RemoteDesyncException;
}
