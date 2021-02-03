package havis.custom.harting.rfidhardwaremonitor;

import java.util.Date;

public class Tag {
	private String epc;
	private int antennaID;
	private int rssi;
	private Date timestamp;

	public Tag() {
	}

	public Tag(String epc, int antennaID, int rssi, Date timestamp) {
		if (epc != null) {
			this.epc = epc;
			this.antennaID = antennaID;
			this.rssi = rssi;
			this.timestamp = timestamp != null ? timestamp : new Date();
		}
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public String getEpc() {
		return epc;
	}

	public int getAntennaID() {
		return antennaID;
	}

	public void setAntennaID(int antennaID) {
		this.antennaID = antennaID;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + antennaID;
		result = prime * result + ((epc == null) ? 0 : epc.hashCode());
		result = prime * result + rssi;
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (antennaID != other.antennaID)
			return false;
		if (epc == null) {
			if (other.epc != null)
				return false;
		} else if (!epc.equals(other.epc))
			return false;
		if (rssi != other.rssi)
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tag [epc=" + epc + ", antennaID=" + antennaID + ", rssi=" + rssi + ", timestamp="
				+ timestamp + "]";
	}
}
