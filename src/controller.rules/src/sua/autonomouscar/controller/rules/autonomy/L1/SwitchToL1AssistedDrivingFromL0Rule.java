package sua.autonomouscar.controller.rules.autonomy.L1;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.properties.car.LineSensorsHealthStatus;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.driving.interfaces.IL0_DrivingService;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;

/**
 * Rule to switch from an active {@link IL0_DrivingService} to a {@link IL1_DrivingService} if all the required sensors are available.
 */
public class SwitchToL1AssistedDrivingFromL0Rule extends SwitchToL1AssistedDrivingRuleBase {
    public SwitchToL1AssistedDrivingFromL0Rule(BundleContext context) {
        super(context);
    }

    protected boolean evaluateRuleCondition(CurrentDrivingServiceStatus currentDrivingServiceStatus, DistanceSensorHealthStatus frontDistanceSensorHealthStatus, LineSensorsHealthStatus leftLineSensorsHealthStatus, LineSensorsHealthStatus rightLineSensorsHealthStatus) {
        boolean areLineSensorsAvailable = leftLineSensorsHealthStatus.isAvailable()
            && rightLineSensorsHealthStatus.isAvailable();

        return currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L0
            && frontDistanceSensorHealthStatus.isAvailable()
            && areLineSensorsAvailable;
    }
}
