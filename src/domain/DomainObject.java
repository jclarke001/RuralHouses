package domain;

import java.io.Serializable;
import java.util.Comparator;

public abstract class DomainObject implements Comparator<DomainObject>,
		Serializable {
	private static final long serialVersionUID = 1L;
	private long id;

	public DomainObject(long id) {
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	public boolean equals(DomainObject o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		if (id != o.getId())
			return false;
		return true;
	}

	public int compare(DomainObject o1, DomainObject o2) {
		if (o1.getId() < o2.getId())
			return -1;
		else if (o1.getId() > o2.getId())
			return 1;
		else
			return 0;
	}

	public String toString() {
		return Long.toString(this.id);
	}
}
