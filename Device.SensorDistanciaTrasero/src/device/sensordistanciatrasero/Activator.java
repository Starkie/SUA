package device.sensordistanciatrasero;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import devices.interfaces.ESensorPosition;
import devices.interfaces.IDistanceSensor;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected IDistanceSensor theSensor = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		theSensor = new SensorDistancia();
		
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put("position", ESensorPosition.REAR);
		
		this.context.registerService(IDistanceSensor.class, theSensor, props);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
