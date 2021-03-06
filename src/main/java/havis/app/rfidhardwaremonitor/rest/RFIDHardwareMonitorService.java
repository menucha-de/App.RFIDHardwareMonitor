package havis.app.rfidhardwaremonitor.rest;

import havis.app.rfidhardwaremonitor.Main;
import havis.app.rfidhardwaremonitor.RFIDHardwareMonitorException;
import havis.app.rfidhardwaremonitor.Tag;
import havis.middleware.tdt.TdtTranslationException;
import havis.net.rest.shared.Resource;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("app/rfidhardwaremonitor")
public class RFIDHardwareMonitorService extends Resource {

	private Main main;

	public RFIDHardwareMonitorService(Main main) {
		super();
		this.main = main;
	}

	@PermitAll
	@GET
	@Path("tags")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Tag> getTags() throws TdtTranslationException, RFIDHardwareMonitorException {
		return main.getTagList();
	}

	@PermitAll
	@POST
	@Path("observing")
	@Consumes({ MediaType.APPLICATION_JSON })
	public void setObserving(Boolean observing) {
		main.setObserving(observing);
	}

	@PermitAll
	@GET
	@Path("observing")
	@Produces({ MediaType.APPLICATION_JSON })
	public boolean isObserving() {
		return main.isObservering();
	}
}