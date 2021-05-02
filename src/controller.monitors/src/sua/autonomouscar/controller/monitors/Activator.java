package sua.autonomouscar.controller.monitors;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.monitors.car.DrivingServiceMonitor;
import sua.autonomouscar.controller.monitors.car.EngineHealthMonitor;
import sua.autonomouscar.controller.monitors.road.IRoadContextMonitor;
import sua.autonomouscar.controller.monitors.road.RoadContextMonitor;

public class Activator implements BundleActivator {

	private static BundleContext context;

	private ServiceRegistration<IRoadContextMonitor> roadContextServiceRegistration;
    private ServiceRegistration<DrivingServiceMonitor> drivingServiceMonitorRegistration;
    private ServiceRegistration<EngineHealthMonitor> engineHealthMonitor;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		IRoadContextMonitor roadContextMonitor = new RoadContextMonitor(bundleContext);
		this.roadContextServiceRegistration = context.registerService(IRoadContextMonitor.class, roadContextMonitor, null);
		
		DrivingServiceMonitor drivingServiceMonitor = new DrivingServiceMonitor(bundleContext);
        this.drivingServiceMonitorRegistration = context.registerService(DrivingServiceMonitor.class, drivingServiceMonitor, null);
        
        EngineHealthMonitor engineHealthMonitor = new EngineHealthMonitor(bundleContext);
        this.engineHealthMonitor = context.registerService(EngineHealthMonitor.class, engineHealthMonitor, null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		this.roadContextServiceRegistration.unregister();
		this.roadContextServiceRegistration = null;
		
		this.drivingServiceMonitorRegistration.unregister();
		this.drivingServiceMonitorRegistration = null;
		
		this.engineHealthMonitor.unregister();
		this.engineHealthMonitor = null;
		
		Activator.context = null;
	}
}
