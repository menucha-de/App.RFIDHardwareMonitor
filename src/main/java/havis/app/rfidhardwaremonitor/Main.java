package havis.app.rfidhardwaremonitor;

import havis.middleware.tdt.TdtTranslationException;

import java.util.List;
import java.util.logging.Logger;

public class Main {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(Main.class.getName());

	private final ReaderMonitor monitor = new ReaderMonitor();

	private boolean isObserving = false;

	public Main() {
	}

	public List<Tag> getTagList() throws RFIDHardwareMonitorException, TdtTranslationException {
		if (isObserving) {
			return monitor.getTagList();
		}
		throw new RFIDHardwareMonitorException("The observation is disabled");
	}

	public ReaderMonitor getMonitor() {
		return monitor;
	}

	public void setObserving(boolean value) {
		isObserving = value;
	}

	public boolean isObservering() {
		return isObserving;
	}
}