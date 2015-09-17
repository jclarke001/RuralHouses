package businessLogic;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import configuration.ConfigXML;
import dataAccess.DataAccessInterface;
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

public class CacheManager implements DataAccessInterface {

	ConcurrentHashMap<Long, DomainObject> idMap;
	ConcurrentHashMap<String, List<DomainObject>> classMap;
	DataAccessInterface dbManager;
	int maxSize;

	public CacheManager(DataAccessInterface dbManager) throws NoDigestAlgorithm {
		idMap = new ConcurrentHashMap<Long, DomainObject>();
		classMap = new ConcurrentHashMap<String, List<DomainObject>>();
		this.dbManager = dbManager;
		maxSize = ConfigXML.getInstance().getCacheMaxSize();
	}

	// ==== MANAGEMENT ====

	public void close() throws RemoteException {
		dbManager.close();
	}

	public void reset() throws RemoteException, NoDigestAlgorithm {
		idMap = new ConcurrentHashMap<Long, DomainObject>();
		classMap = new ConcurrentHashMap<String, List<DomainObject>>();
		dbManager.reset();
	}

	private void mantain() {
		if (idMap.size() > maxSize) {
			idMap = new ConcurrentHashMap<Long, DomainObject>();
			classMap = new ConcurrentHashMap<String, List<DomainObject>>();
		}
	}

	// ==== QUERYING ====

	public DomainObject get(DomainObject domainObject) {
		return get(domainObject.getId());
	}

	public DomainObject get(long id) {
		mantain();
		if (idMap.containsKey(id))
			return idMap.get(id);
		return add(dbManager.get(id));
	}

	public Account getAccount(String username) throws RemoteException,
			CorruptedCache {
		mantain();
		if (classMap.containsKey(Account.class.getName())) {
			List<DomainObject> accounts = classMap.get(Account.class.getName());
			Account account = null;
			Account tmpAccount;
			for (DomainObject domainObject : accounts) {
				if (!(domainObject instanceof Account))
					throw new CorruptedCache();
				tmpAccount = (Account) domainObject;
				if (tmpAccount.getUsername().equals(username)) {
					account = tmpAccount;
					break;
				}
			}
			if (account != null)
				return account;
		}

		return (Account) add(dbManager.getAccount(username));
	}

	public Account getAccount(User user) throws RemoteException, CorruptedCache {
		mantain();
		if (classMap.containsKey(Account.class.getName())) {
			List<DomainObject> accounts = classMap.get(Account.class.getName());
			Account account = null;
			Account tmpAccount;
			for (DomainObject domainObject : accounts) {
				if (!(domainObject instanceof Account))
					throw new CorruptedCache();
				tmpAccount = (Account) domainObject;
				if (tmpAccount.getUser() != null
						&& tmpAccount.getUser().equals(user)) {
					account = tmpAccount;
					break;
				}
			}
			if (account != null)
				return account;
		}

		return (Account) add(dbManager.getAccount(user));
	}

	@SuppressWarnings("unchecked")
	public List<Account> getAccounts(UserType type) throws RemoteException {
		mantain();
		return (List<Account>) (List<?>) add((List<DomainObject>) (List<?>) dbManager
				.getAccounts(type));
	}

	@SuppressWarnings("unchecked")
	public List<Owner> getOwners() throws RemoteException {
		mantain();
		return (List<Owner>) (List<?>) add((List<DomainObject>) (List<?>) dbManager
				.getOwners());
	}

	@SuppressWarnings("unchecked")
	public List<RuralHouse> getRuralHouses() throws RemoteException {
		mantain();
		return (List<RuralHouse>) (List<?>) add((List<DomainObject>) (List<?>) dbManager
				.getRuralHouses());
	}

	@SuppressWarnings("unchecked")
	public List<RuralHouse> getRuralHouses(Owner owner) throws RemoteException {
		mantain();
		return (List<RuralHouse>) (List<?>) add((List<DomainObject>) (List<?>) dbManager
				.getRuralHouses(owner));
	}

	@SuppressWarnings("unchecked")
	public List<RuralHouse> getRuralHouses(District district)
			throws RemoteException {
		mantain();
		return (List<RuralHouse>) (List<?>) add((List<DomainObject>) (List<?>) dbManager
				.getRuralHouses(district));
	}

	@SuppressWarnings("unchecked")
	public List<RuralHouse> searchRuralHouses(Owner owner, String description,
			District district, int nBedrooms, int nBathrooms, int nKitchens,
			int nDiningrooms, int nParkingSpaces) throws RemoteException,
			RemoteDesyncException {
		mantain();
		return (List<RuralHouse>) (List<?>) add((List<DomainObject>) (List<?>) dbManager
				.searchRuralHouses(owner, description, district, nBedrooms,
						nBathrooms, nKitchens, nDiningrooms, nParkingSpaces));
	}

