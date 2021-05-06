package controller.properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.properties.car.EngineHealthStatus;
import sua.autonomouscar.controller.properties.car.FallbackPlanHealthStatus;
import sua.autonomouscar.controller.properties.car.HumanSensorsHealthStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.properties.car.NotificationServiceHealthStatus;
import sua.autonomouscar.controller.properties.car.SteeringHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.LineSensorPosition;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private CurrentDrivingServiceStatus currentDrivingLevel;
    private ServiceRegistration<?> currentDrivingLevelRegistration;

    private DistanceSensorHealthStatus frontDistanceSensorHealthStatus;
    private ServiceRegistration<?> frontDistanceSensorHealthStatusRegistration;

    private DistanceSensorHealthStatus leftDistanceSensorHealthStatus;
    private ServiceRegistration<?> leftDistanceSensorHealthStatusRegistration;

    private DistanceSensorHealthStatus rightDistanceSensorHealthStatus;
    private ServiceRegistration<?> rightDistanceSensorHealthStatusRegistration;

    private DistanceSensorHealthStatus rearDistanceSensorHealthStatus;
    private ServiceRegistration<?> rearDistanceSensorHealthStatusRegistration;

    private EngineHealthStatus engineHealthStatus;
    private ServiceRegistration<?> engineHealthStatusRegistration;

    private HumanSensorsHealthStatus humanSensorsHealthStatus;
    private ServiceRegistration<?> humanSensorsHealthStatusRegistration;

    private LineSensorsHealthStatus leftLineSensorStatus;
    private ServiceRegistration<?> leftLineSensorStatusRegistration;

    private LineSensorsHealthStatus rightLineSensorStatus;
    private ServiceRegistration<?> rightLineSensorStatusRegistration;

    private NotificationServiceHealthStatus notificationServiceStatus;
    private ServiceRegistration<?> notificationServiceStatusRegistration;

    private RoadContext roadContext;
    private ServiceRegistration<?>roadContextRegistration;

    private SteeringHealthStatus steeringHealthStatus;
    private ServiceRegistration<?> steeringHealthStatusRegistration;
    private FallbackPlanHealthStatus fallbackPlanHealthStatus;
    private ServiceRegistration<?> fallbackPlanHealthStatusRegistration;

	public void start(BundleContext bundleContext) throws Exception {
	    Activator.context = bundleContext;

        this.currentDrivingLevel = new CurrentDrivingServiceStatus(context);
        this.currentDrivingLevelRegistration = this.currentDrivingLevel.registerKnowledge();

        this.frontDistanceSensorHealthStatus = new DistanceSensorHealthStatus(bundleContext, DistanceSensorPositon.FRONT);
        this.frontDistanceSensorHealthStatusRegistration  = this.frontDistanceSensorHealthStatus.registerKnowledge();

        this.leftDistanceSensorHealthStatus = new DistanceSensorHealthStatus(bundleContext, DistanceSensorPositon.LEFT);
        this.leftDistanceSensorHealthStatusRegistration  = this.leftDistanceSensorHealthStatus.registerKnowledge();

        this.rightDistanceSensorHealthStatus = new DistanceSensorHealthStatus(bundleContext, DistanceSensorPositon.RIGHT);
        this.rightDistanceSensorHealthStatusRegistration  = this.rightDistanceSensorHealthStatus.registerKnowledge();

        this.rearDistanceSensorHealthStatus = new DistanceSensorHealthStatus(bundleContext, DistanceSensorPositon.REAR);
        this.rearDistanceSensorHealthStatusRegistration  = this.rearDistanceSensorHealthStatus.registerKnowledge();

        this.leftLineSensorStatus = new LineSensorsHealthStatus(bundleContext, LineSensorPosition.LEFT);
        this.leftLineSensorStatusRegistration = this.leftLineSensorStatus.registerKnowledge();

        this.rightLineSensorStatus = new LineSensorsHealthStatus(bundleContext, LineSensorPosition.RIGHT);
        this.rightLineSensorStatusRegistration = this.rightLineSensorStatus.registerKnowledge();

        this.engineHealthStatus = new EngineHealthStatus(context);
        this.engineHealthStatusRegistration = this.engineHealthStatus.registerKnowledge();

        this.fallbackPlanHealthStatus = new FallbackPlanHealthStatus(context);
        this.fallbackPlanHealthStatusRegistration = this.fallbackPlanHealthStatus.registerKnowledge();

        this.humanSensorsHealthStatus = new HumanSensorsHealthStatus(context);
        this.humanSensorsHealthStatusRegistration = this.humanSensorsHealthStatus.registerKnowledge();

        this.notificationServiceStatus = new NotificationServiceHealthStatus(context);
        this.notificationServiceStatusRegistration = this.notificationServiceStatus.registerKnowledge();

        this.roadContext = new RoadContext(context);
        this.roadContextRegistration = this.roadContext.registerKnowledge();

        this.steeringHealthStatus = new SteeringHealthStatus(context);
        this.steeringHealthStatusRegistration = this.steeringHealthStatus.registerKnowledge();
	}

	public void stop(BundleContext bundleContext) throws Exception {
        this.currentDrivingLevelRegistration.unregister();
        this.currentDrivingLevel = null;

        this.frontDistanceSensorHealthStatusRegistration.unregister();
        this.frontDistanceSensorHealthStatus = null;

        this.leftDistanceSensorHealthStatusRegistration.unregister();
        this.leftDistanceSensorHealthStatus = null;

        this.rightDistanceSensorHealthStatusRegistration.unregister();
        this.rightDistanceSensorHealthStatus = null;

        this.rearDistanceSensorHealthStatusRegistration.unregister();
        this.rearDistanceSensorHealthStatus = null;

        this.leftLineSensorStatusRegistration.unregister();
        this.leftLineSensorStatus = null;

        this.rightLineSensorStatusRegistration.unregister();
        this.rightLineSensorStatus = null;

        this.engineHealthStatusRegistration.unregister();
        this.engineHealthStatus = null;

        this.fallbackPlanHealthStatusRegistration.unregister();
        this.fallbackPlanHealthStatus = null;

        this.humanSensorsHealthStatusRegistration.unregister();
        this.humanSensorsHealthStatus = null;

        this.notificationServiceStatusRegistration.unregister();
        this.notificationServiceStatus = null;

        this.roadContextRegistration.unregister();
        this.roadContext = null;

        this.steeringHealthStatusRegistration.unregister();
        this.steeringHealthStatus = null;

		Activator.context = null;
	}
}
