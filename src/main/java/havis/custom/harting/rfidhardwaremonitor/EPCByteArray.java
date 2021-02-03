package havis.custom.harting.rfidhardwaremonitor;

import java.util.Arrays;

public class EPCByteArray {

	private byte[] epc;

	public EPCByteArray(byte[] epc) {
		this.epc = epc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(epc);
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
		EPCByteArray other = (EPCByteArray) obj;
		if (!Arrays.equals(epc, other.epc))
			return false;
		return true;
	}

}
