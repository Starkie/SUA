package sua.autonomouscar.controller;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.interfaces.IAdaptionRule;
import sua.autonomouscar.controller.interfaces.IMonitor;
import sua.autonomouscar.controller.monitors.road.RoadContextMonitor;
import sua.autonomouscar.controller.probes.road.RoadStatusProbe;
import sua.autonomouscar.controller.probes.road.RoadTypeProbe;
import sua.autonomouscar.simulation.interfaces.ISimulationElement;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private SimulationContoller controller;
	private ServiceRegistration<?> serviceReference;

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		// Register the simulation controller, that will execute a loop iteration on each step.
		this.controller = new SimulationContoller(null, null);

		this.serviceReference = bundleContext.registerService(
				new String[] {ISimulationElement.class.getName(), Controller.class.getName()},
				this.controller,
				null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		this.serviceReference.unregister();
		this.controller = null;
		Activator.context = null;
	}
}
