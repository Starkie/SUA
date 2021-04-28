package sua.autonomouscar.controller.monitors;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

	private static BundleContext context;

	private ServiceRegistration<IRoadContextMonitor> roadContextServiceRegistration;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		IRoadContextMonitor roadContextMonitor = new RoadContextMonitor(bundleContext);
		this.roadContextServiceRegistration = context.registerService(IRoadContextMonitor.class, roadContextMonitor,
				null);

	}

	public void stop(BundleContext bundleContext) throws Exception {
		this.roadContextServiceRegistration.unregister();
		this.roadContextServiceRegistration = null;
		Activator.context = null;
	}

}
