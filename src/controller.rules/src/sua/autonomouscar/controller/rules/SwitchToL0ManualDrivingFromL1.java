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
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL0_DrivingService;
import sua.autonomouscar.driving.interfaces.IL0_ManualDriving;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;
import sua.autonomouscar.driving.l0.manual.L0_ManualDriving;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.Thing;

/**
 * Rule to switch from an active {@link IL1_DrivingService} to a {@link IL0_DrivingService} if any of the required sensors stops being available.
 */
public class SwitchToL0ManualDrivingFromL1 extends AdaptionRuleBase {

	private BundleContext context;

	public SwitchToL0ManualDrivingFromL1(BundleContext context) {
		this.context = context;
	}

	/**
	 * The condition to execute the rule: currentDrivingService instanceof IL1_DrivingService && is-driver-ready && !(are-line-sensors-available && (is-front-distance-sensor-available || is-lidar-available)
	 */
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

		IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(context);

		System.out.println("[ Controller ] Executing the " + this.getClass().getSimpleName() + " rule.");

		ServiceReference<IL0_ManualDriving> l0DrivingServiceReference = context.getServiceReference(IL0_ManualDriving.class);

		IL0_ManualDriving l0DrivingService;

		if (l0DrivingServiceReference != null)
		{
		    l0DrivingService = context.getService(l0DrivingServiceReference);
		}
		else
		{
		    l0DrivingService = initializeL0ManualDriving();
		}

		// Unregister the current driving service and replace it with the L0_ManualDriving.
        if (currentDrivingService != null)
        {
            ((Thing)currentDrivingService).unregisterThing();
        }

		l0DrivingService.startDriving();
	}

    private boolean evaluateRuleCondition(CurrentDrivingServiceStatus currentDrivingServiceStatus, DistanceSensorHealthStatus frontDistanceSensorHealthStatus, LineSensorsHealthStatus leftLineSensorsHealthStatus, LineSensorsHealthStatus rightLineSensorsHealthStatus) {
        // If the current autonomy level is L1 and any of the sensors is not available.
        return currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L1
                && (!frontDistanceSensorHealthStatus.isAvailable()
                    || !leftLineSensorsHealthStatus.isAvailable()
                    || !rightLineSensorsHealthStatus.isAvailable());
    }

    private IL0_ManualDriving initializeL0ManualDriving() {
        L0_ManualDriving manualDriving = new L0_ManualDriving(context, "L0_ManualDriving");
        manualDriving.registerThing();

        return manualDriving;
    }
}
