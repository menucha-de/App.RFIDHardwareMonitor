package havis.custom.harting.rfidhardwaremonitor;

public class RFIDHardwareMonitorException extends Exception {

	private static final long serialVersionUID = 1L;

	public RFIDHardwareMonitorException(String message) {
		super(message);
	}

	public RFIDHardwareMonitorException(String message, Throwable cause) {
		super(message, cause);
	}

	public RFIDHardwareMonitorException(Throwable cause) {
		super(cause);
	}
}