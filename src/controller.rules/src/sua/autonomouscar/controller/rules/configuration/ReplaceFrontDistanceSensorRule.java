package sua.autonomouscar.controller.rules.configuration;

import org.osgi.framework.BundleContext;
import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.car.DistanceSensorHealthStatus;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IFallbackPlan;
import sua.autonomouscar.driving.l2.lka.L2_LaneKeepingAssist;
import sua.autonomouscar.infrastructure.driving.L1_DrivingService;

/**
 * Replaces the FRONT {@link IDistanceSensor} in the current {@link IDrivingService} if:
 *  1. A better one becomes available.
 *  2. The current sensor fails and there is backup available.
 */
public class ReplaceFrontDistanceSensorRule extends ReplaceDistanceSensorRuleBase {

    public ReplaceFrontDistanceSensorRule(BundleContext context) {
        super(context, DistanceSensorPositon.FRONT);
    }

    @Override
    protected void replaceDistanceSensor(IDrivingService currentDrivingService, DistanceSensorHealthStatus distanceSensorHealthStatus)
    {
        // The L1_DrivingService is the common class between all the driving services that have a front distance sensor.
        // Otherwise, this rule should be implemented once for each level.
        L1_DrivingService drivingService = (L1_DrivingService) currentDrivingService;

        if (drivingService != null)
        {
            String sensorId = distanceSensorHealthStatus.getBestDistanceSensorId();

            drivingService.setFrontDistanceSensor(sensorId);
            distanceSensorHealthStatus.setActiveDistanceSensorId(sensorId);
        }
    }

    @Override
    protected boolean evaluateRuleCondition(CurrentDrivingServiceStatus currentDrivingServiceStatus, DistanceSensorHealthStatus frontDistanceSensorHealthStatus) {
        // L0_ManualDriving and L2_LaneKeepingAssist are the only driving modes that do not
        // require a front distance sensor. The fallback plans only use the right sensor.
        boolean isDrivingServiceWithFrontDistanceSensor =
                currentDrivingServiceStatus.getClass() != null
                && currentDrivingServiceStatus.getAutonomyLevel() != DrivingAutonomyLevel.L0
                && !L2_LaneKeepingAssist.class.isAssignableFrom(currentDrivingServiceStatus.getClass())
                && !IFallbackPlan.class.isAssignableFrom(currentDrivingServiceStatus.getClass());

        return frontDistanceSensorHealthStatus.isAvailable()
            && frontDistanceSensorHealthStatus.getActiveDistanceSensorId() != frontDistanceSensorHealthStatus.getBestDistanceSensorId()
            && isDrivingServiceWithFrontDistanceSensor;
    }
}
