package sua.autonomouscar.controller.rules;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL0_DrivingService;
import sua.autonomouscar.driving.interfaces.IL1_AssistedDriving;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;
import sua.autonomouscar.driving.l1.assisteddriving.L1_AssistedDriving;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;

/**
 * Rule to switch from an active {@link IL0_DrivingService} to a {@link IL1_DrivingService} if all the required sensors are available.
 */
public class SwitchToL1AssistedDrivingFromL0Rule extends AdaptionRuleBase {
    // The default longitudinal security distance is of 100m (10000 cm).
    private static final int LONGITUDINAL_SECURITY_DISTANCE = 10000;

	private BundleContext context;

	public SwitchToL1AssistedDrivingFromL0Rule(BundleContext context) {
		this.context = context;
	}

	@Override
	public void evaluateAndExecute() {
	    CurrentDrivingServiceStatus currentDrivingServiceStatus = OSGiUtils.getService(context, CurrentDrivingServiceStatus.class);
	    DistanceSensorHealthStatus frontDistanceSensorHealthStatus = OSGiUtils.getService(context, DistanceSensorHealthStatus.class, String.format("(%s=%s)", "position", DistanceSensorPositon.FRONT));
        LineSensorsHealthStatus leftLineSensorsHealthStatus = OSGiUtils.getService(context, LineSensorsHealthStatus.class, String.format("(%s=%s)", "position", LineSensorPosition.LEFT));
        LineSensorsHealthStatus rightLineSensorsHealthStatus = OSGiUtils.getService(context, LineSensorsHealthStatus.class, String.format("(%s=%s)", "position", LineSensorPosition.RIGHT));

        if (currentDrivingServiceStatus == null
            || frontDistanceSensorHealthStatus == null
            || leftLineSensorsHealthStatus == null
            || rightLineSensorsHealthStatus == null
            || !evaluateRuleCondition(currentDrivingServiceStatus, frontDistanceSensorHealthStatus, leftLineSensorsHealthStatus, rightLineSensorsHealthStatus))
        {
            return;
        }

        System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");

        IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);

		ServiceReference<IL1_AssistedDriving> l1DrivingServiceReference = context.getServiceReference(IL1_AssistedDriving.class);

		IL1_AssistedDriving l1DrivingService;

		if (l1DrivingServiceReference != null)
        {
		    l1DrivingService = context.getService(l1DrivingServiceReference);
        }
        else
        {
            l1DrivingService = initializeL1AssistedDriving();
        }

		IDistanceSensor distanceSensor = AutonomousVehicleContextUtils.findDistanceSensor(context, DistanceSensorPositon.FRONT);

		ILineSensor leftLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.LEFT);
		ILineSensor rightLineSensor = AutonomousVehicleContextUtils.findLineSensor(context, LineSensorPosition.RIGHT);

		l1DrivingService.setFrontDistanceSensor(((Thing)distanceSensor).getId());
		l1DrivingService.setLeftLineSensor(((Thing)leftLineSensor).getId());
		l1DrivingService.setRightLineSensor(((Thing)rightLineSensor).getId());

		l1DrivingService.setLongitudinalSecurityDistance(LONGITUDINAL_SECURITY_DISTANCE);

		// Unregister the current driving service and replace it with the L1_AssistedDriving.
		if (currentDrivingService != null)
		{
		    ((Thing)currentDrivingService).unregisterThing();
		}

		l1DrivingService.startDriving();
	}

    private boolean evaluateRuleCondition(CurrentDrivingServiceStatus currentDrivingServiceStatus, DistanceSensorHealthStatus frontDistanceSensorHealthStatus, LineSensorsHealthStatus leftLineSensorsHealthStatus, LineSensorsHealthStatus rightLineSensorsHealthStatus) {
        return currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L0
                && frontDistanceSensorHealthStatus.isAvailable()
                && leftLineSensorsHealthStatus.isAvailable()
                && rightLineSensorsHealthStatus.isAvailable();
    }

    private IL1_AssistedDriving initializeL1AssistedDriving() {
        L1_AssistedDriving assistedDriving = new L1_AssistedDriving(context, "L1_AssistedDriving");
        assistedDriving.registerThing();

        return assistedDriving;
    }
}
