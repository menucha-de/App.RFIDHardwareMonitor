package havis.app.rfidhardwaremonitor.rest;

import havis.app.rfidhardwaremonitor.Main;
import havis.app.rfidhardwaremonitor.rest.provider.RFIDHardwareMonitorExceptionMapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;

public class RESTApplication extends Application {

	private final static String PROVIDERS = "javax.ws.rs.ext.Providers";

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	private Map<String, Object> properties = new HashMap<>();

	public RESTApplication(Main main) {
		singletons.add(new RFIDHardwareMonitorService(main));
		properties.put(PROVIDERS, new Class<?>[] { RFIDHardwareMonitorExceptionMapper.class });
	}

	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}
}