package businessLogic;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import configuration.ConfigXML;
import domain.Account;

public class SessionManager {
	private int numberOfCycles;
	private long millisecondsPerCycle;

	private ConcurrentHashMap<List<Byte>, Account> loggedUsers;
	private ConcurrentHashMap<List<Byte>, Integer> cyclesLeft;
	private Timer timer;

	private class Task extends TimerTask {
		public void run() {
			for (List<Byte> token: cyclesLeft.keySet()) {
				Integer left = cyclesLeft.get(token);
				if (left != null) {
					left -= 1;
					cyclesLeft.replace(token, left);
					if (left <= 0) {
						cyclesLeft.remove(token);
						loggedUsers.remove(token);
					}
				}
			}
		}
	}


	private void update(List<Byte> token) {
		if (cyclesLeft.replace(token,  numberOfCycles) == null);
			cyclesLeft.putIfAbsent(token,  numberOfCycles);
	}

	public SessionManager() {
		ConfigXML configXML = ConfigXML.getInstance();
		millisecondsPerCycle = configXML.getMillisecondsPerCycle();
		numberOfCycles = configXML.getNumberOfCycles();
		loggedUsers = new ConcurrentHashMap<>();
		cyclesLeft = new ConcurrentHashMap<>();
		timer = new Timer();
		timer.schedule(new Task(), 0, millisecondsPerCycle);
	}

	public boolean containsKey(List<Byte> token) {
		update(token);
		return loggedUsers.containsKey(token);
	}

	public Account get(List<Byte> token) {
		update(token);
		return loggedUsers.get(token);
	}

	public void put(List<Byte> token, Account account) {
		update(token);
		loggedUsers.put(token, account);
	}

	public void remove(List<Byte> token) {
		loggedUsers.remove(token);
	}

	public void clear() {
		loggedUsers.clear();
	}

	public void close() {
		timer.cancel();
	}

}
