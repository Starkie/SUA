package controller.properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.EngineHealthStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.properties.car.NotificationServiceHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.utils.LineSensorPosition;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private CurrentDrivingServiceStatus currentDrivingLevel;
    private ServiceRegistration<?> currentDrivingLevelRegistration;

    private EngineHealthStatus engineHealthStatus;
    private ServiceRegistration<?> engineHealthStatusRegistration;

    private LineSensorsHealthStatus leftLineSensorStatus;
    private ServiceRegistration<?> leftLineSensorStatusRegistration;
    
    private LineSensorsHealthStatus rightLineSensorStatus;
    private ServiceRegistration<?> rightLineSensorStatusRegistration;
    
    private NotificationServiceHealthStatus notificationServiceStatus;
    private ServiceRegistration<?> notificationServiceStatusRegistration;

    private RoadContext roadContext;
    private ServiceRegistration<?>roadContextRegistration;

	public void start(BundleContext bundleContext) throws Exception {
	    Activator.context = bundleContext;

        this.currentDrivingLevel = new CurrentDrivingServiceStatus(context);
        this.currentDrivingLevelRegistration = this.currentDrivingLevel.registerKnowledge();
        
        this.leftLineSensorStatus = new LineSensorsHealthStatus(bundleContext, LineSensorPosition.LEFT);
        this.leftLineSensorStatusRegistration = this.leftLineSensorStatus.registerKnowledge();
        
        this.rightLineSensorStatus = new LineSensorsHealthStatus(bundleContext, LineSensorPosition.RIGHT);
        this.rightLineSensorStatusRegistration = this.rightLineSensorStatus.registerKnowledge();

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
        
        this.leftLineSensorStatusRegistration.unregister();
        this.rightLineSensorStatusRegistration.unregister();

        this.currentDrivingLevel = null;
        this.engineHealthStatus = null;
        this.leftLineSensorStatus = null;
        this.notificationServiceStatus = null;
        this.roadContext = null;
        this.rightLineSensorStatus = null;

		Activator.context = null;
	}
}
