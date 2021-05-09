package sua.autonomouscar.controller.rules.autonomy.L1;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL0_DrivingService;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interfaces.ERoadType;

/**
 * Rule to switch from an active {@link IL0_DrivingService} to a {@link IL1_DrivingService} if all the required sensors are available.
 */
public class SwitchToL1AssistedDrivingFromL3Rule extends SwitchToL1AssistedDrivingRuleBase {
    public SwitchToL1AssistedDrivingFromL3Rule(BundleContext context) {
        super(context);
    }

    protected boolean evaluateRuleCondition(CurrentDrivingServiceStatus currentDrivingServiceStatus, DistanceSensorHealthStatus frontDistanceSensorHealthStatus, LineSensorsHealthStatus leftLineSensorsHealthStatus, LineSensorsHealthStatus rightLineSensorsHealthStatus) {
        RoadContext roadContext = OSGiUtils.getService(context, RoadContext.class);

        boolean areLineSensorsAvailable = leftLineSensorsHealthStatus.isAvailable()
            && rightLineSensorsHealthStatus.isAvailable();

        return currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L0
            && roadContext != null && roadContext.getType() == ERoadType.OFF_ROAD
            && frontDistanceSensorHealthStatus.isAvailable()
            && areLineSensorsAvailable;
    }
}