	public Offer getOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay)
			throws RemoteException, RemoteDesyncException, CorruptedCache {
		mantain();
		if (classMap.contains(Offer.class.getName())) {
			List<DomainObject> offers = classMap.get(Offer.class.getName());
			Offer offer = null;
			Offer tmpOffer;
			for (DomainObject domainObject : offers) {
				if (!(domainObject instanceof Offer))
					throw new CorruptedCache();
				tmpOffer = (Offer) domainObject;
				if (tmpOffer.getRuralHouse().equals(ruralHouse)
						&& tmpOffer.getFirstDay().equals(firstDay)
						&& tmpOffer.getLastDay().equals(firstDay)) {
					offer = tmpOffer;
					break;
				}
			}
			if (offer != null)
				return offer;
		}

		return (Offer) add(dbManager.getOffer(ruralHouse, firstDay, lastDay));
	}

	@SuppressWarnings("unchecked")
	public List<Offer> getOffers(RuralHouse ruralHouse) throws RemoteException,
			RemoteDesyncException {
		mantain();
		return (List<Offer>) (List<?>) add((List<DomainObject>) (List<?>) dbManager
				.getOffers(ruralHouse));
	}

	@SuppressWarnings("unchecked")
	public List<Offer> getOffers(Owner owner) throws RemoteException,
			RemoteDesyncException {
		mantain();
		return (List<Offer>) (List<?>) add((List<DomainObject>) (List<?>) dbManager
				.getOffers(owner));
	}

	@SuppressWarnings("unchecked")
	public List<Offer> getOffers(Owner owner, Date lastDate)
			throws RemoteException, RemoteDesyncException {
		mantain();
		return (List<Offer>) (List<?>) add((List<DomainObject>) (List<?>) dbManager
				.getOffers(owner, lastDate));
	}

	@SuppressWarnings("unchecked")
	public List<Offer> getOffers(RuralHouse ruralHouse, Date firstDay,
			Date lastDay) throws RemoteException, RemoteDesyncException {
		mantain();
		return (List<Offer>) (List<?>) add((List<DomainObject>) (List<?>) dbManager
				.getOffers(ruralHouse, firstDay, lastDay));
	}

	@SuppressWarnings("unchecked")
	public List<Booking> getBookings(Customer customer) throws RemoteException {
		mantain();
		return (List<Booking>) (List<?>) add((List<DomainObject>) (List<?>) dbManager
				.getBookings(customer));
	}

	@SuppressWarnings("unchecked")
	public List<Booking> getBookings(BookingStatus status, Owner owner)
			throws RemoteException {
		mantain();
		return (List<Booking>) (List<?>) add((List<DomainObject>) (List<?>) dbManager
				.getBookings(status, owner));
	}

	// ==== ADDING OBJECTS ====

	// TODO: Make sure that it gets the correct class names and not the name of
	// DomainObject
	private DomainObject add(DomainObject domainObject) {
		if (domainObject != null && !idMap.containsKey(domainObject.getId())) {
			idMap.put(domainObject.getId(), domainObject);
			if (!classMap.contains(domainObject.getClass().getName()))
				classMap.put(domainObject.getClass().getName(),
						new Vector<DomainObject>());
			classMap.get(domainObject.getClass().getName()).add(domainObject);
		}
		return domainObject;
	}

	private List<DomainObject> add(List<DomainObject> domainObjects) {
		for (DomainObject domainObject : domainObjects)
			add(domainObject);
		return domainObjects;
	}

	public Account addAccount(UserType type, String username, String password,
			String name) throws NoDigestAlgorithm, UsernameExists {
		mantain();
		return (Account) add(dbManager.addAccount(type, username, password,
				name));
	}

	public RuralHouse addRuralHouse(Owner owner, String description,
			District district, int nBedrooms, int nBathrooms, int nKitchens,
			int nDiningrooms, int nParkingSpaces) throws RemoteException,
			RemoteDesyncException {
		mantain();
		return (RuralHouse) add(dbManager.addRuralHouse(owner, description,
				district, nBedrooms, nBathrooms, nKitchens, nDiningrooms,
				nParkingSpaces));
	}

	public Offer addOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay,
			float price) throws RemoteException, RemoteDesyncException {
		mantain();
		return (Offer) add(dbManager.addOffer(ruralHouse, firstDay, lastDay,
				price));
	}

	public Booking addBooking(Offer offer, Customer customer, String telephone)
			throws RemoteException, RemoteDesyncException, OfferAlreadyBooked {
		mantain();
		return (Booking) add(dbManager.addBooking(offer, customer, telephone));
	}

	public Booking addBooking(Offer offer, String telephone)
			throws RemoteException, RemoteDesyncException, OfferAlreadyBooked {
		mantain();
		return (Booking) add(dbManager.addBooking(offer, telephone));
	}

	// ==== UPDATING OBJECTS ====

	private DomainObject update(DomainObject domainObject)
			throws RemoteDesyncException {
		if (idMap.containsKey(domainObject.getId())) {
			remove(domainObject);
			return add(domainObject);
		}
		return domainObject;
	}

	public Account update(Account account, String newPassword)
			throws RemoteException, RemoteDesyncException, NoDigestAlgorithm {
		mantain();
		return (Account) update(dbManager.update(account, newPassword));
	}

	public Account update(User user, String newPassword)
			throws RemoteException, RemoteDesyncException, NoDigestAlgorithm {
		mantain();
		return (Account) update(dbManager.update(user, newPassword));
	}

	public Account update(Customer customer, String name)
			throws RemoteException, RemoteDesyncException, CorruptedCache {
		mantain();
		return (Account) update(dbManager.update(customer, name));
	}

	public Account update(Owner owner, String name, String bankAccount)
			throws RemoteException, RemoteDesyncException, CorruptedCache {
		mantain();
		return (Account) update(dbManager.update(owner, name, bankAccount));
	}

	public RuralHouse update(RuralHouse ruralHouse, Owner owner,
			String description, District district, int nBedrooms,
			int nBathrooms, int nKitchens, int nDiningrooms, int nParkingSpaces)
			throws RemoteException, RemoteDesyncException {
		mantain();
		return (RuralHouse) update(dbManager.update(ruralHouse, owner,
				description, district, nBedrooms, nBathrooms, nKitchens,
				nDiningrooms, nParkingSpaces));
	}

	public Booking update(Booking booking, BookingStatus status)
			throws RemoteException, RemoteDesyncException {
		mantain();
		return (Booking) update(dbManager.update(booking, status));
	}

	// ==== REMOVING OBJECTS ====

	private void remove(DomainObject domainObject) {
		if (idMap.containsKey(domainObject.getId())) {
			domainObject = get(domainObject);
			idMap.remove(domainObject.getId());
			List<DomainObject> domainObjects = classMap.get(domainObject
					.getClass().getName());
			if (domainObjects != null) {
				if (domainObjects.size() == 1)
					classMap.remove(domainObject.getClass().getName());
				else
					domainObjects.remove(domainObject);
			}
		}
	}

	public void remove(Customer customer) throws RemoteException,
			RemoteDesyncException, CorruptedDatabase, CorruptedCache {
		mantain();
		for (Booking booking : customer.getBookings())
			remove((DomainObject) booking);
		remove((DomainObject) getAccount(customer));
		remove((DomainObject) customer);
		dbManager.remove(customer);
	}

	public List<String> remove(Owner owner) throws RemoteException,
			RemoteDesyncException, CorruptedDatabase, CorruptedCache {
		mantain();
		for (RuralHouse ruralHouse : owner.getRuralHouses()) {
			for (Offer offer : ruralHouse.getOffers()) {
				Booking b = offer.getBooking();
				if (b != null)
					remove((DomainObject)b);
				remove((DomainObject) offer);
			}
			remove((DomainObject) ruralHouse);
		}
		remove((DomainObject) getAccount(owner));
		remove((DomainObject) owner);
		return dbManager.remove(owner);
	}

	public List<String> remove(RuralHouse ruralHouse) throws RemoteException,
			RemoteDesyncException {
		mantain();
		for (Offer offer : ruralHouse.getOffers()) {
			Booking b = offer.getBooking();
			if (b != null)
				remove((DomainObject)b);
			remove((DomainObject) offer);
		}
		remove((DomainObject) ruralHouse);
		return dbManager.remove(ruralHouse);
	}

	public String remove(Offer offer) throws RemoteException,
			RemoteDesyncException {
		mantain();
		Booking b = offer.getBooking();
		if (b != null)
			remove((DomainObject)b);
		remove((DomainObject) offer);
		return dbManager.remove(offer);
	}

	public void remove(Booking booking) throws RemoteException,
			RemoteDesyncException {
		mantain();
		remove((DomainObject) booking);
		dbManager.remove(booking);
	}
}
