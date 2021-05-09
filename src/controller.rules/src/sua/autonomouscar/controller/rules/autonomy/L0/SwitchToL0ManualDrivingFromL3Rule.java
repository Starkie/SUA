package sua.autonomouscar.controller.rules.autonomy.L0;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL0_DrivingService;
import sua.autonomouscar.driving.interfaces.IL3_DrivingService;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * Rule to switch from an active {@link IL3_DrivingService} to a {@link IL0_DrivingService} if any of the required sensors stops being available.
 */
public class SwitchToL0ManualDrivingFromL3Rule extends SwitchToL0ManualDrivingBase {
	public SwitchToL0ManualDrivingFromL3Rule(BundleContext context) {
		super(context);
	}

    protected boolean evaluateRuleCondition(
        CurrentDrivingServiceStatus currentDrivingServiceStatus,
        DistanceSensorHealthStatus frontDistanceSensorHealthStatus,
        LineSensorsHealthStatus leftLineSensorsHealthStatus,
        LineSensorsHealthStatus rightLineSensorsHealthStatus)
    {
        // If the current autonomy level is L3 and any of the sensors is not available.
        boolean oneOrMoreSensorsUnavailable = !frontDistanceSensorHealthStatus.isAvailable()
            || !leftLineSensorsHealthStatus.isAvailable()
            || !rightLineSensorsHealthStatus.isAvailable();

        RoadContext roadContext = OSGiUtils.getService(context, RoadContext.class);

        return currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L3
                && oneOrMoreSensorsUnavailable
                && roadContext != null && roadContext.getType() == ERoadType.OFF_ROAD;
    }
}
