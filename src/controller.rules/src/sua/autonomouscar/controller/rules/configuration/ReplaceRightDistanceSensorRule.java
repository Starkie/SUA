package sua.autonomouscar.controller.rules.configuration;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.parkintheroadshoulderfallbackplan.ParkInTheRoadShoulderFallbackPlan;
import sua.autonomouscar.infrastructure.driving.L3_DrivingService;

/**
 * Replaces the RIGHT {@link IDistanceSensor} in the current {@link IDrivingService} if:
 *  1. A better one becomes available.
 *  2. The current sensor fails and there is backup available.
 */
public class ReplaceRightDistanceSensorRule extends ReplaceDistanceSensorRuleBase {

    public ReplaceRightDistanceSensorRule(BundleContext context) {
        super(context, DistanceSensorPositon.REAR);
    }

    @Override
    protected void replaceDistanceSensor(IDrivingService currentDrivingService, DistanceSensorHealthStatus distanceSensorHealthStatus)
    {
        String sensorId = distanceSensorHealthStatus.getBestDistanceSensorId();

        // No common class exists between L3_DrivingService and ParkInTheRoadShoulder that accepts a
        // right distance sensor. Each must be treated separately.
        if (currentDrivingService instanceof L3_DrivingService)
        {
            L3_DrivingService drivingService = (L3_DrivingService) currentDrivingService;

            if (drivingService != null)
            {
                drivingService.setRearDistanceSensor(sensorId);
                distanceSensorHealthStatus.setActiveDistanceSensorId(sensorId);
            }
        }
        else if (currentDrivingService instanceof ParkInTheRoadShoulderFallbackPlan)
        {
            ParkInTheRoadShoulderFallbackPlan fallbackPlan = (ParkInTheRoadShoulderFallbackPlan) currentDrivingService;

            if (fallbackPlan != null)
            {
                fallbackPlan.setRightDistanceSensor(sensorId);
                distanceSensorHealthStatus.setActiveDistanceSensorId(sensorId);
            }
        }
    }

    @Override
    protected boolean evaluateRuleCondition(CurrentDrivingServiceStatus currentDrivingServiceStatus, DistanceSensorHealthStatus rightDistanceSensorHealthStatus) {
        // Only the L3 driving modes or the ParkInRoadShoulderFallBackPlan require a right distance sensor.
        boolean isDrivingServiceWithRightDistanceSensor =
            currentDrivingServiceStatus.getClass() != null
            && (currentDrivingServiceStatus.getAutonomyLevel() == DrivingAutonomyLevel.L3
                || ParkInTheRoadShoulderFallbackPlan.class.isAssignableFrom(currentDrivingServiceStatus.getClass()));

        return rightDistanceSensorHealthStatus.isAvailable()
            && rightDistanceSensorHealthStatus.getActiveDistanceSensorId() != rightDistanceSensorHealthStatus.getBestDistanceSensorId()
            && isDrivingServiceWithRightDistanceSensor;
    }
}
