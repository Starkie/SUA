package simulacion.instaladorruntime;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected Bundle b = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		b = this.context.installBundle("http://localhost:8080/plugins/ADS.L2.AdaptiveCruiseControl_1.0.0.202103311702.jar");
		b.start();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		b.uninstall();
		Activator.context = null;
	}

}
