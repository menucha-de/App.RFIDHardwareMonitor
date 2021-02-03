package havis.custom.harting.rfidhardwaremonitor.rest.provider;

import havis.custom.harting.rfidhardwaremonitor.RFIDHardwareMonitorException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RFIDHardwareMonitorExceptionMapper implements
		ExceptionMapper<RFIDHardwareMonitorException> {

	@Override
	public Response toResponse(RFIDHardwareMonitorException e) {
		return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage())
				.type(MediaType.TEXT_PLAIN).build();
	}
}