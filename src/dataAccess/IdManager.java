package dataAccess;

import java.util.HashSet;
import java.util.Iterator;

class IdManager {
	private long nextId;
	private HashSet<Long> freedIds;

	IdManager() {
		this.nextId = 0;
		this.freedIds = new HashSet<Long>();
	}

	public long getFreeId() {
		Iterator<Long> iter = this.freedIds.iterator();
		if (iter.hasNext()) {
			long id = iter.next();
			this.freedIds.remove(id);
			return id;
		} else {
			return this.nextId++;
		}
	}

	public void freeId(long id) {
		this.freedIds.add(id);
	}
}