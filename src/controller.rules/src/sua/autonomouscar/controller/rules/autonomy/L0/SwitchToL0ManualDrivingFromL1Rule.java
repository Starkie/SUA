package sua.autonomouscar.controller.rules.autonomy.L0;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL0_DrivingService;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;

/**
 * Rule to switch from an active {@link IL1_DrivingService} to a {@link IL0_DrivingService} if any of the required sensors stops being available.
 */
public class SwitchToL0ManualDrivingFromL1Rule extends SwitchToL0ManualDrivingBase {

	private BundleContext context;

	public SwitchToL0ManualDrivingFromL1Rule(BundleContext context) {
		super(context);
	}

    protected boolean evaluateRuleCondition(
        CurrentDrivingServiceStatus currentDrivingServiceStatus,
        DistanceSensorHealthStatus frontDistanceSensorHealthStatus,
        LineSensorsHealthStatus leftLineSensorsHealthStatus,
        LineSensorsHealthStatus rightLineSensorsHealthStatus)
    {
        // If the current autonomy level is L1 and any of the sensors is not available.
        boolean oneOrMoreSensorsUnavailable = !frontDistanceSensorHealthStatus.isAvailable()
            || !leftLineSensorsHealthStatus.isAvailable()
            || !rightLineSensorsHealthStatus.isAvailable();

        return currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L1
                && oneOrMoreSensorsUnavailable;
    }
}
