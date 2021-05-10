package sua.autonomouscar.controller.rules.configuration;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.infrastructure.driving.L3_DrivingService;

/**
 * Replaces the REAR {@link IDistanceSensor} in the current {@link IDrivingService} if:
 *  1. A better one becomes available.
 *  2. The current sensor fails and there is backup available.
 */
public class ReplaceRearDistanceSensorRule extends ReplaceDistanceSensorRuleBase {

    public ReplaceRearDistanceSensorRule(BundleContext context) {
        super(context, DistanceSensorPositon.REAR);
    }

    @Override
    protected void replaceDistanceSensor(IDrivingService currentDrivingService, DistanceSensorHealthStatus distanceSensorHealthStatus)
    {
        // The L2_DrivingService is the common class between all the driving services that have a rear distance sensor.
        // Otherwise, this rule should be implemented once for each level.
        L3_DrivingService drivingService = (L3_DrivingService) currentDrivingService;

        if (drivingService != null)
        {
            drivingService.setRearDistanceSensor(distanceSensorHealthStatus.getBestDistanceSensorId());
        }
    }

    @Override
    protected boolean evaluateRuleCondition(CurrentDrivingServiceStatus currentDrivingServiceStatus, DistanceSensorHealthStatus rearDistanceSensorHealthStatus) {
        // Only the L3 driving modes require a rear distance sensor.
        boolean isDrivingServiceWithRearDistanceSensor =
                currentDrivingServiceStatus.getClass() != null
                && currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L3;

        return rearDistanceSensorHealthStatus.isAvailable()
            && isDrivingServiceWithRearDistanceSensor;
    }
}
