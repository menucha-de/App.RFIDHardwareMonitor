package havis.custom.harting.rfidhardwaremonitor.rest.async;

import havis.custom.harting.rfidhardwaremonitor.Tag;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

@Path("../rest/custom/harting/rfidhardwaremonitor")
public interface RFIDHardwareMonitorServiceAsync extends RestService {

	@GET
	@Path("tags")
	void getTags(MethodCallback<List<Tag>> callback);

	@POST
	@Path("observing")
	void setObserving(Boolean observing, MethodCallback<Void> callback);

	@GET
	@Path("observing")
	void isObserving(MethodCallback<Boolean> callback);
}