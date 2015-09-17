package businessLogic;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

import domain.Booking;
import domain.Customer;
import domain.District;
import domain.Offer;
import domain.Owner;
import domain.RuralHouse;
import domain.User;
import domain.UserType;
import exceptions.AuthenticationError;
import exceptions.BadDates;
import exceptions.CorruptedCache;
import exceptions.CorruptedDatabase;
import exceptions.InvalidPassword;
import exceptions.InvalidToken;
import exceptions.NoDigestAlgorithm;
import exceptions.OfferAlreadyBooked;
import exceptions.OverlappingOfferExists;
import exceptions.RegisterPropertyValidationError;
import exceptions.RemoteDesyncException;
import exceptions.UsernameExists;

public interface FacadeInterface extends Remote {

	// Management
	public void close() throws RemoteException;

	// ANONYMOUS & CUSTOMER

	public List<Owner> getOwners() throws RemoteException;

	// Query availability
	public RuralHouse getRuralHouse(long id) throws RemoteException,
			RemoteDesyncException;

	public List<RuralHouse> getRuralHouses() throws RemoteException,
			RemoteDesyncException;

	// Book rural house
	public RuralHouse getRuralHouse(RuralHouse ruralHouse)
			throws RemoteException, RemoteDesyncException;

	public List<Offer> getOffers(RuralHouse ruralHouse, Date firstDay,
			Date lastDay) throws RemoteException, RemoteDesyncException;

	public Booking makeBooking(Offer offer, Customer customer, String telephone)
			throws RemoteException, RemoteDesyncException, OfferAlreadyBooked;

	public Booking makeBooking(Offer offer, String telephone)
			throws RemoteException, RemoteDesyncException, OfferAlreadyBooked;

	// CUSTOMER

	// Edit profile
	public void editProfileCustomer(List<Byte> token, String name)
			throws RemoteException, InvalidToken, CorruptedDatabase,
			RemoteDesyncException, CorruptedCache;

	// Cancel booking
	public List<Booking> getBookings(List<Byte> token) throws RemoteException,
			InvalidToken;

	public void cancelBooking(List<Byte> token, Booking booking)
			throws RemoteException, InvalidToken, RemoteDesyncException;

	// Search house
	public List<RuralHouse> searchRuralHouses(Owner owner, String description,
			District district, int nBedrooms, int nBathrooms, int nKitchens,
			int nDiningrooms, int nParkingSpaces) throws RemoteException,
			RemoteDesyncException;

	// OWNER

	// Edit profile
	public void editProfileOwner(List<Byte> token, String name,
			String bankAccount) throws RemoteException, InvalidToken,
			CorruptedDatabase, RemoteDesyncException, CorruptedCache;

	// Set availability
	public Offer addOffer(List<Byte> token, RuralHouse ruralHouse,
			Date firstDay, Date lastDay, float price)
			throws OverlappingOfferExists, BadDates, RemoteException,
			InvalidToken, RemoteDesyncException;

	// Unset availability
	public List<Offer> getOffers(List<Byte> token) throws InvalidToken,
			RemoteException, RemoteDesyncException;

	public String removeOffer(List<Byte> token, Offer offer)
			throws RemoteException, InvalidToken, RemoteDesyncException;

	// Review booking
	public List<Booking> getPendingBookings(List<Byte> token)
			throws RemoteException, InvalidToken;

	public void acceptBooking(List<Byte> token, Booking booking)
			throws RemoteException, InvalidToken, RemoteDesyncException;

	public void denyBooking(List<Byte> token, Booking booking)
			throws RemoteException, InvalidToken, RemoteDesyncException;

	public void restoreBooking(List<Byte> token, Booking booking)
			throws RemoteException, InvalidToken, RemoteDesyncException;

	// Remove out dated offers
	public List<Offer> getOutDatedOffers(Owner owner, Date currentDate)
			throws RemoteException, RemoteDesyncException;

	public void removeOutDatedOffers(List<Byte> token, Date currentDate)
			throws InvalidToken, RemoteException, RemoteDesyncException;

	// ADMINISTRATOR

	// Register property
	public RuralHouse addRuralHouse(List<Byte> token, Owner owner,
			String description, District district, int nBedrooms,
			int nBathrooms, int nKitchens, int nDiningrooms, int nParkingSpaces)
			throws RemoteException, InvalidToken, RemoteDesyncException,
			RegisterPropertyValidationError;

	// Edit property
	public void editRuralHouse(List<Byte> token, RuralHouse ruralHouse,
			Owner owner, String description, District district, int nBedrooms,
			int nBathrooms, int nKitchens, int nDiningrooms, int nParkingSpaces)
			throws RemoteException, InvalidToken, RemoteDesyncException,
			RegisterPropertyValidationError;

	// Withdraw property
	public List<RuralHouse> getRuralHouses(List<Byte> token)
			throws RemoteException, InvalidToken;

	public List<String> removeRuralHouse(List<Byte> token, RuralHouse rh)
			throws RemoteException, InvalidToken, RemoteDesyncException;

	// Reset DB
	public void resetDB(List<Byte> token, String password)
			throws RemoteException, InvalidToken, NoDigestAlgorithm,
			InvalidPassword;

	// AUTHENTICATION

	public User createAccount(List<Byte> token, UserType type, String username,
			String password, String name) throws RemoteException,
			UsernameExists, NoDigestAlgorithm, InvalidToken;

	public List<Byte> login(String username, String password)
			throws RemoteException, AuthenticationError, NoDigestAlgorithm,
			CorruptedCache;

	public User getUserByToken(List<Byte> token) throws RemoteException;

	public UserType getUserType(List<Byte> token) throws RemoteException,
			InvalidToken;

	public void changePassword(List<Byte> token, String oldPassword,
			String newPassword) throws RemoteException, RemoteDesyncException,
			InvalidToken, NoDigestAlgorithm, InvalidPassword;

	public void changePasswordAdmin(List<Byte> adminToken, Owner owner,
			String newPassword) throws InvalidToken, RemoteException,
			RemoteDesyncException, NoDigestAlgorithm;

	public void removeCustomer(List<Byte> token, String password)
			throws RemoteException, RemoteDesyncException, InvalidToken,
			InvalidPassword, NoDigestAlgorithm, CorruptedDatabase,
			CorruptedCache;

	public List<String> removeOwner(List<Byte> token, Owner owner) throws InvalidToken,
			RemoteException, RemoteDesyncException, CorruptedDatabase,
			CorruptedCache;

	public void logout(List<Byte> token) throws RemoteException;

}
