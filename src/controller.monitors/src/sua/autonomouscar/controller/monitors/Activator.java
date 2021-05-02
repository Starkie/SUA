package sua.autonomouscar.controller.monitors;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.monitors.car.DrivingServiceMonitor;
import sua.autonomouscar.controller.monitors.car.EngineHealthMonitor;
import sua.autonomouscar.controller.monitors.car.NotificationServiceHealthMonitor;
import sua.autonomouscar.controller.monitors.road.IRoadContextMonitor;
import sua.autonomouscar.controller.monitors.road.RoadContextMonitor;

public class Activator implements BundleActivator {

	private static BundleContext context;

    private ServiceRegistration<DrivingServiceMonitor> drivingServiceMonitorRegistration;
    private ServiceRegistration<EngineHealthMonitor> engineHealthMonitor;
    private ServiceRegistration<IRoadContextMonitor> roadContextServiceRegistration;
    private ServiceRegistration<NotificationServiceHealthMonitor> notificationServiceHealthMonitorRegistration;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		DrivingServiceMonitor drivingServiceMonitor = new DrivingServiceMonitor(bundleContext);
        this.drivingServiceMonitorRegistration = context.registerService(DrivingServiceMonitor.class, drivingServiceMonitor, null);

        EngineHealthMonitor engineHealthMonitor = new EngineHealthMonitor(bundleContext);
        this.engineHealthMonitor = context.registerService(EngineHealthMonitor.class, engineHealthMonitor, null);

        NotificationServiceHealthMonitor notificationServiceHealthMonitor = new NotificationServiceHealthMonitor(bundleContext);
        this.notificationServiceHealthMonitorRegistration = context.registerService(NotificationServiceHealthMonitor.class, notificationServiceHealthMonitor, null);

        IRoadContextMonitor roadContextMonitor = new RoadContextMonitor(bundleContext);
        this.roadContextServiceRegistration = context.registerService(IRoadContextMonitor.class, roadContextMonitor, null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		this.drivingServiceMonitorRegistration.unregister();
		this.drivingServiceMonitorRegistration = null;

		this.engineHealthMonitor.unregister();
		this.engineHealthMonitor = null;

		this.notificationServiceHealthMonitorRegistration.unregister();
		this.notificationServiceHealthMonitorRegistration = null;

		this.roadContextServiceRegistration.unregister();
		this.roadContextServiceRegistration = null;

		Activator.context = null;
	}
}
