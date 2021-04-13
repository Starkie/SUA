package simulacion.sensortipocarretera.highway;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import context.interfaces.ERoadType;
import context.interfaces.IRoadContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		IRoadContext contextoCarretera = null;
		ServiceReference ref = this.context.getServiceReference(IRoadContext.class);
		if ( ref != null ) {
			System.out.println("[Sensor Tipo Carretera] Setting road type to HIGHWAY");
			contextoCarretera = (IRoadContext) this.context.getService(ref);
			contextoCarretera.setType(ERoadType.HIGHWAY);
		}
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
