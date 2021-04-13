package device.engine;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import devices.interfaces.IEngine;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected IEngine theEngine = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		theEngine = new Engine();
		theEngine.setRPM(2000);
		
		bundleContext.registerService(IEngine.class, theEngine, null);
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		
		theEngine.setRPM(0);		
		
		Activator.context = null;
	}

}
