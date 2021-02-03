package havis.custom.harting.rfidhardwaremonitor.osgi;

import havis.custom.harting.rfidhardwaremonitor.Main;
import havis.custom.harting.rfidhardwaremonitor.rest.RESTApplication;
import havis.util.monitor.Monitor;

import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.ws.rs.core.Application;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

	private final static Logger log = Logger.getLogger(Activator.class.getName());

	private final static String MONITOR_NAME = "tag";
	private final static String MONITOR_VALUE = "reader";

	private ServiceRegistration<Monitor> monitor;

	private Main main;
	private ServiceRegistration<Application> app;

	@Override
	public void start(BundleContext context) throws Exception {
		long startTime = new Date().getTime();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(Activator.class.getClassLoader());
			main = new Main();
		} finally {
			Thread.currentThread().setContextClassLoader(loader);
		}

		app = context.registerService(Application.class, new RESTApplication(main), null);

		monitor = context.registerService(Monitor.class, main.getMonitor(),
				new Hashtable<String, String>() {
					private static final long serialVersionUID = 1L;
					{
						put(MONITOR_NAME, MONITOR_VALUE);
					}
				});

		log.info("Bundle start took " + (new Date().getTime() - startTime) + "ms");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (monitor != null) {
			monitor.unregister();
			monitor = null;
		}

		if (app != null) {
			app.unregister();
			app = null;
		}
	}
}