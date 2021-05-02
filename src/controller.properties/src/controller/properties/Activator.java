package controller.properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.EngineHealthStatus;
import sua.autonomouscar.controller.properties.car.NotificationServiceHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private CurrentDrivingServiceStatus currentDrivingLevel;
    private ServiceRegistration<?> currentDrivingLevelRegistration;

    private EngineHealthStatus engineHealthStatus;
    private ServiceRegistration<?> engineHealthStatusRegistration;

    private NotificationServiceHealthStatus notificationServiceStatus;
    private ServiceRegistration<?> notificationServiceStatusRegistration;

    private RoadContext roadContext;
    private ServiceRegistration<?>roadContextRegistration;

	public void start(BundleContext bundleContext) throws Exception {
	    Activator.context = bundleContext;

        this.currentDrivingLevel = new CurrentDrivingServiceStatus(context);
        this.currentDrivingLevelRegistration = this.currentDrivingLevel.registerKnowledge();

        this.engineHealthStatus = new EngineHealthStatus(context);
        this.engineHealthStatusRegistration = this.engineHealthStatus.registerKnowledge();

        this.notificationServiceStatus = new NotificationServiceHealthStatus(context);
        this.notificationServiceStatusRegistration = this.notificationServiceStatus.registerKnowledge();

        this.roadContext = new RoadContext(context);
        this.roadContextRegistration = this.roadContext.registerKnowledge();
	}

	public void stop(BundleContext bundleContext) throws Exception {
        this.currentDrivingLevelRegistration.unregister();
        this.engineHealthStatusRegistration.unregister();
        this.notificationServiceStatusRegistration.unregister();
        this.roadContextRegistration.unregister();

        this.currentDrivingLevel = null;
        this.engineHealthStatus = null;
        this.notificationServiceStatus = null;
        this.roadContext = null;

		Activator.context = null;
	}
}
