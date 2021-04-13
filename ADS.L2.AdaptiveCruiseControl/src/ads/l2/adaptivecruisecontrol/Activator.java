package ads.l2.adaptivecruisecontrol;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ADS.interfaces.IL2_ACC;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected IL2_ACC theACC = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		theACC = new ACC(bundleContext);
		theACC.start();
		
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		
		theACC.stop();
		Activator.context = null;
	}

}
